package lt.vu.rest;

import lombok.Getter;
import lombok.Setter;
import lt.vu.entities.Customer;
import lt.vu.entities.EOrder;
import lt.vu.persistence.CustomersDAO;
import lt.vu.persistence.EOrdersDAO;
import lt.vu.rest.contracts.CustomerDto;
import lt.vu.rest.contracts.EOrderDto;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
@Path("/customers")
public class CustomersController {

    @Inject
    @Setter @Getter
    private CustomersDAO customersDAO;

    @Inject
    @Setter @Getter
    private EOrdersDAO eOrdersDAO;

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") final Long id) {
        Customer customer = customersDAO.findOne(id);
        if (customer == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(customer.getId());
        customerDto.setFirstName(customer.getFirstName());
        customerDto.setLastName(customer.getLastName());
        customerDto.setEmail(customer.getEmail());

        return Response.ok(customerDto).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<Customer> customers = customersDAO.findAll();

        if (customers == null || customers.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        List<CustomerDto> customerDtos = customers.stream().map(customer -> {
            CustomerDto customerDto = new CustomerDto();
            customerDto.setId(customer.getId());
            customerDto.setFirstName(customer.getFirstName());
            customerDto.setLastName(customer.getLastName());
            customerDto.setEmail(customer.getEmail());
            return customerDto;
        }).collect(Collectors.toList());

        return Response.ok(customerDtos).build();
    }

    @Path("/{id}/orders")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCustomerOrders(@PathParam("id") final Long id) {
        List<EOrder> orders = eOrdersDAO.findAll();
        if (orders == null || orders.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        List<EOrderDto> orderDtos = orders.stream()
                .filter(order -> order.getCustomer().getId().equals(id)) // Only include matching customer ID
                .map(order -> {
                    EOrderDto orderDto = new EOrderDto();
                    orderDto.setId(order.getId());
                    orderDto.setCustomerId(order.getCustomer().getId());
                    orderDto.setDate(order.getDate());

                    List<Long> productIds = eOrdersDAO.selectAllOrderProductIds(order.getId());
                    orderDto.setProductIds(productIds);

                    return orderDto;
                })
                .collect(Collectors.toList());

        if (orderDtos.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(orderDtos).build();
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response create(CustomerDto customerData) {

        Customer newCustomer = new Customer();

        newCustomer.setFirstName(customerData.getFirstName());
        newCustomer.setLastName(customerData.getLastName());
        newCustomer.setEmail(customerData.getEmail());

        customersDAO.persist(newCustomer);

        // Update the DTO with the generated id
        customerData.setId(newCustomer.getId());

        return Response
                .status(Response.Status.CREATED)
                .entity(customerData)
                .build();
    }
}

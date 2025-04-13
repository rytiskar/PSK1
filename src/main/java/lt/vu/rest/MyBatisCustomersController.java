package lt.vu.rest;

import lt.vu.entities.EOrder;
import lt.vu.mybatis.model.Eorder;
import lt.vu.mybatis.model.EorderProduct;
import lt.vu.persistence.MyBatisCustomersDAO;
import lt.vu.persistence.MyBatisEOrdersDAO;
import lt.vu.rest.contracts.CustomerDto;
import lt.vu.mybatis.model.Customer;
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
@Path("/myBatis/customers")
public class MyBatisCustomersController {

    @Inject
    private MyBatisCustomersDAO myBatisCustomersDAO;
    @Inject
    private MyBatisEOrdersDAO myBatisEOrdersDAO;

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") final Long id) {
        Customer customer = myBatisCustomersDAO.findOne(id);
        if (customer == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(customer.getId());
        customerDto.setFirstName(customer.getFirstname());
        customerDto.setLastName(customer.getLastname());
        customerDto.setEmail(customer.getEmail());

        return Response.ok(customerDto).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<Customer> customers = myBatisCustomersDAO.findAll();

        if (customers == null || customers.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        List<CustomerDto> customerDtos = customers.stream().map(customer -> {
            CustomerDto customerDto = new CustomerDto();
            customerDto.setId(customer.getId());
            customerDto.setFirstName(customer.getFirstname());
            customerDto.setLastName(customer.getLastname());
            customerDto.setEmail(customer.getEmail());
            return customerDto;
        }).collect(Collectors.toList());

        return Response.ok(customerDtos).build();
    }

    @Path("/{id}/orders")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCustomerOrders(@PathParam("id") final Long id) {
        List<Eorder> orders = myBatisEOrdersDAO.findAll();
        if (orders == null || orders.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        List<EOrderDto> orderDtos = orders.stream()
                .filter(order -> order.getCustomerId().equals(id)) // Only include matching customer ID
                .map(order -> {
                    EOrderDto orderDto = new EOrderDto();
                    orderDto.setId(order.getId());
                    orderDto.setCustomerId(order.getCustomerId());
                    orderDto.setDate(order.getDate());

                    List<Long> productIds = myBatisEOrdersDAO.SelectAllOrderProducts()
                            .stream()
                            .map(EorderProduct::getProductsId)
                            .collect(Collectors.toList());

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

        newCustomer.setFirstname(customerData.getFirstName());
        newCustomer.setLastname(customerData.getLastName());
        newCustomer.setEmail(customerData.getEmail());

        myBatisCustomersDAO.persist(newCustomer);

        // Update the DTO with the generated id
        customerData.setId(newCustomer.getId());

        return Response
                .status(Response.Status.CREATED)
                .entity(customerData)
                .build();
    }
}

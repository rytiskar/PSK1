package lt.vu.rest;

import lombok.Getter;
import lombok.Setter;
import lt.vu.entities.Customer;
import lt.vu.persistence.CustomersDAO;
import lt.vu.persistence.EOrdersDAO;
import lt.vu.rest.contracts.CustomerDto;
import lt.vu.rest.contracts.CustomerWithOrdersAndProductsDto;
import lt.vu.services.CustomerService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@ApplicationScoped
@Path("/customers")
public class CustomersController {

    @Inject
    @Setter @Getter
    private CustomersDAO customersDAO;

    @Inject
    @Setter @Getter
    private EOrdersDAO eOrdersDAO;

    @Inject
    private CustomerService customerService;

/*    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") final Long id) {

        return Response.ok(customerDto).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {

        return Response.ok(customerDtos).build();
    }

    @Path("/{id}/orders")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCustomerOrders(@PathParam("id") final Long id) {

        return Response.ok(orderDtos).build();
    }*/

    @Path("/withOrdersAndProducts")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCustomersWithTheirOrdersAndProducts() {

        List<CustomerWithOrdersAndProductsDto> customers = customerService.getAllCustomersWithTheirOrdersAndProducts();

        return Response.ok(customers).build();
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

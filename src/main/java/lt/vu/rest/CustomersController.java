package lt.vu.rest;

import lt.vu.rest.contracts.CustomerDto;
import lt.vu.rest.contracts.CustomerWithOrdersAndProductsDto;
import lt.vu.services.CustomerService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@ApplicationScoped
@Path("/customers")
public class CustomersController {

    @Inject
    private CustomerService customerService;

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
    public Response create(CustomerDto customerData) {

        customerService.createCustomer(customerData);

        return Response.status(Response.Status.CREATED).build();
    }
}

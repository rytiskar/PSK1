package lt.vu.rest;

import lt.vu.persistence.MyBatisCustomersDAO;
import lt.vu.rest.contracts.CustomerDto;
import lt.vu.mybatis.model.Customer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApplicationScoped
@Path("/myBatis/customers")
public class MyBatisCustomersController {

    @Inject
    private MyBatisCustomersDAO myBatisCustomersDAO;

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


    // This endpoint isn't working yet
    // Error updating database.  Cause: org.h2.jdbc.JdbcSQLException: NULL not allowed for column "ID"; SQL statement
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response create(CustomerDto customerData) {

        Customer newCustomer = new Customer();

        // newCustomer.setId(System.currentTimeMillis());
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

package lt.vu.rest;

import lt.vu.rest.contracts.EOrderDto;
import lt.vu.rest.contracts.ProductDto;
import lt.vu.services.EOrderService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApplicationScoped
@Path("myBatis/orders")
public class MyBatisEOrdersController {

    @Inject
    private EOrderService orderService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(EOrderDto eOrderData) {

        orderService.createOrder(eOrderData);

        return Response.status(Response.Status.CREATED).build();
    }

    @Path("/{id}/addProduct")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addProduct(@PathParam("id") Long orderId, ProductDto product) {

        orderService.addProductToOrder(orderId, product);

        return Response.ok().build();
    }
}

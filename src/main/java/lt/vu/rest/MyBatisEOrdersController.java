package lt.vu.rest;

import lt.vu.rest.contracts.EOrderDto;
import lt.vu.rest.contracts.ProductDto;
import lt.vu.services.MyBatisEOrderService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@ApplicationScoped
@Path("myBatis/orders")
public class MyBatisEOrdersController {

    @Inject
    private MyBatisEOrderService orderService;

    @Path("/{id}/products")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrderProducts(@PathParam("id") final Long orderId) {

        List<Long> orderProductIds = orderService.getOrderProductIds(orderId);

        return Response.ok(orderProductIds).build();
    }

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
    public Response addProductToOrder(@PathParam("id") Long orderId, ProductDto product) {

        orderService.addProductToOrder(orderId, product.getId());

        return Response.ok().build();
    }
}

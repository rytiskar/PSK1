package lt.vu.rest;

import lt.vu.rest.contracts.EOrderDto;
import lt.vu.rest.contracts.ProductDto;
import lt.vu.services.EOrderService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@ApplicationScoped
@Path("/orders")
public class EOrdersController {
    @Inject
    private EOrderService orderService;

    @Path("/{id}/products")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrderProducts(@PathParam("id") final Long id) {

        List<Long> productIds = orderService.getOrderProductIds(id);

        return Response.ok(productIds).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response create(EOrderDto eOrderData) {

        orderService.createOrder(eOrderData);

        return Response.status(Response.Status.CREATED).build();
    }

    @Transactional
    @Path("/{id}/addProduct")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addProductToOrder(@PathParam("id") Long orderId, ProductDto product) {

        orderService.addProductToOrder(orderId, product.getId());

        return Response.ok().build();
    }

}

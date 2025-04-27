package lt.vu.rest;

import lt.vu.rest.contracts.ProductDto;
import lt.vu.services.MyBatisProductService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@ApplicationScoped
@Path("myBatis/products")
public class MyBatisProductsController {

    @Inject
    private MyBatisProductService productService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllProducts() {

        List<ProductDto> products = productService.getAllProducts();

        return Response.ok(products).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(ProductDto productData) {

        productService.createProduct(productData);

        return Response.status(Response.Status.CREATED).build();
    }
}
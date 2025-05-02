package lt.vu.rest;

import lt.vu.rest.contracts.ProductDto;
import lt.vu.services.ProductService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@ApplicationScoped
@Path("/products")
public class ProductsController {
    @Inject
    private ProductService productService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {

        List<ProductDto> products = productService.getAllProducts();

        return Response.ok(products).build();
    }

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrderProducts(@PathParam("id") final Long id) {

        ProductDto product = productService.getProductById(id);

        return Response.ok(product).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(ProductDto productData) {

        productService.createProduct(productData);

        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(ProductDto productData) {

        ProductDto product = productService.updateProduct(productData);

        return Response.ok(product).build();
    }
}

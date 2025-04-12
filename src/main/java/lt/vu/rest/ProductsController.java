package lt.vu.rest;

import lombok.Getter;
import lombok.Setter;
import lt.vu.entities.Product;
import lt.vu.persistence.ProductsDAO;
import lt.vu.rest.contracts.ProductDto;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.OptimisticLockException;
import javax.print.attribute.standard.Media;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApplicationScoped
@Path("/products")
public class ProductsController {
    @Inject
    @Setter @Getter
    private ProductsDAO productsDAO;

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") final Long id) {
        Product product = productsDAO.findOne(id);
        if (product == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        ProductDto productDto = new ProductDto();
        productDto.setName(product.getProductName());
        productDto.setPrice(product.getPrice());

        return Response.ok(productDto).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response create(ProductDto productData) {

        Product newProduct = new Product();

        newProduct.setProductName(productData.getName());
        newProduct.setPrice(productData.getPrice());

        productsDAO.persist(newProduct);

        return Response
                .status(Response.Status.CREATED)
                .entity(productData)
                .build();
    }

    @Path("/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response update(@PathParam("id") final Long id, ProductDto productData) {
        try {
            Product existingProduct = productsDAO.findOne(id);

            if (existingProduct == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            existingProduct.setProductName(productData.getName());
            existingProduct.setPrice(productData.getPrice());

            productsDAO.update(existingProduct);

            return Response.ok().build();

        } catch (OptimisticLockException ole) {

            return Response.status(Response.Status.CONFLICT).build();

        }
    }
}

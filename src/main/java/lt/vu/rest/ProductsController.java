package lt.vu.rest;

import lombok.Getter;
import lombok.Setter;
import lt.vu.entities.Product;
import lt.vu.persistence.ProductsDAO;
import lt.vu.rest.contracts.ProductDto;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.OptimisticLockException;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

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
        productDto.setId(product.getId());
        productDto.setName(product.getProductName());
        productDto.setPrice(product.getPrice());

        return Response.ok(productDto).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<Product> products = productsDAO.findAll();

        if (products == null || products.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No products found.")
                    .build();
        }

        List<ProductDto> productDtos = products.stream().map(product -> {
            ProductDto dto = new ProductDto();
            dto.setId(product.getId());
            dto.setName(product.getProductName());
            dto.setPrice(product.getPrice());
            return dto;
        }).collect(Collectors.toList());

        return Response.ok(productDtos).build();
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

        // Update the DTO with the generated id
        productData.setId(newProduct.getId());

        return Response
                .status(Response.Status.CREATED)
                .entity(productData)
                .build();
    }


}

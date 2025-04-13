package lt.vu.rest;

import lt.vu.mybatis.model.Product;
import lt.vu.persistence.MyBatisProductsDAO;
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
@Path("myBatis/products")
public class MyBatisProductsController {
    @Inject
    private MyBatisProductsDAO myBatisProductsDAO;

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") final Long id) {
        Product product = myBatisProductsDAO.findOne(id);
        if (product == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getProductname());
        productDto.setPrice(product.getPrice());

        return Response.ok(productDto).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllProducts() {
        List<Product> products = myBatisProductsDAO.findAll();

        if (products == null || products.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No products found.")
                    .build();
        }

        List<ProductDto> productDtos = products.stream().map(product -> {
            ProductDto dto = new ProductDto();
            dto.setId(product.getId());
            dto.setName(product.getProductname());
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

        newProduct.setProductname(productData.getName());
        newProduct.setPrice(productData.getPrice());

        myBatisProductsDAO.persist(newProduct);

        // Update the DTO with the generated id
        productData.setId(newProduct.getId());

        return Response
                .status(Response.Status.CREATED)
                .entity(productData)
                .build();
    }
}

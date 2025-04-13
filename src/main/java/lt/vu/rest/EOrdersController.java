package lt.vu.rest;

import lombok.Getter;
import lombok.Setter;
import lt.vu.entities.Customer;
import lt.vu.entities.EOrder;
import lt.vu.entities.Product;
import lt.vu.mybatis.model.Eorder;
import lt.vu.mybatis.model.EorderProduct;
import lt.vu.persistence.CustomersDAO;
import lt.vu.persistence.EOrdersDAO;
import lt.vu.persistence.ProductsDAO;
import lt.vu.rest.contracts.EOrderDto;
import lt.vu.rest.contracts.ProductDto;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
@Path("/orders")
public class EOrdersController {
    @Inject
    @Setter @Getter
    private EOrdersDAO eOrdersDAO;

    @Inject
    @Setter @Getter
    private CustomersDAO customersDAO;

    @Inject
    @Setter @Getter
    private ProductsDAO productsDAO;

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") final Long id) {

        EOrder order = eOrdersDAO.findOne(id);
        if (order == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        EOrderDto orderDto = new EOrderDto();
        orderDto.setCustomerId(order.getCustomer().getId());
        orderDto.setProductIds(order.getProducts().stream().map(Product::getId).collect(Collectors.toList()));
        orderDto.setDate(order.getDate());

        return Response.ok(orderDto).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {

        List<EOrder> orders = eOrdersDAO.findAll();
        if (orders == null || orders.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        List<EOrderDto> orderDtos = orders.stream().map(order -> {
            EOrderDto orderDto = new EOrderDto();

            orderDto.setCustomerId(order.getId());
            orderDto.setCustomerId(order.getCustomer().getId());
            orderDto.setDate(order.getDate());

            List<Long> productIds = eOrdersDAO.selectAllOrderProductIds(order.getId());

            orderDto.setProductIds(productIds);

            return orderDto;
        }).collect(Collectors.toList());

        return Response.ok(orderDtos).build();
    }

    @Path("/{id}/products")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrderProducts(@PathParam("id") final Long id) {

        List<Long> productIds = eOrdersDAO.selectAllOrderProductIds(id);

        return Response.ok(productIds).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response create(EOrderDto eOrderData) {

        Customer customer = customersDAO.findOne(eOrderData.getCustomerId());
        if (customer == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Customer not found").build();
        }

        List<Product> products = productsDAO.findAll(eOrderData.getProductIds());
        if (products.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("Products not found").build();
        }

        EOrder newOrder = new EOrder();
        newOrder.setCustomer(customer);
        newOrder.setProducts(products);
        newOrder.setDate(new Date());

        eOrdersDAO.persist(newOrder);

        return Response.status(Response.Status.CREATED).build();
    }

    @Transactional
    @Path("/{id}/addProduct")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addProduct(@PathParam("id") Long id, ProductDto product) {

        EOrder existingOrder = eOrdersDAO.findOne(id);
        if (existingOrder == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Order with ID " + id + " not found.")
                    .build();
        }

        List<Long> existingProductIds = productsDAO.findAll()
                .stream()
                .map(Product::getId)
                .collect(Collectors.toList());

        if (!existingProductIds.contains(product.getId())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Product id is invalid.")
                    .build();
        }

        Product existingProduct = productsDAO.findOne(product.getId());

        eOrdersDAO.addProductToOrder(id, existingProduct);

        return Response.ok().build();
    }

}

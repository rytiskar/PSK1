package lt.vu.rest;

import lombok.Getter;
import lombok.Setter;
import lt.vu.entities.EOrder;
import lt.vu.mybatis.model.Customer;
import lt.vu.mybatis.model.Eorder;
import lt.vu.mybatis.model.EorderProduct;
import lt.vu.mybatis.model.Product;
import lt.vu.persistence.*;
import lt.vu.rest.contracts.CustomerDto;
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
@Path("myBatis/orders")
public class MyBatisEOrdersController {

    @Inject
    @Setter @Getter
    private MyBatisEOrdersDAO myBatisEOrdersDAO;

    @Inject
    @Setter @Getter
    private MyBatisCustomersDAO myBatisCustomersDAO;

    @Inject
    @Setter @Getter
    private MyBatisProductsDAO myBatisProductsDAO;

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") final Long id) {

        Eorder order = myBatisEOrdersDAO.findOne(id);
        if (order == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        List<Long> orderProducts = myBatisEOrdersDAO.SelectAllOrderProducts()
                .stream()
                .filter(link -> link.getOrdersId().equals(id))
                .map(EorderProduct::getProductsId)
                .collect(Collectors.toList());

        EOrderDto orderDto = new EOrderDto();
        orderDto.setCustomerId(order.getCustomerId());
        orderDto.setProductIds(orderProducts);
        orderDto.setDate(order.getDate());

        return Response.ok(orderDto).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {

        List<Eorder> orders = myBatisEOrdersDAO.findAll();
        if (orders == null || orders.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        List<EOrderDto> orderDtos = orders.stream().map(order -> {
            EOrderDto orderDto = new EOrderDto();

            orderDto.setCustomerId(order.getId());
            orderDto.setCustomerId(order.getCustomerId());
            orderDto.setDate(order.getDate());

            List<Long> orderProducts = myBatisEOrdersDAO.SelectAllOrderProducts()
                    .stream()
                    .filter(link -> link.getOrdersId().equals(order.getId()))
                    .map(EorderProduct::getProductsId)
                    .collect(Collectors.toList());

            orderDto.setProductIds(orderProducts);

            return orderDto;
        }).collect(Collectors.toList());

        return Response.ok(orderDtos).build();
    }

    @Path("/{id}/products")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrderProducts(@PathParam("id") final Long id) {

        List<Long> orderProducts = myBatisEOrdersDAO.SelectAllOrderProducts()
                .stream()
                .filter(link -> link.getOrdersId().equals(id))
                .map(EorderProduct::getProductsId)
                .collect(Collectors.toList());

        return Response.ok(orderProducts).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response create(EOrderDto eOrderData) {

        Customer customer = myBatisCustomersDAO.findOne(eOrderData.getCustomerId());
        if (customer == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Customer not found").build();
        }

        List<Long> existingProductIds = getMyBatisProductsDAO().findAll()
                .stream()
                .map(Product::getId)
                .collect(Collectors.toList());

        List<Long> requestedProducts = eOrderData.getProductIds();

        if (!existingProductIds.containsAll(requestedProducts)) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("One or more product IDs are invalid.")
                    .build();
        }

        Eorder newOrder = new Eorder();
        newOrder.setCustomerId(customer.getId());
        newOrder.setDate(new Date());

        // Insert the new order into the database
        myBatisEOrdersDAO.persist(newOrder);

        // For each product ID, create a new EorderProduct record to associate the product with the newly created order
        for (Long productId : requestedProducts) {
            EorderProduct eorderProduct = new EorderProduct();
            eorderProduct.setOrdersId(newOrder.getId());
            eorderProduct.setProductsId(productId);

            myBatisEOrdersDAO.insertOrderProduct(eorderProduct);
        }

        return Response.status(Response.Status.CREATED).build();
    }

    @Transactional
    @Path("/{id}/addProduct")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addProduct(@PathParam("id") Long id, ProductDto product) {

        Eorder existingOrder = myBatisEOrdersDAO.findOne(id);
        if (existingOrder == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Order with ID " + id + " not found.")
                    .build();
        }

        List<Long> existingProductIds = getMyBatisProductsDAO().findAll()
                .stream()
                .map(Product::getId)
                .collect(Collectors.toList());

        if (!existingProductIds.contains(product.getId())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Product id is invalid.")
                    .build();
        }

        EorderProduct eorderProduct = new EorderProduct();
        eorderProduct.setOrdersId(id);
        eorderProduct.setProductsId(product.getId());

        myBatisEOrdersDAO.insertOrderProduct(eorderProduct);

        return Response.ok().build();
    }
}

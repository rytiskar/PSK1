package lt.vu.rest;

import lombok.Getter;
import lombok.Setter;
import lt.vu.entities.Customer;
import lt.vu.entities.EOrder;
import lt.vu.entities.Product;
import lt.vu.persistence.CustomersDAO;
import lt.vu.persistence.EOrdersDAO;
import lt.vu.persistence.ProductsDAO;
import lt.vu.rest.contracts.EOrderDto;

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

}

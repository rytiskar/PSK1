package lt.vu.services;

import lt.vu.entities.Customer;
import lt.vu.entities.EOrder;
import lt.vu.entities.Product;
import lt.vu.persistence.CustomersDAO;
import lt.vu.persistence.EOrdersDAO;
import lt.vu.persistence.ProductsDAO;
import lt.vu.rest.contracts.EOrderDto;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;
import java.util.Date;
import java.util.List;

@RequestScoped
public class EOrderService {

    @Inject
    private EOrdersDAO eOrdersDAO;

    @Inject
    private CustomersDAO customersDAO;

    @Inject
    private ProductsDAO productsDAO;

    public List<Long> getOrderProductIds(Long orderId) {

        return eOrdersDAO.selectAllOrderProductIds(orderId);
    }

    @Transactional
    public void createOrder(EOrderDto eOrderData) {

        Customer customer = customersDAO.findOne(eOrderData.getCustomerId());
        if (customer == null) {
            throw new NotFoundException("Customer not found");
        }

        List<Product> products = productsDAO.findAll(eOrderData.getProductIds());
        if (products.isEmpty()) {
            throw new NotFoundException("Products not found");
        }

        EOrder newOrder = new EOrder();
        newOrder.setCustomer(customer);
        newOrder.setProducts(products);
        newOrder.setDate(new Date());

        eOrdersDAO.persist(newOrder);
    }

    @Transactional
    public void addProductToOrder(Long orderId, Long productId) {

        EOrder existingOrder = eOrdersDAO.findOne(orderId);
        if (existingOrder == null) {
            throw new NotFoundException("Order with id " + orderId + " not found.");
        }

        Product existingProduct = productsDAO.findOne(productId);
        if (existingProduct == null) {
            throw new NotFoundException("Product with id " + productId + " not found.");
        }

        eOrdersDAO.addProductToOrder(orderId, existingProduct);
    }
}

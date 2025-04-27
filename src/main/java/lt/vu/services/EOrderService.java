package lt.vu.services;

import lombok.Getter;
import lombok.Setter;
import lt.vu.mybatis.model.Customer;
import lt.vu.mybatis.model.Eorder;
import lt.vu.mybatis.model.EorderProduct;
import lt.vu.mybatis.model.Product;
import lt.vu.persistence.MyBatisCustomersDAO;
import lt.vu.persistence.MyBatisEOrdersDAO;
import lt.vu.persistence.MyBatisProductsDAO;
import lt.vu.rest.contracts.EOrderDto;
import lt.vu.rest.contracts.ProductDto;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class EOrderService {

    @Inject
    @Setter @Getter
    private MyBatisEOrdersDAO myBatisEOrdersDAO;

    @Inject
    @Setter @Getter
    private MyBatisCustomersDAO myBatisCustomersDAO;

    @Inject
    @Setter @Getter
    private MyBatisProductsDAO myBatisProductsDAO;

    @Transactional
    public void createOrder(EOrderDto eOrderData) {

        Customer customer = myBatisCustomersDAO.findOne(eOrderData.getCustomerId());
        if (customer == null) {
            throw new NotFoundException("Customer not found");
        }

        List<Long> existingProductIds = myBatisProductsDAO.findAll()
                .stream()
                .map(Product::getId)
                .collect(Collectors.toList());

        List<Long> requestedProductIds = eOrderData.getProductIds();

        // Ensure that all requested products exist
        if (!existingProductIds.containsAll(requestedProductIds)) {
            throw new NotFoundException("One or more product ids are invalid.");
        }

        Eorder newOrder = new Eorder();
        newOrder.setCustomerId(customer.getId());
        newOrder.setDate(new Date());
        myBatisEOrdersDAO.persist(newOrder);

        for (Long productId : requestedProductIds) {
            EorderProduct eorderProduct = new EorderProduct();
            eorderProduct.setOrdersId(newOrder.getId());
            eorderProduct.setProductsId(productId);

            myBatisEOrdersDAO.insertOrderProduct(eorderProduct);
        }
    }

    @Transactional
    public void addProductToOrder(Long orderId, ProductDto product) {

        Eorder existingOrder = myBatisEOrdersDAO.findOne(orderId);
        if (existingOrder == null) {
            throw new NotFoundException("Order with id " + orderId + " not found.");
        }

        Long productId = product.getId();
        Product existingProduct = myBatisProductsDAO.findOne(productId);
        if (existingProduct == null) {
            throw new NotFoundException("Product with id " + productId + " not found.");
        }

        EorderProduct eorderProduct = new EorderProduct();
        eorderProduct.setOrdersId(orderId);
        eorderProduct.setProductsId(productId);

        myBatisEOrdersDAO.insertOrderProduct(eorderProduct);
    }
}

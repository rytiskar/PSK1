package lt.vu.services;

import lt.vu.mybatis.model.Customer;
import lt.vu.mybatis.model.CustomerWithOrdersAndProducts;
import lt.vu.persistence.MyBatisCustomersDAO;
import lt.vu.rest.contracts.CustomerDto;
import lt.vu.rest.contracts.CustomerWithOrdersAndProductsDto;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequestScoped
public class MyBatisCustomerService {

    @Inject
    private MyBatisCustomersDAO myBatisCustomersDAO;

    public List<CustomerWithOrdersAndProductsDto> getAllCustomersWithTheirOrdersAndProducts() {

        List<CustomerWithOrdersAndProducts> customers = myBatisCustomersDAO.getCustomersWithOrdersAndProducts();

        return customers.stream()
                .flatMap(customer -> {
                    List<CustomerWithOrdersAndProducts.OrderWithProducts> orders = customer.getOrders();

                    // Customer has no orders
                    if (orders == null || orders.isEmpty()) {
                        return Stream.of(buildCustomerDto(customer, null, null));
                    }

                    return orders.stream().flatMap(order -> {
                        List<CustomerWithOrdersAndProducts.ProductInfo> products = order.getProducts();

                        // Order has no products
                        if (products == null || products.isEmpty()) {
                            return Stream.of(buildCustomerDto(customer, order, null));
                        }

                        // Normal case
                        return products.stream()
                                .map(product -> buildCustomerDto(customer, order, product));
                    });
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void create(CustomerDto customerData) {
        Customer newCustomer = new Customer();

        newCustomer.setFirstname(customerData.getFirstName());
        newCustomer.setLastname(customerData.getLastName());
        newCustomer.setEmail(customerData.getEmail());

        myBatisCustomersDAO.persist(newCustomer);
    }

    private CustomerWithOrdersAndProductsDto buildCustomerDto(
            CustomerWithOrdersAndProducts customer,
            CustomerWithOrdersAndProducts.OrderWithProducts order,
            CustomerWithOrdersAndProducts.ProductInfo product
    ) {
        CustomerWithOrdersAndProductsDto dto = new CustomerWithOrdersAndProductsDto();
        dto.setCustomerId(customer.getCustomerId());
        dto.setFirstName(customer.getCustomerFirstName());
        dto.setLastName(customer.getCustomerLastName());
        dto.setEmail(customer.getCustomerEmail());

        if (order != null) {
            dto.setOrderId(order.getOrderId().toString());
            dto.setOrderDate(order.getOrderDate() != null ? order.getOrderDate().toString() : null);
        }

        if (product != null) {
            dto.setProductId(product.getProductId().toString());
            dto.setProductName(product.getProductName());
            dto.setProductPrice(Double.toString(product.getPrice()));
        }

        return dto;
    }
}

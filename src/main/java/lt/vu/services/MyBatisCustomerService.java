package lt.vu.services;

import lt.vu.mybatis.model.Customer;
import lt.vu.mybatis.model.CustomerWithOrdersAndProducts;
import lt.vu.persistence.MyBatisCustomersDAO;
import lt.vu.rest.contracts.CustomerDto;
import lt.vu.rest.contracts.CustomerWithOrdersAndProductsDto;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequestScoped
public class MyBatisCustomerService {

    @Inject
    private MyBatisCustomersDAO myBatisCustomersDAO;

    public List<CustomerWithOrdersAndProductsDto> getAllCustomersWithTheirOrdersAndProducts() {
        List<CustomerWithOrdersAndProducts> customers = myBatisCustomersDAO.getCustomersWithOrdersAndProducts();

        if (customers == null || customers.isEmpty()) {
            return new ArrayList<>();
        }

        return customers.stream().map(customer -> {
            CustomerWithOrdersAndProductsDto dto = new CustomerWithOrdersAndProductsDto();
            dto.setCustomerId(customer.getCustomerId());
            dto.setFirstName(customer.getCustomerFirstName());
            dto.setLastName(customer.getCustomerLastName());
            dto.setEmail(customer.getCustomerEmail());

            List<CustomerWithOrdersAndProducts.OrderWithProducts> orders = customer.getOrders();
            if (orders != null) {
                List<CustomerWithOrdersAndProductsDto.Order> orderDtos = orders.stream().map(order -> {
                    CustomerWithOrdersAndProductsDto.Order orderDto = new CustomerWithOrdersAndProductsDto.Order();
                    orderDto.setOrderId(order.getOrderId() != null ? order.getOrderId().toString() : null);
                    orderDto.setOrderDate(order.getOrderDate() != null ? order.getOrderDate().toString() : null);

                    List<CustomerWithOrdersAndProducts.ProductInfo> products = order.getProducts();
                    if (products != null) {
                        List<CustomerWithOrdersAndProductsDto.Product> productDtos = products.stream().map(product -> {
                            CustomerWithOrdersAndProductsDto.Product productDto = new CustomerWithOrdersAndProductsDto.Product();
                            productDto.setProductId(product.getProductId() != null ? product.getProductId().toString() : null);
                            productDto.setProductName(product.getProductName());
                            productDto.setProductPrice(Double.toString(product.getPrice()));
                            return productDto;
                        }).collect(Collectors.toList());

                        orderDto.setProducts(productDtos);
                    }

                    return orderDto;
                }).collect(Collectors.toList());

                dto.setOrders(orderDtos);
            }

            return dto;
        }).collect(Collectors.toList());
    }

    @Transactional
    public void create(CustomerDto customerData) {
        Customer newCustomer = new Customer();

        newCustomer.setFirstname(customerData.getFirstName());
        newCustomer.setLastname(customerData.getLastName());
        newCustomer.setEmail(customerData.getEmail());

        myBatisCustomersDAO.persist(newCustomer);
    }
}

package lt.vu.services;

import lt.vu.mybatis.model.Customer;
import lt.vu.mybatis.model.CustomerWithOrdersAndProducts;
import lt.vu.persistence.MyBatisCustomersDAO;
import lt.vu.rest.contracts.CustomerDto;
import lt.vu.rest.contracts.CustomerWithOrdersAndProductsDto;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationScoped
public class CustomerService {

    @Inject
    private MyBatisCustomersDAO myBatisCustomersDAO;

    public List<CustomerWithOrdersAndProductsDto> getAllCustomersWithTheirOrdersAndProducts()
    {
        List<CustomerWithOrdersAndProducts> customers = myBatisCustomersDAO.getCustomersWithOrdersAndProducts();

        if (customers == null || customers.isEmpty()) {
            return new ArrayList<>();
        }

        return customers.stream()
                .flatMap(customer -> {

                    List<CustomerWithOrdersAndProducts.OrderWithProducts> orders = customer.getOrders();

                    // Customer has no orders
                    if (orders == null || orders.isEmpty()) {
                        CustomerWithOrdersAndProductsDto dto = new CustomerWithOrdersAndProductsDto();
                        dto.setCustomerId(customer.getCustomerId());
                        dto.setFirstName(customer.getCustomerFirstName());
                        dto.setLastName(customer.getCustomerLastName());
                        dto.setEmail(customer.getCustomerEmail());
                        return Stream.of(dto);
                    }

                    return orders.stream().flatMap(order -> {

                        List<CustomerWithOrdersAndProducts.ProductInfo> products = order.getProducts();

                        // Order has no products
                        if (products == null || products.isEmpty()) {
                            CustomerWithOrdersAndProductsDto dto = new CustomerWithOrdersAndProductsDto();
                            dto.setCustomerId(customer.getCustomerId());
                            dto.setFirstName(customer.getCustomerFirstName());
                            dto.setLastName(customer.getCustomerLastName());
                            dto.setEmail(customer.getCustomerEmail());
                            dto.setOrderId(order.getOrderId().toString());
                            dto.setOrderDate(order.getOrderDate() != null ? order.getOrderDate().toString() : null);
                            return Stream.of(dto);
                        }

                        // Normal case: order has products
                        return products.stream().map(product -> {
                            CustomerWithOrdersAndProductsDto dto = new CustomerWithOrdersAndProductsDto();
                            dto.setCustomerId(customer.getCustomerId());
                            dto.setFirstName(customer.getCustomerFirstName());
                            dto.setLastName(customer.getCustomerLastName());
                            dto.setEmail(customer.getCustomerEmail());
                            dto.setOrderId(order.getOrderId().toString());
                            dto.setOrderDate(order.getOrderDate() != null ? order.getOrderDate().toString() : null);
                            dto.setProductId(product.getProductId().toString());
                            dto.setProductName(product.getProductName());
                            dto.setProductPrice(Double.toString(product.getPrice()));
                            return dto;
                        });
                    });
                })
                .collect(Collectors.toList());
    }

    public void Create(CustomerDto customerData)
    {
        Customer newCustomer = new Customer();

        newCustomer.setFirstname(customerData.getFirstName());
        newCustomer.setLastname(customerData.getLastName());
        newCustomer.setEmail(customerData.getEmail());

        myBatisCustomersDAO.persist(newCustomer);

    }
}

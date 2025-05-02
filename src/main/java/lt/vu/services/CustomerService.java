package lt.vu.services;

import lt.vu.entities.Customer;
import lt.vu.entities.EOrder;
import lt.vu.persistence.CustomersDAO;
import lt.vu.persistence.EOrdersDAO;
import lt.vu.rest.contracts.CustomerDto;
import lt.vu.rest.contracts.CustomerWithOrdersAndProductsDto;
import lt.vu.rest.contracts.EOrderDto;
import lt.vu.rest.contracts.ProductDto;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequestScoped
public class CustomerService {

    @Inject
    private CustomersDAO customersDAO;

    @Inject
    private EOrdersDAO eOrdersDAO;

    @Inject
    private EOrderService orderService;

    @Inject
    private ProductService productService;

    public List<CustomerWithOrdersAndProductsDto> getAllCustomersWithTheirOrdersAndProducts() {
        List<Customer> customers = customersDAO.findAll();

        if (customers == null || customers.isEmpty()) {
            return new ArrayList<>();
        }

        return customers.stream().map(customer -> {
            CustomerWithOrdersAndProductsDto dto = new CustomerWithOrdersAndProductsDto();
            dto.setCustomerId(customer.getId());
            dto.setFirstName(customer.getFirstName());
            dto.setLastName(customer.getLastName());
            dto.setEmail(customer.getEmail());

            if (customer.getOrders() != null) {
                List<CustomerWithOrdersAndProductsDto.Order> orderDtos = customer.getOrders().stream().map(order -> {
                    CustomerWithOrdersAndProductsDto.Order orderDto = new CustomerWithOrdersAndProductsDto.Order();
                    orderDto.setOrderId(String.valueOf(order.getId()));
                    orderDto.setOrderDate(order.getDate().toString());
                    if (order.getProducts() != null) {
                        List<CustomerWithOrdersAndProductsDto.Product> productDtos = order.getProducts().stream().map(product -> {
                            CustomerWithOrdersAndProductsDto.Product productDto = new CustomerWithOrdersAndProductsDto.Product();
                            productDto.setProductId(String.valueOf(product.getId()));
                            productDto.setProductName(product.getProductName());
                            productDto.setProductPrice(String.valueOf(product.getPrice()));
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
    public void createCustomer(CustomerDto customerData) {

        Customer newCustomer = new Customer();

        newCustomer.setFirstName(customerData.getFirstName());
        newCustomer.setLastName(customerData.getLastName());
        newCustomer.setEmail(customerData.getEmail());

        customersDAO.persist(newCustomer);
    }
}

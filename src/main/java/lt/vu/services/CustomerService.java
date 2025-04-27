package lt.vu.services;

import lt.vu.entities.Customer;
import lt.vu.entities.EOrder;
import lt.vu.persistence.CustomersDAO;
import lt.vu.persistence.EOrdersDAO;
import lt.vu.rest.contracts.CustomerDto;
import lt.vu.rest.contracts.CustomerWithOrdersAndProductsDto;
import lt.vu.rest.contracts.EOrderDto;
import lt.vu.rest.contracts.ProductDto;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class CustomerService {

    @Inject
    private CustomersDAO customersDAO;

    @Inject
    private EOrdersDAO eOrdersDAO;

    @Inject
    private EOrderService orderService;

    @Inject
    private ProductService productService;


    public CustomerDto getCustomerById(Long id) {
        Customer customer = customersDAO.findOne(id);
        if (customer == null) {
            throw new NotFoundException("Customer not found.");
        }

        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(customer.getId());
        customerDto.setFirstName(customer.getFirstName());
        customerDto.setLastName(customer.getLastName());
        customerDto.setEmail(customer.getEmail());

        return customerDto;
    }

    public List<CustomerDto> getAllCustomers() {
        List<Customer> customers = customersDAO.findAll();

        if (customers == null || customers.isEmpty()) {
            throw new NotFoundException("No customers found.");
        }

        return customers.stream()
                .map(customer -> {
                    CustomerDto customerDto = new CustomerDto();
                    customerDto.setId(customer.getId());
                    customerDto.setFirstName(customer.getFirstName());
                    customerDto.setLastName(customer.getLastName());
                    customerDto.setEmail(customer.getEmail());
                    return customerDto;
                })
                .collect(Collectors.toList());
    }

    public List<EOrderDto> getCustomerOrders(Long customerId) {
        List<EOrder> orders = eOrdersDAO.findAll();

        if (orders == null || orders.isEmpty()) {
            throw new NotFoundException("No orders found.");
        }
        List<EOrderDto> orderDtos = orders.stream()
                .filter(order -> order.getCustomer().getId().equals(customerId))
                .map(order -> {
                    EOrderDto orderDto = new EOrderDto();
                    orderDto.setId(order.getId());
                    orderDto.setCustomerId(order.getCustomer().getId());
                    orderDto.setDate(order.getDate());

                    List<Long> productIds = eOrdersDAO.selectAllOrderProductIds(order.getId());
                    orderDto.setProductIds(productIds);

                    return orderDto;
                })
                .collect(Collectors.toList());

        if (orderDtos.isEmpty()) {
            throw new NotFoundException("No orders found for this customer.");
        }

        return orderDtos;
    }

    public void createCustomer(CustomerDto customerData) {

        Customer newCustomer = new Customer();

        newCustomer.setFirstName(customerData.getFirstName());
        newCustomer.setLastName(customerData.getLastName());
        newCustomer.setEmail(customerData.getEmail());

        customersDAO.persist(newCustomer);
    }

    public List<CustomerWithOrdersAndProductsDto> getAllCustomersWithTheirOrdersAndProducts() {

        List<CustomerDto> customers = getAllCustomers();

        List<CustomerWithOrdersAndProductsDto> customerWithOrdersAndProductsDtos = new ArrayList<>();

        for (CustomerDto customer : customers) {

            List<EOrderDto> orders = getCustomerOrders(customer.getId());

            for (EOrderDto order : orders) {

                List<Long> orderProductIds = orderService.getOrderProductIds(order.getId());

                for (Long orderProductId : orderProductIds) {

                    ProductDto product = productService.getProductById(orderProductId);

                    if (product != null) {
                        CustomerWithOrdersAndProductsDto dto = new CustomerWithOrdersAndProductsDto();
                        dto.setCustomerId(customer.getId());
                        dto.setFirstName(customer.getFirstName());
                        dto.setLastName(customer.getLastName());
                        dto.setEmail(customer.getEmail());
                        dto.setOrderId(order.getId().toString());
                        dto.setOrderDate(order.getDate() != null ? order.getDate().toString() : null);
                        dto.setProductId(product.getId().toString());
                        dto.setProductName(product.getName());
                        dto.setProductPrice((String.valueOf(product.getPrice())));

                        customerWithOrdersAndProductsDtos.add(dto);
                    }
                }
            }
        }

        if (customerWithOrdersAndProductsDtos.isEmpty()) {
            throw new NotFoundException("No orders or products found for customers.");
        }

        // Return the list of DTOs
        return customerWithOrdersAndProductsDtos;
    }

}

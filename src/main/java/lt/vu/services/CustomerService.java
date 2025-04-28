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

    private List<CustomerDto> getAllCustomers() {
        List<Customer> customers = customersDAO.findAll();

        if (customers == null || customers.isEmpty()) {
            return new ArrayList<>();
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

    private List<EOrderDto> getCustomerOrders(Long customerId) {
        List<EOrder> orders = eOrdersDAO.findAll();

        if (orders == null || orders.isEmpty()) {
            return new ArrayList<>();
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
            return new ArrayList<>();
        }

        return orderDtos;
    }

    public List<CustomerWithOrdersAndProductsDto> getAllCustomersWithTheirOrdersAndProducts() {

        List<CustomerDto> customers = getAllCustomers();

        List<CustomerWithOrdersAndProductsDto> customersWithOrdersAndProducts = new ArrayList<>();

        for (CustomerDto customer : customers) {
            List<EOrderDto> orders = getCustomerOrders(customer.getId());

            if(orders.isEmpty()) {
                CustomerWithOrdersAndProductsDto dto = createCustomerWithoutOrdersDto(customer);

                customersWithOrdersAndProducts.add(dto);

                continue;
            }

            for (EOrderDto order : orders) {
                List<Long> orderProductIds = orderService.getOrderProductIds(order.getId());

                List<ProductDto> orderProducts = productService.getOrderProducts(orderProductIds);

                for (ProductDto orderProduct : orderProducts) {

                    if (orderProduct != null) {
                        CustomerWithOrdersAndProductsDto dto = new CustomerWithOrdersAndProductsDto();
                        dto.setCustomerId(customer.getId());
                        dto.setFirstName(customer.getFirstName());
                        dto.setLastName(customer.getLastName());
                        dto.setEmail(customer.getEmail());
                        dto.setOrderId(order.getId().toString());

                        if (order.getDate() != null) {
                            SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
                            dto.setOrderDate(formatter.format(order.getDate()));
                        } else {
                            dto.setOrderDate(null);
                        }

                        dto.setProductId(orderProduct.getId().toString());
                        dto.setProductName(orderProduct.getName());
                        dto.setProductPrice((String.valueOf(orderProduct.getPrice())));

                        customersWithOrdersAndProducts.add(dto);
                    }
                }
            }
        }

        return customersWithOrdersAndProducts;
    }

    @Transactional
    public void createCustomer(CustomerDto customerData) {

        Customer newCustomer = new Customer();

        newCustomer.setFirstName(customerData.getFirstName());
        newCustomer.setLastName(customerData.getLastName());
        newCustomer.setEmail(customerData.getEmail());

        customersDAO.persist(newCustomer);
    }

    private CustomerWithOrdersAndProductsDto createCustomerWithoutOrdersDto(CustomerDto customer) {
        CustomerWithOrdersAndProductsDto dto = new CustomerWithOrdersAndProductsDto();
        dto.setCustomerId(customer.getId());
        dto.setFirstName(customer.getFirstName());
        dto.setLastName(customer.getLastName());
        dto.setEmail(customer.getEmail());
        dto.setOrderId(null);
        dto.setOrderDate(null);
        dto.setProductId(null);
        dto.setProductName(null);
        dto.setProductPrice(null);

        return dto;
    }
}

package lt.vu.services;

import lt.vu.mybatis.model.CustomerWithOrdersAndProducts;
import lt.vu.persistence.MyBatisCustomersDAO;
import lt.vu.rest.contracts.CustomerWithOrdersAndProductsDto;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
                .flatMap(customer -> customer.getOrders().stream()
                        .flatMap(order -> order.getProducts().stream()
                                .map(product -> {

                                    CustomerWithOrdersAndProductsDto dto = new CustomerWithOrdersAndProductsDto();

                                    dto.setCustomerId(customer.getCustomerId());
                                    dto.setFirstName(customer.getCustomerFirstName());
                                    dto.setLastName(customer.getCustomerLastName());
                                    dto.setEmail(customer.getCustomerEmail());

                                    dto.setOrderId(order.getOrderId() != null ? order.getOrderId().toString() : null);
                                    dto.setOrderDate(order.getOrderDate() != null ? order.getOrderDate().toString() : null);

                                    dto.setProductId(product.getProductId() != null ? product.getProductId().toString() : null);
                                    dto.setProductName(product.getProductName());
                                    dto.setProductPrice(Double.toString(product.getPrice()));

                                    return dto;
                                })
                        )
                )
                .collect(Collectors.toList());
    }

}

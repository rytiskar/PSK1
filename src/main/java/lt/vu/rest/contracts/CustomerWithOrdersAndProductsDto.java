package lt.vu.rest.contracts;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CustomerWithOrdersAndProductsDto {
    public long customerId;
    public String firstName;
    public String lastName;
    public String email;
    List<Order> orders;

    @Getter
    @Setter
    public static class Order {
        public String orderId;
        public String orderDate;
        List<Product> products;
    }

    @Getter
    @Setter
    public static class Product {
        public String productId;
        public String productName;
        public String productPrice;
    }
}

package lt.vu.mybatis.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter @Setter
public class CustomerWithOrdersAndProducts {

    private Long customerId;
    private String customerFirstName;
    private String customerLastName;
    private String customerEmail;
    private List<OrderWithProducts> orders;

    @Getter @Setter
    public static class OrderWithProducts {
        private Long orderId;
        private Date orderDate;
        private List<ProductInfo> products;
    }

    @Getter @Setter
    public static class ProductInfo {
        private Long productId;
        private String productName;
        private double price;
    }

}

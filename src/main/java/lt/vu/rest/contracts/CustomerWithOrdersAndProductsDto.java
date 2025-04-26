package lt.vu.rest.contracts;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerWithOrdersAndProductsDto {

    public long customerId;

    public String firstName;

    public String lastName;

    public String email;

    public String orderId;

    public String orderDate;

    public String productId;

    public String productName;

    public String productPrice;
}

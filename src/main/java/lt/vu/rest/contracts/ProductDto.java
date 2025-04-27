package lt.vu.rest.contracts;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDto {

    public Long id;

    public String name;

    public double price;
}

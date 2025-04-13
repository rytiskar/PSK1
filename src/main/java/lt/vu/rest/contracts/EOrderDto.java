package lt.vu.rest.contracts;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class EOrderDto {
    private Long id;

    private Long customerId;

    private List<Long> productIds;

    private Date date;
}

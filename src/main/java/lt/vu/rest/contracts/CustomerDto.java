package lt.vu.rest.contracts;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDto {

    public long id;

    public String firstName;

    public String lastName;

    public String email;
}

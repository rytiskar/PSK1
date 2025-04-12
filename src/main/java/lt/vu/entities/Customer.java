package lt.vu.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Table(name = "CUSTOMER")
@Entity
@Getter @Setter
public class Customer {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "FirstName")
    @Basic(optional = false)
    private String firstName;

    @Column(name = "LastName")
    @Basic(optional = false)
    private String lastName;

    @Column(name = "Email")
    @Basic(optional = false)
    private String email;

    @OneToMany(mappedBy = "customer")
    private List<EOrder> orders;
}

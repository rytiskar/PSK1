package lt.vu.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Table(name = "EORDER")
@Entity
@Getter @Setter
public class EOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Date")
    @Basic(optional = false)
    private Date date;

    @ManyToOne(optional = false)
    private Customer customer;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Product> products;
}

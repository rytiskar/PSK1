package lt.vu.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Table(name = "PRODUCT")
@Entity
@Getter @Setter
public class Product {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "ProductName")
    @Basic(optional = false)
    private String productName;

    @Column(name = "Price")
    @Basic(optional = false)
    private double price;

    @ManyToMany(mappedBy = "products")
    private List<EOrder> orders;
}

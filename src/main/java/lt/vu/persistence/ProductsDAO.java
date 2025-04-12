package lt.vu.persistence;

import lt.vu.entities.Product;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@ApplicationScoped
public class ProductsDAO {
    @Inject
    private EntityManager em;

    public void persist(Product product){
        this.em.persist(product);
    }

    public Product findOne(Long id){
        return em.find(Product.class, id);
    }

    public Product update(Product product){
        return em.merge(product);
    }
}

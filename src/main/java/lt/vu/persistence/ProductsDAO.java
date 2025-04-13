package lt.vu.persistence;

import lt.vu.entities.Product;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

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

    public List<Product> findAll() {
        return em.createQuery("SELECT p FROM Product p", Product.class).getResultList();
    }

    public List<Product> findAll(List<Long> ids){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Product> query = cb.createQuery(Product.class);
        Root<Product> productRoot = query.from(Product.class);

        // Create the 'IN' condition dynamically
        Predicate inPredicate = productRoot.get("id").in(ids);

        // Apply the condition to the query
        query.select(productRoot).where(inPredicate);

        // Execute the query and get the results
        return em.createQuery(query).getResultList();
    }
}

package lt.vu.persistence;

import lt.vu.entities.Product;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@RequestScoped
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

        Predicate inPredicate = productRoot.get("id").in(ids);

        query.select(productRoot).where(inPredicate);

        return em.createQuery(query).getResultList();
    }

    public Product merge(Product product){
        try {
            return em.merge(product);
        } catch (OptimisticLockException e) {
            System.err.println("Optimistic locking failed for product with id: " + product.getId());
            throw e;
        }
    }

    public void remove(Product product){
        em.remove(product);
    }
}

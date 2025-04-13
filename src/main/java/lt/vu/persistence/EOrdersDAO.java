package lt.vu.persistence;

import lt.vu.entities.EOrder;
import lt.vu.entities.Product;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

@ApplicationScoped
public class EOrdersDAO {
    @Inject
    private EntityManager em;

    public void persist(EOrder order){
        this.em.persist(order);
    }

    public EOrder findOne(Long id){
        return em.find(EOrder.class, id);
    }

    public List<EOrder> findAll() {
        return em.createQuery("SELECT o FROM EOrder o", EOrder.class)
                .getResultList();
    }

    public List<Long> selectAllOrderProductIds(Long orderId) {
        return em.createQuery(
                        "SELECT p.id FROM EOrder o " +
                                "JOIN o.products p " +
                                "WHERE o.id = :orderId", Long.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }

    public void addProductToOrder(Long orderId, Product product) {
        EOrder order = em.find(EOrder.class, orderId);
        if (order != null) {
            order.getProducts().add(product);
            em.merge(order);
        }
    }
}

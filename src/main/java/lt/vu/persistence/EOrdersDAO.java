package lt.vu.persistence;

import lt.vu.entities.EOrder;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

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

    public EOrder update(EOrder order){
        return em.merge(order);
    }

}

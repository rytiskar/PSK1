package lt.vu.persistence;

import lt.vu.entities.Customer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@ApplicationScoped
public class CustomersDAO {
    @Inject
    private EntityManager em;

    public void persist(Customer customer){
        this.em.persist(customer);
    }

    public Customer findOne(Long id){
        return em.find(Customer.class, id);
    }

    public Customer update(Customer customer){
        return em.merge(customer);
    }
}

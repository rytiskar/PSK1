package lt.vu.persistence;

import lt.vu.entities.Customer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

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

    public List<Customer> findAll() {
        return em.createQuery("SELECT c FROM Customer c", Customer.class)
                .getResultList();
    }
}

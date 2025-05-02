package lt.vu.persistence;

import lt.vu.entities.Customer;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

@RequestScoped
public class CustomersDAO {
    @Inject
    private EntityManager em;

    @Transactional(Transactional.TxType.MANDATORY)
    public void persist(Customer customer){
        this.em.persist(customer);
    }

    public Customer findOne(Long id){
        return em.find(Customer.class, id);
    }

    public List<Customer> findAll() {
        return em.createQuery("SELECT DISTINCT c FROM Customer c", Customer.class)
                .getResultList();
    }
}

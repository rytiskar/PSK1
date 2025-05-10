package lt.vu.persistence;

import lt.vu.entities.Customer;
import lt.vu.interfaces.ICustomersDAO;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

@RequestScoped
public class CustomersDAO implements ICustomersDAO {
    @Inject
    private EntityManager em;

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

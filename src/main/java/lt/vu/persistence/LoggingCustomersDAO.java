package lt.vu.persistence;

import lt.vu.entities.Customer;
import lt.vu.interceptors.LoggedInvocation;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Specializes;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

@RequestScoped
@Specializes
@LoggedInvocation
public class LoggingCustomersDAO extends CustomersDAO {
    @Inject
    private EntityManager em;

    public void persist(Customer customer){
        System.out.println("[LoggingCustomersDAO] persisting Customer with id: " + customer.getId());
        this.em.persist(customer);
    }

    public Customer findOne(Long id){
        System.out.println("[LoggingCustomersDAO] looking for customer with id: " + id);
        return em.find(Customer.class, id);
    }

    public List<Customer> findAll() {
        System.out.println("[LoggingCustomersDAO] retrieving all customers");
        return em.createQuery("SELECT DISTINCT c FROM Customer c", Customer.class)
                .getResultList();
    }
}

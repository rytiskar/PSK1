package lt.vu.persistence;

import lt.vu.entities.Customer;
import lt.vu.interfaces.ICustomersDAO;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.inject.Inject;
import java.util.List;

@Decorator
public class CustomersDecoratorDAO implements ICustomersDAO {

    @Inject
    @Delegate
    private ICustomersDAO delegate;

    @Override
    public void persist(Customer customer){
        System.out.println("[Decorator] persisting customer with id: " + customer.getId());
        String currentName = customer.getFirstName();
        currentName += " DECORATED";

        customer.setFirstName(currentName);

        delegate.persist(customer);
    }

    @Override
    public Customer findOne(Long id){
        System.out.println("[Decorator] getting customer with id: " + id);
        return delegate.findOne(id);
    }

    @Override
    public List<Customer> findAll() {
        System.out.println("[Decorator] retrieving all customers");
        return delegate.findAll();
    }
}

package lt.vu.interfaces;

import lt.vu.entities.Customer;

import java.util.List;

public interface ICustomersDAO {
    void persist(Customer customer);
    Customer findOne(Long id);
    List<Customer> findAll();
}

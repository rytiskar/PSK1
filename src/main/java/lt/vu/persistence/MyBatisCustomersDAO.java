package lt.vu.persistence;

import lt.vu.mybatis.dao.CustomerMapper;
import lt.vu.mybatis.model.Customer;
import org.apache.ibatis.session.SqlSession;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class MyBatisCustomersDAO {

    @Inject
    private SqlSession sqlSession;

    public void persist(Customer customer) {
        CustomerMapper mapper = sqlSession.getMapper(CustomerMapper.class);
        mapper.insert(customer);
    }

    public Customer findOne(Long id) {
        CustomerMapper mapper = sqlSession.getMapper(CustomerMapper.class);
        return mapper.selectByPrimaryKey(id);
    }

    public Customer update(Customer customer) {
        CustomerMapper mapper = sqlSession.getMapper(CustomerMapper.class);
        mapper.updateByPrimaryKey(customer);
        return customer;
    }

}

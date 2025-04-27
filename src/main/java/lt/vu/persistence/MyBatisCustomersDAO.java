package lt.vu.persistence;

import lt.vu.mybatis.dao.CustomerMapper;
import lt.vu.mybatis.model.Customer;
import lt.vu.mybatis.model.CustomerWithOrdersAndProducts;
import org.apache.ibatis.session.SqlSession;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

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

    public List<CustomerWithOrdersAndProducts> getCustomersWithOrdersAndProducts() {
        CustomerMapper mapper = sqlSession.getMapper(CustomerMapper.class);
        return mapper.selectCustomersWithOrdersAndProducts();
    }
}

package lt.vu.persistence;

import lt.vu.mybatis.dao.ProductMapper;
import lt.vu.mybatis.model.Product;
import org.apache.ibatis.session.SqlSession;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.List;

@RequestScoped
public class MyBatisProductsDAO {

    @Inject
    private SqlSession sqlSession;

    public void persist(Product product) {
        ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
        mapper.insert(product);
    }

    public Product findOne(Long id) {
        ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
        return mapper.selectByPrimaryKey(id);
    }

    public List<Product> findAll() {
        ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
        return mapper.selectAll();
    }
}

package lt.vu.persistence;

import lt.vu.entities.EOrder;
import lt.vu.mybatis.dao.EorderMapper;
import lt.vu.mybatis.dao.EorderProductMapper;
import lt.vu.mybatis.dao.ProductMapper;
import lt.vu.mybatis.model.Eorder;
import lt.vu.mybatis.model.EorderProduct;
import lt.vu.mybatis.model.Product;
import org.apache.ibatis.session.SqlSession;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class MyBatisEOrdersDAO {

    @Inject
    private SqlSession sqlSession;

    public void persist(Eorder eorder) {
        EorderMapper mapper = sqlSession.getMapper(EorderMapper.class);
        mapper.insert(eorder);
    }

    public Eorder findOne(Long id) {
        EorderMapper mapper = sqlSession.getMapper(EorderMapper.class);
        return mapper.selectByPrimaryKey(id);
    }

    public List<Eorder> findAll() {
        EorderMapper mapper = sqlSession.getMapper(EorderMapper.class);
        return mapper.selectAll();
    }

    public List<EorderProduct> SelectAllOrderProducts() {
        EorderProductMapper mapper = sqlSession.getMapper(EorderProductMapper.class);
        return mapper.selectAll();
    }

    public void insertOrderProduct(EorderProduct eorderProduct) {
        EorderProductMapper mapper = sqlSession.getMapper(EorderProductMapper.class);
        mapper.insert(eorderProduct);
    }
}

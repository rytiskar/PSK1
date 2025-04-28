package lt.vu.persistence;

import lt.vu.mybatis.dao.EorderMapper;
import lt.vu.mybatis.dao.EorderProductMapper;
import lt.vu.mybatis.model.Eorder;
import lt.vu.mybatis.model.EorderProduct;
import org.apache.ibatis.session.SqlSession;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.List;

@RequestScoped
public class MyBatisEOrdersDAO {

    @Inject
    private SqlSession sqlSession;

    public List<EorderProduct> SelectAllOrderProducts() {
        EorderProductMapper mapper = sqlSession.getMapper(EorderProductMapper.class);
        return mapper.selectAll();
    }

    public void persist(Eorder eorder) {
        EorderMapper mapper = sqlSession.getMapper(EorderMapper.class);
        mapper.insert(eorder);
    }

    public Eorder findOne(Long id) {
        EorderMapper mapper = sqlSession.getMapper(EorderMapper.class);
        return mapper.selectByPrimaryKey(id);
    }

    public void insertOrderProduct(EorderProduct eorderProduct) {
        EorderProductMapper mapper = sqlSession.getMapper(EorderProductMapper.class);
        mapper.insert(eorderProduct);
    }
}

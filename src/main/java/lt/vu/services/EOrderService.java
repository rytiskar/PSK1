package lt.vu.services;

import lt.vu.persistence.EOrdersDAO;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class EOrderService {

    @Inject
    private EOrdersDAO eOrdersDAO;

    public List<Long> getOrderProductIds(Long orderId) {

        return eOrdersDAO.selectAllOrderProductIds(orderId);
    }


}

package org.mnnu.crowd.service.api;

import com.mnnu.crowd.util.ResultEntity;
import org.mnnu.crowd.entity.vo.AddressVO;
import org.mnnu.crowd.entity.vo.OrderProjectVO;
import org.mnnu.crowd.entity.vo.OrderVO;

import java.util.List;

/**
 * @author qiaoh
 */
public interface OrderService {
    OrderProjectVO getOrderProjectVO(Integer projectId, Integer returnId);

    List<AddressVO> getAddressVOList(Integer memberId);

    void saveAddress(AddressVO addressVO);

    ResultEntity<String> saveOrderPO(OrderVO orderVO);
}

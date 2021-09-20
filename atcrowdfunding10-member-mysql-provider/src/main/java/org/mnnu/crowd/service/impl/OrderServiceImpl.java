package org.mnnu.crowd.service.impl;

import com.mnnu.crowd.util.ResultEntity;
import org.mnnu.crowd.entity.po.AddressPO;
import org.mnnu.crowd.entity.po.AddressPOExample;
import org.mnnu.crowd.entity.po.OrderPO;
import org.mnnu.crowd.entity.po.OrderProjectPO;
import org.mnnu.crowd.entity.vo.AddressVO;
import org.mnnu.crowd.entity.vo.OrderProjectVO;
import org.mnnu.crowd.entity.vo.OrderVO;
import org.mnnu.crowd.mapper.AddressPOMapper;
import org.mnnu.crowd.mapper.OrderPOMapper;
import org.mnnu.crowd.mapper.OrderProjectPOMapper;
import org.mnnu.crowd.service.api.OrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qiaoh
 */
@Service
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderPOMapper orderPOMapper;

    @Autowired
    private OrderProjectPOMapper orderProjectPOMapper;

    @Autowired
    private AddressPOMapper addressPOMapper;


    @Override
    public OrderProjectVO getOrderProjectVO(Integer projectId, Integer returnId) {
        return orderProjectPOMapper.getOrderProjectVO(returnId);
    }

    @Override
    public List<AddressVO> getAddressVOList(Integer memberId) {
        AddressPOExample addressPOExample = new AddressPOExample();
        AddressPOExample.Criteria criteria = addressPOExample.createCriteria();
        criteria.andMemberIdEqualTo(memberId);

        List<AddressPO> addressPOS = addressPOMapper.selectByExample(addressPOExample);

        List<AddressVO> addressVOList = new ArrayList<>();
        for (AddressPO addressPO : addressPOS) {
            AddressVO addressVO = new AddressVO();
            BeanUtils.copyProperties(addressPO, addressVO);
            addressVOList.add(addressVO);
        }
        return addressVOList;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void saveAddress(AddressVO addressVO) {
        AddressPO addressPO = new AddressPO();
        BeanUtils.copyProperties(addressVO, addressPO);
        addressPOMapper.insert(addressPO);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public ResultEntity<String> saveOrderPO(OrderVO orderVO) {
        OrderPO orderPO = new OrderPO();
        BeanUtils.copyProperties(orderVO, orderPO);
        // 1.将订单信息保存到数据库中
        orderPOMapper.insert(orderPO);
        Integer orderPOId = orderPO.getId();
        // 2.将orderProjectPO保存到数据库中，相当于订单详情
        OrderProjectPO orderProjectPO = new OrderProjectPO();
        BeanUtils.copyProperties(orderVO.getOrderProjectVO(), orderProjectPO);
        // 设置orderId
        orderProjectPO.setOrderId(orderPOId);
        orderProjectPOMapper.insert(orderProjectPO);
        return ResultEntity.successWithoutData();
    }
}

package org.mnnu.crowd.handler;

import com.mnnu.crowd.util.ResultEntity;
import org.mnnu.crowd.entity.vo.AddressVO;
import org.mnnu.crowd.entity.vo.OrderProjectVO;
import org.mnnu.crowd.entity.vo.OrderVO;
import org.mnnu.crowd.service.api.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author qiaoh
 */
@RestController
public class OrderHandler {

    @Autowired
    OrderService orderService;

    @RequestMapping("/get/order/project/VO/remote")
    public ResultEntity<OrderProjectVO> getOrderProjectVORemote(@RequestParam("projectId") Integer projectId, @RequestParam("returnId") Integer returnId) {
        try {
            OrderProjectVO orderProjectVO = orderService.getOrderProjectVO(projectId, returnId);
            return ResultEntity.successWithData(orderProjectVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 通过用户id查询当前用户的收货地址
     *
     * @param memberId
     * @return
     */
    @RequestMapping("/get/address/vo/list")
    ResultEntity<List<AddressVO>> getAddressVOListRemote(@RequestParam("memberId") Integer memberId) {
        try {
            List<AddressVO> addressVOList = orderService.getAddressVOList(memberId);
            return ResultEntity.successWithData(addressVOList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 保存地址
     *
     * @param addressVO
     * @return
     */
    @RequestMapping("/save/address")
    ResultEntity<String> saveAddress(@RequestBody AddressVO addressVO) {
        try {
            orderService.saveAddress(addressVO);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 将付款成功的订单保存到数据库中
     *
     * @param orderVO
     * @return
     */
    @RequestMapping("/save/order/po")
    ResultEntity<String> saveOrderPORemote(@RequestBody OrderVO orderVO) {
        try {
            ResultEntity<String> resultEntity = orderService.saveOrderPO(orderVO);
            return resultEntity;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

}

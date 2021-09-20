package org.mnnu.crowd.handler;

import com.mnnu.crowd.constant.CrowdConstant;
import com.mnnu.crowd.util.ResultEntity;
import org.mnnu.crowd.api.MySQLRemoteService;
import org.mnnu.crowd.entity.vo.AddressVO;
import org.mnnu.crowd.entity.vo.MemberLoginVO;
import org.mnnu.crowd.entity.vo.OrderProjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class OrderHandler {

    @Autowired
    private MySQLRemoteService sqlRemoteService;


    @RequestMapping("/confirm/return/info/{projectId}/{returnId}")
    public String showReturnConfirmInfo(@PathVariable("projectId") Integer projectId,
                                        @PathVariable("returnId") Integer returnId,
                                        HttpSession session) {

        ResultEntity<OrderProjectVO> resultEntity = sqlRemoteService.getOrderProjectVORemote(projectId, returnId);
        if (ResultEntity.SUCCESS.equals(resultEntity.getOperationResult())) {
            // 存入session中，便于后续取出
            session.setAttribute(CrowdConstant.ATTR_NAME_ORDER_PROJECT, resultEntity.getQueryData());
        }

        return "confirm_return";
    }

    @RequestMapping("/confirm/order/info/{returnCount}")
    public String showOrderConfirmInfo(@PathVariable("returnCount") Integer returnCount,
                                       HttpSession session) {
        // 1.合并个数
        OrderProjectVO orderProjectVO = (OrderProjectVO) session.getAttribute(CrowdConstant.ATTR_NAME_ORDER_PROJECT);
        orderProjectVO.setReturnCount(returnCount);
        // session中的值要重新设置
        session.setAttribute(CrowdConstant.ATTR_NAME_ORDER_PROJECT, orderProjectVO);
        // 2.获取登录的用户id
        MemberLoginVO memberLoginVO = (MemberLoginVO) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);
        Integer memberId = memberLoginVO.getId();
        // 3.查询地址信息
        ResultEntity<List<AddressVO>> resultEntity = sqlRemoteService.getAddressVOListRemote(memberId);
        if (ResultEntity.SUCCESS.equals(resultEntity.getOperationResult())) {
            session.setAttribute(CrowdConstant.ATTR_NAME_ADDRESSVO_LIST, resultEntity.getQueryData());
        }
        return "confirm_order";
    }

    @RequestMapping("/save/address")
    public String saveAddress(AddressVO addressVO,
                              @RequestHeader("Referer") String referer) {

        ResultEntity<String> resultEntity = sqlRemoteService.saveAddress(addressVO);
        return "redirect:" + referer;
    }
}

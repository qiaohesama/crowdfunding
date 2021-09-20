package org.mnnu.crowd.api;

import com.mnnu.crowd.util.ResultEntity;
import org.mnnu.crowd.entity.po.MemberPO;
import org.mnnu.crowd.entity.vo.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 暴露provider的接口给远程调用
 *
 * @author qiaoh
 */
@FeignClient("mnnu-crowd-mysql-provider")
public interface MySQLRemoteService {
    /**
     * 远程调用获取方法
     *
     * @param loginAcct
     * @return
     */
    @RequestMapping("/get/memberpo/by/loginacct/remote")
    public ResultEntity<MemberPO> getMemberByAcct(@RequestParam("loginAcct") String loginAcct);

    /**
     * 保存用户注册信息
     *
     * @return
     */
    @RequestMapping("/save/memberpo/remote")
    ResultEntity<String> saveMemberRemote(@RequestBody MemberPO memberPO);


    /**
     * 将项目的信息保存到mysql中
     *
     * @param memberId
     * @param projectVO
     * @return
     */
    @RequestMapping("/save/project/remote")
    ResultEntity<String> saveProjectVORemote(@RequestParam("memberId") Integer memberId, @RequestBody ProjectVO projectVO);


    /**
     * 远程接口暴露给其他微服务调用获取首页分类信息
     * 每个分类下最多有四个项目
     *
     * @return
     */
    @RequestMapping("/get/portal/type/remote")
    ResultEntity<List<PortalTypeVO>> getPortalTypeVOList();


    /**
     * 通过id获取项目详情的接口
     *
     * @param projectId
     * @return
     */
    @RequestMapping("/get/project/detail/remote/{projectId}")
    ResultEntity<DetailProjectVO> getProjectDetailById(@PathVariable("projectId") Integer projectId);


    /**
     * 获取订单中的项目信息，用于界面的显示
     *
     * @param projectId
     * @param returnId
     * @return
     */
    @RequestMapping("/get/order/project/VO/remote")
    ResultEntity<OrderProjectVO> getOrderProjectVORemote(@RequestParam("projectId") Integer projectId, @RequestParam("returnId") Integer returnId);


    /**
     * 通过用户id查询当前用户的收货地址
     *
     * @param memberId
     * @return
     */
    @RequestMapping("/get/address/vo/list")
    ResultEntity<List<AddressVO>> getAddressVOListRemote(@RequestParam("memberId") Integer memberId);

    /**
     * 保存地址
     *
     * @param addressVO
     * @return
     */
    @RequestMapping("/save/address")
    ResultEntity<String> saveAddress(@RequestBody AddressVO addressVO);

    /**
     * 将付款成功的订单保存到数据库中
     *
     * @param orderVO
     * @return
     */
    @RequestMapping("/save/order/po")
    ResultEntity<String> saveOrderPORemote(@RequestBody OrderVO orderVO);
}

package org.mnnu.crowd.handler;

import com.mnnu.crowd.util.ResultEntity;
import org.mnnu.crowd.entity.po.MemberPO;
import org.mnnu.crowd.service.api.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qiaoh
 */
@RestController
public class MemberHandler {

    @Autowired
    MemberService memberService;

    @RequestMapping("/get/memberpo/by/loginacct/remote")
    public ResultEntity<MemberPO> getMemberByAcct(@RequestParam("loginAcct") String loginAcct) {
        try {
            MemberPO memberPO = memberService.getMemberByLoginAcct(loginAcct);
            return ResultEntity.successWithData(memberPO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    @RequestMapping("/save/memberpo/remote")
    public ResultEntity<String> saveMemberRemote(@RequestBody MemberPO memberPO) {
        try {
            memberService.registerMember(memberPO);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof DuplicateKeyException) {
                return ResultEntity.failed("用户名已被使用");
            }
            return ResultEntity.failed(e.getMessage());
        }
    }



}

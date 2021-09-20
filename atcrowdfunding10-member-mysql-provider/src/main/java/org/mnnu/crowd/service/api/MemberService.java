package org.mnnu.crowd.service.api;

import org.mnnu.crowd.entity.po.MemberPO;

/**
 * @author qiaoh
 */
public interface MemberService {
    MemberPO getMemberByLoginAcct(String loginAcct);

    void registerMember(MemberPO memberPO);
}

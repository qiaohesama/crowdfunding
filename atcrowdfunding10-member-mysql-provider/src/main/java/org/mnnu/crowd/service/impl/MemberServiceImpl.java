package org.mnnu.crowd.service.impl;

import org.mnnu.crowd.entity.po.MemberPO;
import org.mnnu.crowd.entity.po.MemberPOExample;
import org.mnnu.crowd.mapper.MemberPOMapper;
import org.mnnu.crowd.service.api.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author qiaoh
 */
@Service
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    @Autowired
    MemberPOMapper memberPOMapper;

    @Override
    public MemberPO getMemberByLoginAcct(String loginAcct) {
        MemberPOExample example = new MemberPOExample();
        MemberPOExample.Criteria criteria = example.createCriteria();
        criteria.andLoginacctEqualTo(loginAcct);
        List<MemberPO> memberPOS = memberPOMapper.selectByExample(example);
        return memberPOS.get(0);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class, readOnly = false)
    @Override
    public void registerMember(MemberPO memberPO) {

            memberPOMapper.insertSelective(memberPO);


    }
}

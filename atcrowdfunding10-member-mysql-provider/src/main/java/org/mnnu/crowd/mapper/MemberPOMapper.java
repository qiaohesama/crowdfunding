package org.mnnu.crowd.mapper;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.mnnu.crowd.entity.po.MemberPO;
import org.mnnu.crowd.entity.po.MemberPOExample;

@Mapper
public interface MemberPOMapper {
    long countByExample(MemberPOExample example);

    int deleteByExample(MemberPOExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(MemberPO record);

    int insertSelective(MemberPO record);

    List<MemberPO> selectByExample(MemberPOExample example);

    MemberPO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") MemberPO record, @Param("example") MemberPOExample example);

    int updateByExample(@Param("record") MemberPO record, @Param("example") MemberPOExample example);

    int updateByPrimaryKeySelective(MemberPO record);

    int updateByPrimaryKey(MemberPO record);
}
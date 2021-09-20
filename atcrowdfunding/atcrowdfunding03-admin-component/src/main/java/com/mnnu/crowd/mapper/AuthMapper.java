package com.mnnu.crowd.mapper;

import com.mnnu.crowd.entity.Auth;
import com.mnnu.crowd.entity.AuthExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AuthMapper {
    long countByExample(AuthExample example);

    int deleteByExample(AuthExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Auth record);

    int insertSelective(Auth record);

    List<Auth> selectByExample(AuthExample example);

    Auth selectByPrimaryKey(Integer id);

    /**
     * 通过管理员ID查询已分配给对应角色的权限
     *
     * @param adminId 管理员ID
     * @return 权限名称数组
     */
    List<String> selectAuthByAdminId(Integer adminId);

    int updateByExampleSelective(@Param("record") Auth record, @Param("example") AuthExample example);

    int updateByExample(@Param("record") Auth record, @Param("example") AuthExample example);

    int updateByPrimaryKeySelective(Auth record);

    int updateByPrimaryKey(Auth record);
}
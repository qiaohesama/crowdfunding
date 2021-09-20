package com.mnnu.crowd.mapper;

import com.mnnu.crowd.entity.Role;
import com.mnnu.crowd.entity.RoleExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMapper {
    long countByExample(RoleExample example);

    int deleteByExample(RoleExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Role record);

    int insertSelective(Role record);

    List<Role> selectByExample(RoleExample example);

    Role selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Role record, @Param("example") RoleExample example);

    int updateByExample(@Param("record") Role record, @Param("example") RoleExample example);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);

    List<Role> selectByKeyword(String keyword);

    List<Role> selectAssigned(Integer adminId);

    /**
     * 通过adminId查询inner_admin_role中已经被分配给该管理员的角色数组
     *
     * @param adminId 管理员的主键
     * @return 已分配的角色数组
     */
    List<Role> selectUnAssigned(Integer adminId);

    List<Integer> selectAssignedAuth(Integer roleId);

    void deleteAuthInfo(Integer roleId);

    void insertAuthInfo(@Param("roleId") Integer roleId, @Param("authIdArr") List<Integer> authIdArr);
}
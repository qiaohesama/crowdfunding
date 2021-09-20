package com.mnnu.crowd.mapper;

import com.mnnu.crowd.entity.Admin;
import com.mnnu.crowd.entity.AdminExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdminMapper {
    long countByExample(AdminExample example);

    int deleteByExample(AdminExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Admin record);

    int insertSelective(Admin record);

    List<Admin> selectByExample(AdminExample example);

    Admin selectByPrimaryKey(Integer id);

    List<Admin> selectByKeyword(String keyword);

    int updateByExampleSelective(@Param("record") Admin record, @Param("example") AdminExample example);

    int updateByExample(@Param("record") Admin record, @Param("example") AdminExample example);

    int updateByPrimaryKeySelective(Admin record);

    int updateByPrimaryKey(Admin record);

    void deleteRoleByAdminId(Integer adminId);

    /**
     * 动态sql
     *
     * @param adminId
     * @param roleIdList
     */
    void insertNewRolesByAdminId(@Param("adminId") Integer adminId, @Param("roleIdArry") List<Integer> roleIdList);
}
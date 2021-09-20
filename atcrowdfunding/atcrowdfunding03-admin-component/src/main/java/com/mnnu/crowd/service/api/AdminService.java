package com.mnnu.crowd.service.api;

import com.github.pagehelper.PageInfo;
import com.mnnu.crowd.entity.Admin;

import java.util.List;

/**
 * @author qiaoh
 */
public interface AdminService {
    /**
     * 添加管理员角色
     *
     * @param admin 传入保存的管理员信息
     */
    void saveAdmin(Admin admin);

    /**
     * 通过账户和密码登录系统，获取管理员对象
     *
     * @param loginAcct 管理员账号
     * @param userPswd  管理员明文密码
     * @return 返回查询的到的信息
     */
    Admin getAdminByLoginAcct(String loginAcct, String userPswd);

    /**
     * 获取分页信息
     *
     * @param keyword  搜索的关键字，将在账号、用户名、邮箱之间查找
     * @param pageNum  搜索的当前页码
     * @param pageSize 每页的个数
     * @return 返回分页信息
     */
    PageInfo<Admin> getPageInfo(String keyword, Integer pageNum, Integer pageSize);

    /**
     * 根据管理员id删除对应管理员角色
     *
     * @param id 管理员id
     */
    void removeAdmin(Integer id);

    /**
     * 通过主键id获取admin信息
     *
     * @param id admin的主键id
     * @return admin信息
     */
    Admin getAdminByPrimaryKey(Integer id);

    /**
     * 只更新不为空的字段
     *
     * @param admin 管理员角色信息
     */
    void updateAdmin(Admin admin);

    @Deprecated
    void deleteOldRole(Integer adminId);

    @Deprecated
    void addNewRole(Integer adminId, List<Integer> roleIdList);

    /**
     * 更新角色信息，代替之前的单纯删除和添加
     *
     * @param adminId
     * @param roleIdList
     */
    void updateRoleInfo(Integer adminId, List<Integer> roleIdList);
}

package com.mnnu.crowd.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mnnu.crowd.constant.CrowdConstant;
import com.mnnu.crowd.entity.Admin;
import com.mnnu.crowd.entity.AdminExample;
import com.mnnu.crowd.exception.LoginAcctInUseException;
import com.mnnu.crowd.exception.LoginFailedException;
import com.mnnu.crowd.mapper.AdminMapper;
import com.mnnu.crowd.service.api.AdminService;
import com.mnnu.crowd.util.CrowdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author qiaoh
 */
@Service
public class AdminServiceImpl implements AdminService {

    private AdminMapper adminMapper;

    @Autowired
    public void setAdminMapper(AdminMapper adminMapper) {
        this.adminMapper = adminMapper;
    }

    @Override
    public void saveAdmin(Admin admin) {
        adminMapper.insert(admin);
    }

    @Override
    public Admin getAdminByLoginAcct(String loginAcct, String userPswd) {
        //1.查询对象
        //尝试QBC查询
        AdminExample adminExample = new AdminExample();

        //创建example对象，获取criteria
        AdminExample.Criteria criteria = adminExample.createCriteria();
        //设置查询条件
        criteria.andLoginAcctEqualTo(loginAcct);
        //mapper通过example对象查询
        List<Admin> admins = adminMapper.selectByExample(adminExample);

        //2.对象是否为空
        if (admins == null || admins.size() == 0) {
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }

        if (admins.size() > 1) {
            throw new RuntimeException(CrowdConstant.MESSAGE_SYSTEM_ERROR);
        }

        Admin adminDB = admins.get(0);

        if (adminDB == null) {
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }
        //3.两个密码进行比对

        String passwordEncoding = CrowdUtil.passwordEncoding(userPswd);
        if (!Objects.equals(passwordEncoding, adminDB.getUserPswd())) {
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }
        //4.返回账户信息
        return adminDB;
    }

    @Override
    public PageInfo<Admin> getPageInfo(String keyword, Integer pageNum, Integer pageSize) {
        // 1.使用pageHelper的静态方法开启分页功能
        PageHelper.startPage(pageNum, pageSize);
        // 2.查询数据
        List<Admin> admins = adminMapper.selectByKeyword(keyword);
        // 3.将数据传入pageInfo对象返回
        return new PageInfo<>(admins);
    }

    @Override
    public void removeAdmin(Integer id) {
        adminMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Admin getAdminByPrimaryKey(Integer id) {
        return adminMapper.selectByPrimaryKey(id);
    }

    @Override
    public void updateAdmin(Admin admin) {

        try {
            adminMapper.updateByPrimaryKeySelective(admin);
        } catch (Exception e) {
            //如果更新后重复则抛异常
            e.printStackTrace();
            if (e instanceof DuplicateKeyException) {
                throw new LoginAcctInUseException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }
        }

    }

    @Override
    public void deleteOldRole(Integer adminId) {
        adminMapper.deleteRoleByAdminId(adminId);
    }

    @Override
    public void addNewRole(Integer adminId, List<Integer> roleIdList) {
        if (roleIdList == null || roleIdList.size() == 0) {
            return;
        }
        adminMapper.insertNewRolesByAdminId(adminId, roleIdList);
    }

    @Override
    public void updateRoleInfo(Integer adminId, List<Integer> roleIdList) {
        deleteOldRole(adminId);

        addNewRole(adminId, roleIdList);
    }
}

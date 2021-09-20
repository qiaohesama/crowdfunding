package com.mnnu.crowd.service.impl;

import com.mnnu.crowd.entity.Auth;
import com.mnnu.crowd.entity.AuthExample;
import com.mnnu.crowd.mapper.AuthMapper;
import com.mnnu.crowd.service.api.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author qiaoh
 */
@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    AuthMapper authMapper;

    @Override
    public List<Auth> getAllAuth() {
        return authMapper.selectByExample(new AuthExample());
    }
}

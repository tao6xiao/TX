package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.dao.UserMapper;
import com.trs.gov.kpi.entity.User;
import org.springframework.stereotype.Service;

import com.trs.gov.kpi.service.UserService;

import javax.annotation.Resource;

/**
 * Created by wangxuan on 2017/5/9.
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    UserMapper userMapper;

    @Override
    public User getUserById(int id) {

        return userMapper.selectByPrimaryKey(id);
    }
}

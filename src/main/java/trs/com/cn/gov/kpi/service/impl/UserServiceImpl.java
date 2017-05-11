package trs.com.cn.gov.kpi.service.impl;

import org.springframework.stereotype.Service;
import trs.com.cn.gov.kpi.dao.UserMapper;
import trs.com.cn.gov.kpi.entity.User;
import trs.com.cn.gov.kpi.service.UserService;

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

        return userMapper.getUserById(id);
    }
}

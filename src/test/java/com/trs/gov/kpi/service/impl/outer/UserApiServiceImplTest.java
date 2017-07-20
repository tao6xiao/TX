package com.trs.gov.kpi.service.impl.outer;

import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.User;
import com.trs.gov.kpi.service.outer.UserApiService;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by he.lang on 2017/6/27.
 */
public class UserApiServiceImplTest {
    @Test
    public void findUserById_right() throws Exception {
        MockUserService service = new MockUserService();
        assertEquals("zhangsan", service.findUserById("", 1).getUserName());
    }

    @Test
    public void findUserById_right_two() throws Exception {
        MockUserService service = new MockUserService();
        User user = service.findUserById("", 1);
        int count = 0;
        if(user.getUserId() != null){
            count++;
        }
        if(user.getUserName() != null){
            count++;
        }
        if(user.getTrueName() != null){
            count++;
        }
        if(user.getMobile() != null){
            count++;
        }
        assertEquals(4, count);
    }

    @Test
    public void findUserById_null() throws Exception {
        MockUserService service = new MockUserService();
        assertEquals(null, service.findUserById("", 2).getUserId());
    }

    @Test
    public void findUserById_object() throws Exception {
        MockUserService service = new MockUserService();
        User user = new User();
        assertEquals(user.getTrueName(), service.findUserById("", 3).getTrueName());
    }

    private class MockUserService implements UserApiService {

        @Override
        public User findUserById(String currUserName, int userId) throws RemoteException {
            User user = null;
            if (userId == 1) {
                user = new User();
                user.setUserId(userId);
                user.setMobile("123");
                user.setTrueName("张三");
                user.setUserName("zhangsan");
                return user;
            }else if(userId == 2){
                user = new User();
                user.setUserId(null);
            }
            return new User();
        }
    }

}
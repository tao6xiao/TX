package com.trs.gov.kpi.entity.responsedata;

import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.service.outer.UserApiService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * Created by linwei on 2017/6/15.
 */
@Component
@Scope("prototype")
@Slf4j
public class UserDocMultiCounterResponse extends DocMultiCounterResponse {

    @Getter
    private Long userId = 0L;

    @Getter
    @Setter
    private String userName;

    @Resource
    UserApiService userApiService;

    public void setUserId(Long userId){
        this.userId = userId;
//        try {
//            if(userApiService.findUserById("", Math.toIntExact(userId)) != null) {
//                this.userName = userApiService.findUserById("", Math.toIntExact(userId)).getUserName();
//            }
//        } catch (RemoteException e) {
//            log.error("", e);
//        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UserDocMultiCounterResponse that = (UserDocMultiCounterResponse) o;
        return Objects.equals(getUserId(), that.getUserId()) &&
                Objects.equals(getUserName(), that.getUserName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getUserId(), getUserName());
    }
}

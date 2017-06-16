package com.trs.gov.kpi.entity.responsedata;

import lombok.Getter;

import java.util.Objects;

/**
 * Created by linwei on 2017/6/15.
 */
public class UserDocMultiCounterResponse extends DocMultiCounterResponse {

    @Getter
    private Long userId;

    @Getter
    private String userName;

    public void setUserId(Long userId) {
        this.userId = userId;
        // TODO wait for editcenter
        this.userName = "UserId[" + String.valueOf(userId) + "]";
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

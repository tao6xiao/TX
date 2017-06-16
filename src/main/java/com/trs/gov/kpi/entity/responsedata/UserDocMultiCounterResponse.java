package com.trs.gov.kpi.entity.responsedata;

import lombok.Data;

import java.util.Objects;

/**
 * Created by linwei on 2017/6/15.
 */
@Data
public class UserDocMultiCounterResponse extends DocMultiCounterResponse {

    private Long userId;
    private String userName;

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

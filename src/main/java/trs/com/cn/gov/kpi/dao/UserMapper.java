package trs.com.cn.gov.kpi.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import trs.com.cn.gov.kpi.entity.User;

/**
 * Created by wangxuan on 2017/5/9.
 */
@Mapper
public interface UserMapper {

    User getUserById(@Param("id") int id);
}

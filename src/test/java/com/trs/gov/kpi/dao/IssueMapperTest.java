package com.trs.gov.kpi.dao;

import com.trs.gov.kpi.entity.dao.DBPager;
import com.trs.gov.kpi.entity.dao.OrCondDBFields;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.dao.Table;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.Arrays;

/**
 * Created by linwei on 2017/5/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class IssueMapperTest {

    @Resource
    private IssueMapper issueMapper;

    @Test
    public void select() throws Exception {
        QueryFilter filter = new QueryFilter(Table.ISSUE);

        // 添加siteId和错误类型查询条件
        filter.addCond("siteId", 11);

        // 设置一个复合型查询条件
        OrCondDBFields orFields = new OrCondDBFields();
        orFields.addCond("typeId", Arrays.asList(1,2,3));
        orFields.addCond("detail", "test").setLike(true);
        filter.addOrConds(orFields);

        // 添加一个 groupby 字段
        filter.addGroupField("typeId");

        // 添加一个升序的排序字段
        filter.addSortField("id");

        // 添加分页

        filter.setPager(new DBPager(0, 10));

        // 查询
//        issueMapper.select(filter);
    }

}
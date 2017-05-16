package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.ChnlGroups;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.ChnlGroupsResponseDetail;
import com.trs.gov.kpi.service.ChnlGroupService;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 栏目分类Controller
 * Created by he.lang on 2017/5/16.
 */
@RestController
@RequestMapping("/gov/kpi/setting")
public class ChnlGroupController {
    @Resource
    ChnlGroupService chnlGroupService;

    /**
     * 获取栏目分类
     *
     * @return
     */
    @RequestMapping(value = "/chnlgroups", method = RequestMethod.GET)
    @ResponseBody
    public ChnlGroupsResponseDetail[] getChnlGroups() {
        return chnlGroupService.getChnlGroupsResponseDetailArray();
    }

    /**
     * 分页查询对于siteId和groupId下面的数据记录
     * @param siteId
     * @param groupId
     * @param pageSize
     * @param pageIndex
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/chnlgroup/chnls", method = RequestMethod.GET)
    @ResponseBody
    public ApiPageData getPageDataBySiteIdAndGroupId(@RequestParam("siteId") Integer siteId, @RequestParam Integer groupId,@RequestParam("pageSize") Integer pageSize, @RequestParam("pageIndex") Integer pageIndex) throws BizException {
        if(siteId == null || groupId == null){
            throw new BizException("参数存在null值");
        }
        return null;
    }
}

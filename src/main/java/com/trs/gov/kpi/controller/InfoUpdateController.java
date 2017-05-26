package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.entity.InfoUpdate;
import com.trs.gov.kpi.entity.IssueBase;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.Statistics;
import com.trs.gov.kpi.service.InfoUpdateService;
import com.trs.gov.kpi.utils.InitQueryFiled;
import com.trs.gov.kpi.utils.IssueCounter;
import com.trs.gov.kpi.utils.PageInfoDeal;
import com.trs.gov.kpi.utils.ParamCheckUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 信息更新问题
 */
@RestController
@RequestMapping("/gov/kpi/channel/issue")
public class InfoUpdateController {

    @Resource
    private InfoUpdateService infoUpdateService;

    /**
     * 查询已解决、预警和更新不及时的数量
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/bytype/count", method = RequestMethod.GET)
    public List getIssueCount(@ModelAttribute PageDataRequestParam param) throws BizException {
        ParamCheckUtil.paramCheck(param);
        List<Statistics> statisticsList = infoUpdateService.getIssueCount(param);
        return statisticsList;
    }

    /**
     * 查询历史记录
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/all/count/history", method = RequestMethod.GET)
    public List getIssueHistoryCount(@ModelAttribute PageDataRequestParam param) throws BizException {
        ParamCheckUtil.paramCheck(param);
        return infoUpdateService.getIssueHistoryCount(param);
    }

    /**
     * 查询待解决的问题列表
     *
     * @param param
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/unhandled", method = RequestMethod.GET)
    public ApiPageData getIssueList(@ModelAttribute PageDataRequestParam param) throws BizException, RemoteException {
        ParamCheckUtil.paramCheck(param);
        ApiPageData apiPageData = infoUpdateService.get(param);
        return apiPageData;
    }


    /**
     * 批量处理
     *
     * @param siteId
     * @param ids
     * @return
     */
    @RequestMapping(value = "/handle", method = RequestMethod.POST)
    public String handIssuesByIds(Integer siteId, Integer[] ids) throws BizException {
        if(siteId == null){
            throw new BizException("参数siteId存在null值");
        }
        infoUpdateService.handIssuesByIds(siteId, Arrays.asList(ids));
        return null;
    }

    /**
     * 批量忽略
     *
     * @param siteId
     * @param ids
     * @return
     */
    @RequestMapping(value = "/ignore", method = RequestMethod.POST)
    public String ignoreIssuesByIds(Integer siteId, Integer[] ids) throws BizException {
        if(siteId == null){
            throw new BizException("参数siteId存在null值");
        }
        infoUpdateService.ignoreIssuesByIds(siteId, Arrays.asList(ids));
        return null;
    }

    /**
     * 批量删除
     *
     * @param siteId
     * @param ids
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public String delIssueByIds(Integer siteId, Integer[] ids) throws BizException {
        if(siteId == null){
            throw new BizException("参数siteId存在null值");
        }
        infoUpdateService.delIssueByIds(siteId, Arrays.asList(ids));
        return null;
    }

    /**
     * 获取栏目信息更新不及时的统计信息
     *
     * @param param
     * @return
     * @throws BizException
     * @throws ParseException
     * @throws RemoteException
     */
    @RequestMapping(value = "/bygroup/count", method = RequestMethod.GET)
    @ResponseBody
    public List<Statistics> getUpdateNotInTimeCountList(@ModelAttribute PageDataRequestParam param) throws BizException, ParseException, RemoteException {
        ParamCheckUtil.paramCheck(param);
        List<Statistics> updateNotInTimeCountList = infoUpdateService.getUpdateNotInTimeCountList(param);
        return updateNotInTimeCountList;
    }

//    /**
//     * 获取栏目更新不及时
//     * @param issueBase
//     * @return
//     * @throws BizException
//     * @throws ParseException
//     * @throws RemoteException
//     */
//    @RequestMapping(value = "/bygroup/all/count", method = RequestMethod.GET)
//    @ResponseBody
//    public Integer getUpdateNotInTimeAllCount(@ModelAttribute PageDataRequestParam param) throws BizException, ParseException, RemoteException {
//        
//        // prepareIssueBase(issueBase);
//        Integer count = infoUpdateService.getAllDateUpdateNotInTime(issueBase);
//        return count;
//    }

}

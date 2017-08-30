package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.constant.EnumIndexUpdateType;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.sp.SGStatistics;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.IndexPage;
import com.trs.gov.kpi.entity.responsedata.Statistics;
import com.trs.gov.kpi.service.InfoUpdateService;
import com.trs.gov.kpi.service.LinkAvailabilityService;
import com.trs.gov.kpi.service.outer.SGService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 绩效指数计算规则：
 * 网站可用情况:
 * 站点无法访问的，此项直接为0分；
 * 其他页面的链接（包括图片、外部链接等），每发现一个打不开或错误的，扣0.1%；
 * 单个指标扣分不设上限，此项分数扣完为止，不计负分）
 * 总分：37.5
 * <p>
 * 网站信息更新情况：
 * 网站中应更新但长期未更新的栏目数，每发现一个扣10%分；空白栏目问题，每发现一个扣20%分。扣完为止。
 * <p>
 * 总分：37.5
 * <p>
 * 办事指南要素的完整性、准确性：
 * 办事指南要素类别缺失的，每发现一项扣10%分，扣完为止；
 * 办事指南要素内容不准确的，每发现一项扣10%分，扣完为止。
 * 总分：12.5
 * <p>
 * 咨询信件答复质量情况：
 * ，答复推诿、答复不及时，回复质量差,每发现一项扣10%分，扣完为止。
 * 总分：6.25
 * <p>
 * 围绕年度重点工作开展在线访谈情况：
 * 开展一次的，得50%；
 * 开展二次的，得满分；
 * 未开展的，则不得分。
 * 总分：6.25
 */
@Service
public class PerformanceService {

    @Resource
    private LinkAvailabilityService linkAvailabilityService;

    @Resource
    private InfoUpdateService infoUpdateService;

    @Resource
    private SGService sgService;

    public Double calPerformanceIndex(Integer siteId) throws BizException, RemoteException {

        PageDataRequestParam param = new PageDataRequestParam();
        param.setSiteId(siteId);

        //咨询信件答复质量情况
        double advisoryScore = 6.25;
        advisoryScore *= (1);

        //年度在线访谈情况
        double interviewScore = 6.25;
        interviewScore *= (1);

        //总分
        double sum = getAvailabilityScore(param) + getInfoUpdateScore(param) + getHandleGuideScore(param) + advisoryScore + interviewScore;

        return Double.parseDouble(String.format("%.2f", sum));
    }

    //办事指南得分
    private double getHandleGuideScore(PageDataRequestParam param) throws RemoteException {

        double handleGuideScore = 12.5;
        SGStatistics sgStatistics = sgService.getSGCount(param);
        int handleIssueCount = 0;
        if (sgStatistics != null) {
            handleIssueCount = sgStatistics.getAbandonedCounts();
        }

        handleGuideScore *= 1 - handleIssueCount * 0.1;
        if (handleGuideScore < 0) {
            handleGuideScore = 0;
        }
        return handleGuideScore;
    }

    //网站信息更新得分
    private double getInfoUpdateScore(PageDataRequestParam param) throws BizException, RemoteException {
        double infoUpdateScore = 37.5;

        List<Statistics> statisticsList = infoUpdateService.getUpdateNotInTimeCountList(param);
        int updateNotInTimeCount = 0;
        int blankChnlCount = 0;
        for (Statistics statistics : statisticsList) {
            if (statistics.getType() == EnumIndexUpdateType.ALL.getCode()) {
                updateNotInTimeCount = statistics.getCount();
            } else if (statistics.getType() == EnumIndexUpdateType.NULL_CHANNEL.getCode()) {
                blankChnlCount = statistics.getCount();
            }
        }
        double percent = 1 - updateNotInTimeCount * 0.1 - blankChnlCount * 0.2;
        if (percent > 0) {
            infoUpdateScore *= percent;
        } else {
            infoUpdateScore = 0;
        }
        return infoUpdateScore;
    }

    //网站可用性得分
    private double getAvailabilityScore(PageDataRequestParam param) throws RemoteException {

        double availabilityScore = 37.5;
        IndexPage indexPage = linkAvailabilityService.showIndexAvailability(param);
        if (indexPage != null && indexPage.getIndexAvailable() != null && indexPage.getIndexAvailable() == Boolean.TRUE) {
            int linkIssueCount = linkAvailabilityService.getUnhandledIssueCount(param);
            availabilityScore *= 1 - linkIssueCount * 0.001;
            if (availabilityScore < 0) {
                availabilityScore = 0;
            }
        } else {
            availabilityScore = 0;
        }
        return availabilityScore;
    }


}

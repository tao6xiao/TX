package com.trs.gov.kpi.utils;

import com.squareup.okhttp.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangxuan on 2017/5/15.
 */
public class HttpUtils {

    public static String getHttpInfo(String url, Map<String, String> params) throws Exception {

        OkHttpClient httpClient = new OkHttpClient();
        URIBuilder uriBuilder = new URIBuilder(url);
        for (Map.Entry<String, String> param : params.entrySet()) {

            uriBuilder.addParameter(param.getKey(), param.getValue());
        }

        Request request = new Request.Builder()
                .url(uriBuilder.build().toURL())
                .build();

        Response response = httpClient.newCall(request).execute();
        return response.body().string();
    }

    public static String postHttpInfo(String url, Map<String, String> params) throws Exception {

        OkHttpClient httpClient = new OkHttpClient();

        FormEncodingBuilder form = new FormEncodingBuilder();
        for(Map.Entry<String, String> param : params.entrySet()) {

            form.add(param.getKey(), param.getValue());
        }
        Request request = new Request.Builder()
                .url(url)
                .post(form.build())
                .build();
        Response response = httpClient.newCall(request).execute();
        return response.body().string();
    }

    public static void main(String[] args) {

        String url = "http://jiaodui.trs.cn/rs/detect";
        String text = "成都市公安局\n" +
                "关于加强成都双流国际机场净空区域安全保护的通告\n" +
                "    为维护成都双流国际机场净空保护区域飞行安全，根据《中华人民共和国国家安全法》《中华人民共和国反恐怖主义法》等法律法规以及西部战区空军参谋部、民航西南地区管理局、民航西南地区空中交通管理局、四川省公安厅四部门联合发布的《关于加强全省军民航机场净空区域安全保护的通告》相关规定，现就成都双流国际机场净空区域安全保护有关事项通告如下：\n" +
                "    一、严禁任何单位、组织和个人未经军民航职能部门批准，在成都双流国际机场净空保护区域内进行无人机、航空模型等飞行活动。\n" +
                "    凡取得军民航职能部门批准的飞行，在每次飞行前必须向属地公安机关治安部门进行书面报备。\n" +
                "    二、对在机场净空保护区擅自进行无人机、航空模型等飞行活动的，公安机关将依法从严处理。\n" +
                "    三、全市所有单位、组织和个人应当遵守相关法律、法规，必须配合公安机关开展机场净空保护工作。对妨碍公安机关依法开展调查，不如实提供证据、谎报案情、制造虚假信息的，将依法追究相关法律责任。\n" +
                "    四、鼓励广大市民积极发现、规劝和举报可能扰乱飞行安全的违法行为。公安机关24小时接受线索举报，线索一经查实，公安机关将按相关规定给予举报人奖励。\n" +
                "    五、本通告机场净空保护区是指距机场跑道中心线两侧各10公里，跑道端外20公里以内的区域。包含我市高新区、天府新区、锦江区、青羊区、金牛区、武侯区、成华区、双流区、郫都区、温江区、新津县等地区全部或部分区域。\n" +
                "    本通告自发布之日起施行，有效期一年。\n" +
                "    公安机关举报电话：110\n" +
                "2017年5月1日\n" +
                " \n" +
                "西部战区空军参谋部 民航西南地区管理局 民航西南地区空中交通管理局 四川省公安厅\n" +
                "关于加强全省军民航机场净空区域安全保护的通告\n" +
                "　　为维护全省军民航机场净空保护区域飞行安全，根据《中华人民共和国民用航空法》《中华人民共和国飞行基本规则》《军用机场净空规定》《通用航空飞行管制条例》《民用机场管理条例》《四川省民用机场净空及电磁环境保护条例》等规定，现就军民航机场净空区域安全保护有关事项通告如下：\n" +
                "　　一、严禁任何单位、组织和个人在全省军民航机场净空保护区域内从事下列活动：\n" +
                "　　（一）未经军民航职能部门批准，进行无人机、航空模型等飞行活动；\n" +
                "　　（二）排放大量烟雾、粉尘、火焰、废气等影响飞行安全的物质；\n" +
                "　　（三）放飞影响飞行安全的鸟类、孔明灯，升放无人驾驶的自由气球和其他升空物体；\n" +
                "　　（四）焚烧产生大量烟雾的农作物秸秆、垃圾等物质，或者燃放烟花、焰火。\n" +
                "　　民用机场净空保护区域是指距机场跑道中心线两侧各10km、跑道端外20km以内的区域；军用机场净空保护区域是指距机场跑道中心线两侧各15km、跑道端外20km以内的区域。\n" +
                "　　二、在机场净空区域内实施上述违法行为的，军民航职能部门、公安机关将依法予以查处；在机场净空区域外从事上述活动的，不得影响机场净空保护。\n" +
                "　　三、凡开展通用航空飞行活动，应当提前向军民航职能部门提出申请，经批准后方可实施。\n" +
                "　　四、重大活动期间的空域管控措施以地方人民政府公告为准。\n" +
                "　\n" +
                "　　五、所有单位、组织和个人应当遵守相关法律、法规，支持配合军民航职能部门和公安机关开展机场净空保护工作。鼓励积极发现、规劝和举报可能扰乱飞行安全的违法行为。举报的违法飞行线索一经查实，公安机关将按相关规定给予举报人不低于10000元人民币的奖励。\n" +
                "　　本通告自发布之日起施行。\n" +
                "　　空军飞行管制部门电话：\n" +
                "　　（028）85399067\n" +
                "　　民航空中管制部门电话：\n" +
                "　　（028）85702366（白天）\n" +
                "　　（028）85702365（夜间）\n" +
                "　　公安机关举报电话：110\n" +
                "西部战区空军参谋部\n" +
                "　　民航西南地区管理局\n" +
                "　　民航西南地区空中交通管理局\n" +
                "四川省公安厅\n" +
                "　　2016年9月1日";

        Map<String, String> params = new HashMap<>();
        params.put("type", "字词;政治");
        params.put("text", text);

        String result = StringUtils.EMPTY;
        try {
            result = postHttpInfo(url, params);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.print(result);
    }
}

/**
 * Created:         2005-2-3 9:01:22
 * Last Modified:   2005-2-3/2005-2-3
 * Description:
 * class ContextHelper
 */
package com.trs.gov.kpi.ids;

import com.trs.gov.kpi.entity.LocalUser;
import com.trs.gov.kpi.entity.exception.BizRuntimeException;
import lombok.extern.slf4j.Slf4j;

/**
 * Title: TRS 内容协作平台（TRS WCM） <BR>
 * Description: <BR>
 * 使用ThreadLocal方式在各个功能模块间传递参数的管理模块 <BR>
 * Copyright: Copyright (c) 2004-2005 TRS信息技术有限公司 <BR>
 * Company: TRS信息技术有限公司(www.trs.com.cn) <BR>
 *
 * @author TRS信息技术有限公司
 * @version 1.0
 */
@Slf4j
public class ContextHelper {
    private ContextHelper() {

    }

    private static final int ARG_INDEX_USER = BaseContextHelper.getNextIndex();

    public static final int CONTEXT_INDEX_IP = BaseContextHelper.getNextIndex();

    /**
     * 利用单态模式初始化ContextTL，保证只有一个 处理策略： <BR>
     * 1. 接口为静态方法，static 2. 由于每个线程应该只有一个ContextTL，所以应该以单态方式创建ContextTL。
     * 当ContextTL已经存在时则直接返回当前的ContextTL。 3.
     * 对于初始化时是否需要做一些初始化时的工作，比如setLoginUser，有两种考虑
     * 。一是为了方便用户调用，可以将setLoginUser放入initContext中
     * 。另一种考虑，为了保证接口功能的明确和独立，不应该包含其它任何逻辑
     * 。这里我们采取不包含业务逻辑的策略，保证接口功能的独立性，也便于其它定制开发的需要。
     *
     * setArg(CONTEXT_INDEX_IP, loginUser.getLastLoginIP());//添加ip记录
     * @return ContextTL的现有实例
     */
    /**
     * 初始化时，同时初始化登录用户参数
     */
    public static void initContext(LocalUser loginUser) {
        setLoginUser(loginUser);
        setArg(CONTEXT_INDEX_IP, loginUser.getLastLoginIP());//添加ip记录
    } // END of initContext

    /**
     * 获得当前线程的登录用户，委托给ContextTL 处理策略： <BR>
     * 1. 接口为静态方法
     *
     * @return 当共享参数存在时，直接返回ContextTL的loginUser（当前登录用户）；当共享参数不存在时，返回空（NULL）
     */
    public static LocalUser getLoginUser() {
        LocalUser loginUser = (LocalUser) BaseContextHelper.getArg(ARG_INDEX_USER);
        if (loginUser == null) {
            log.info("fatal error:no current user in threadlocal,iniContext please!");
            throw new BizRuntimeException("fatal error:no current user in threadlocal,iniContext please!");
        }
        return loginUser;
    } // END of getLoginUser

    /**
     * 设置当前线程的登录用户
     *
     * @param currUser 登录用户
     */
    public static void setLoginUser(LocalUser currUser) {
        BaseContextHelper.setArg(ARG_INDEX_USER, currUser);
    } // END of setLoginUser

    /**
     * 获得扩展参数列表中第_nIndex位置上的参数 处理策略： <BR>
     * 1. 从ArrayList中定位到第_nIndex个位置 2. 取出来的对象使用前要做类型强制转换
     *
     * @param nIndex ：扩展参数列表中参数的存放位置
     * @return 如果指定位置上有对象，则返回该对象；如果没有则返回NULL（异常？）
     */
    public static Object getArg(int nIndex) {
        return BaseContextHelper.getArg(nIndex);
    } // END of getArg

    /**
     * 在扩展参数列表的第_nIndex个位置设置参数对象_currObj 处理策略： <BR>
     * 1. 如果该位置上已经存在参数，则替换原有参数
     *
     * @param nIndex  ：扩展参数列表中的位置
     * @param currObj ：设置的参数
     * @return：设置后的位置，返回-1表示设置失败
     */
    public static int setArg(int nIndex, Object currObj) {
        return BaseContextHelper.setArg(nIndex, currObj);
    } // END of setArg

    public static void clear() {
        BaseContextHelper.clear();

        // 清除本地中线程的缓存
        CacheLayer.clear();
    }
}
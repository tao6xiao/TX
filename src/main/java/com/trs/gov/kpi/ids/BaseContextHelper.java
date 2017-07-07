/**
 * Created:         2005-2-3 9:01:22
 * Last Modified:   2005-2-3/2005-2-3
 * Description:
 *      class ContextHelper
 */
package com.trs.gov.kpi.ids;

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
public class BaseContextHelper {
	private BaseContextHelper(){}
	private static ContextTL contextTL = null;

	private static int nextIndex = 0;

	/**
	 * 获得扩展参数列表中第_nIndex位置上的参数 处理策略： <BR>
	 * 1. 从ArrayList中定位到第_nIndex个位置 2. 取出来的对象使用前要做类型强制转换
	 * 
	 * @param nIndex
	 *            ：扩展参数列表中参数的存放位置
	 * @return 如果指定位置上有对象，则返回该对象；如果没有则返回NULL（异常？）
	 */
	public static Object getArg(int nIndex) {
		return getContextTL().getArg(nIndex);
	} // END of getArg

	/**
	 * 在扩展参数列表的第_nIndex个位置设置参数对象_currObj 处理策略： <BR>
	 * 1. 如果该位置上已经存在参数，则替换原有参数
	 * 
	 * @param nIndex
	 *            ：扩展参数列表中的位置
	 * @param currObj
	 *            ：设置的参数
	 * @return：设置后的位置，返回-1表示设置失败
	 */
	public static int setArg(int nIndex, Object currObj) {
		return getContextTL().setArg(nIndex, currObj);
	} // END of setArg

	public static synchronized int getNextIndex() {
		if (nextIndex >= (ContextTL.MAX_THREADLOCAL_LENGTH - 1)) {
			log.error("当前系统只可以容纳最多"+ContextTL.MAX_THREADLOCAL_LENGTH + "个变量!");
			throw new BizRuntimeException("BaseContextHelper.label1 fatal of ThreadLocal:系统只可以容纳最多"
							+ ContextTL.MAX_THREADLOCAL_LENGTH + "个变量!");
		}

		getContextTL().setArg(++nextIndex, null);
		return nextIndex;
	}

	/**
	 * @return
	 */
	private static ContextTL getContextTL() {
		if (contextTL == null)
			contextTL = new ContextTL();
		return contextTL;
	}

	public static void clear() {
		getContextTL().clear();
	}
}
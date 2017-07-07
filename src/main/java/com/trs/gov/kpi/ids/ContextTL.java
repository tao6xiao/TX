/**
 * Created:         2005-2-3 10:00:34
 * Last Modified:   2005-2-3/2005-2-3
 * Description:
 *      class ContextTL
 */
package com.trs.gov.kpi.ids;

import java.util.ArrayList;
import java.util.List;

/**
 * Title: TRS 内容协作平台（TRS WCM） <BR>
 * Description: <BR>
 * 利用ThreadLocal传递参数的具体实现 <BR>
 * Copyright: Copyright (c) 2004-2005 TRS信息技术有限公司 <BR>
 * Company: TRS信息技术有限公司(www.trs.com.cn) <BR>
 * 
 * @author TRS信息技术有限公司
 * @version 1.0
 */

public class ContextTL extends ThreadLocal {

    // wenyh@2005-3-30 15:39:49 add comment:修改实现,使用数组
    public static final int MAX_THREADLOCAL_LENGTH = 20;// 最多只存10个变量

    public ContextTL() {
        super();
    }

    /**
     * 初始化参数对象体（参数对象的结构） 处理逻辑： <BR>
     * 1. 建立两个ArrayList 2. 一个存放系统专用的参数列表，一个提供给用户进行参数扩展
     * 
     * @return 包含两个ArrayList的ArrayList对象
     */
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.ThreadLocal#initialValue()
     */
    @Override
    protected Object initialValue() {
        return new Object[MAX_THREADLOCAL_LENGTH];
    } // END of initialValue

    /**
     * 获得用户自定义的扩展参数列表 处理逻辑： <BR>
     * 1. 第2个ArrayList是用户自定义的扩展参数List
     * List<List></>
     * 
     * @return 如果尚未初始化，则返回NUL；否则返回ArrayList
     */
    public List getArgs() {

        return (ArrayList) ((ArrayList) (super.get())).get(1);
    } // END of getArgs

    /**
     * 设置用户自定义的扩展参数列表 处理逻辑： <BR>
     * 1. 用户构造一个ArrayList，并设置好参数，然后调用该接口设置参数 2. 第2个ArrayList是用户自定义的扩展参数List
     * 
     * @param currArgs
     */
    public void setArgs(List currArgs) {
        getArgs().clear();
        getArgs().addAll(currArgs);
    } // END of setArgs

    /**
     * 获取扩展参数列表中第_nIndex个位置上的参数
     * 
     * @param nIndex
     *            ：参数位置，从0开始
     * @return 如果没有初始化，则返回NULL；否则返回_nIndex位置上的对象
     */
    public Object getArg(int nIndex) {

        if (nIndex < MAX_THREADLOCAL_LENGTH) {
            return ((Object[]) super.get())[nIndex];
        }

        return null;
    } // END of getArg

    /**
     * 在扩展参数列表的第_nIndex个位置设置参数 处理逻辑： <BR>
     * 
     * @param nIndex
     *            ：扩展参数列表中的位置，为-1表示按顺序增加变量
     * @param currObj
     *            ：设置的参数
     * @return：设置后的位置，返回-1表示设置失败、尚未初始化
     */
    public int setArg(int nIndex, Object currObj) {
        if (nIndex < 0 || nIndex > MAX_THREADLOCAL_LENGTH)
            return -1;

        ((Object[]) super.get())[nIndex] = currObj;

        return nIndex;
    } // END of setArg

    public void clear() {
        Object[] args = ((Object[]) super.get());
        for (int i = 0; i < args.length; i++) {
            Object object = args[i];
            if (object != null)
                args[i] = null;
        }
    }
}
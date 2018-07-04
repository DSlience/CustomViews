package com.adealink.baselib.utils;

import java.util.List;

/**
 * Created by Xuefu_Du on 2018/6/21.
 */
public class CommonUtil {

    /**
     * @param list 列表
     * @return 判断list是否为空（null或者size为0）
     */
    public static boolean isListBlank(List list) {
        boolean isBlank = (list == null) || (list.size() <= 0);
        return isBlank;
    }
}

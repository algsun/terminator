package com.microwise.terminator.sys.util;

import org.joda.time.DateTime;

import java.util.Date;

/**
 * @author zhangpeng
 * @date 2013-2-26
 */
public class ExportUtil {

    /**
     * 文件类型：excel
     */
    public static final int FILR_TYPE_EXCEL = 1;

    /**
     * 获得文件文件名
     *
     * @param deviceName 设备名称
     * @param deviceId   设备对象
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @param isZip      是否是zip 文件名
     * @return string excel文件名称
     * @author 许保吉
     * @date 2013-2-5
     */
    public static String getFileName(String deviceName, String deviceId,
                                     Date startTime, Date endTime, boolean isZip) {
        String fileName;
        String minTime = DateTimeUtil.format(DateTimeUtil.YYYY_MM_DD, startTime);
        String maxTime = DateTimeUtil.format(DateTimeUtil.YYYY_MM_DD, endTime);
        fileName = (deviceName == null ? "" : deviceName) + "(" + deviceId
                + ")" + " "
                + (minTime.equals(maxTime) ? minTime : minTime + "至" + maxTime)
                + "历史数据" + (isZip ? ".zip" : ".xlsx");
        return fileName;
    }

    /**
     * 获得excel文件的最小时间
     *
     * @param currentYear 当前年
     * @param startTime   开始时间
     * @return DateTime
     * @author 许保吉
     * @date 2013-2-26
     */
    public static DateTime getMinTime(Integer currentYear, Date startTime) {
        DateTime startDataTime = new DateTime(startTime);
        return currentYear == startDataTime.getYear() ? startDataTime
                : new DateTime(currentYear, 1, 1, 0, 0, 0);
    }

    /**
     * 获得excel文件的最大时间
     *
     * @param currentYear 当前年
     * @param endTime     结束时间
     * @return DateTime
     * @author 许保吉
     * @date 2013-2-26
     */
    public static DateTime getMaxTime(Integer currentYear, Date endTime) {
        DateTime endDateTime = new DateTime(endTime);
        return currentYear == endDateTime.getYear() ? endDateTime
                : new DateTime(currentYear, 12, 31, 23, 59, 59);
    }

}

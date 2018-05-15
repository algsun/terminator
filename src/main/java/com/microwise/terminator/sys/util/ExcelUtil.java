package com.microwise.terminator.sys.util;

import com.microwise.terminator.sys.entity.LocationDataVO;
import com.microwise.terminator.sys.entity.RecentDataVO;
import com.microwise.terminator.sys.entity.Sensorinfo;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.List;
import java.util.Map;

/**
 * 使用poi技术生成excel报表的工具类
 *
 * @author xubaoji
 * @date 2013-2-4
 */
public class ExcelUtil {

    /**
     * excel 文件列宽常量1
     */
    public static final int COLUMN_WIDTH_ONE = 4000;

    /**
     * excel 文件列宽常量2
     */
    public static final int COLUMN_WIDTH_TWO = 6000;

    /**
     * excel 文件标题行高常量
     */
    public static final int TITLE_ROW_HEIGHE = 40;

    /**
     * excel 文件列头行高常量
     */
    public static final int HEAD_ROW_HEIGHE = 35;

    /**
     * excel 文件数据行高常量
     */
    public static final int DATA_ROW_HEIGHE = 20;

    /**
     * 标题行行下标常量
     */
    public static final int TITLE_ROW_INDEX = 0;

    /**
     * 标题列下标常量
     */
    public static final int TITLE_CELL_INDEX = 0;

    /**
     * 列头行行下标常量
     */
    public static final int HEAD_ROW_INDEX = 1;

    /**
     * 字体大小常量
     */
    private static final short BODY_SIZE = 12;

    /**
     * excel 文件工作薄
     */
    private Workbook workbook;
    private CellStyle numericCellStyle;
    private CellStyle dateCellStyle;
    private CellStyle defaultCellStyle;

    /**
     * 组装位置点sheet
     *
     * @param sensorinfoList  监测指标（用于sheet的列头显示）
     * @param historyDataList 历史数据列表（用于sheet数据）
     * @param sheet           要组装的sheet
     * @author 许月希
     * @date 2014-7-8
     */
    public void assembleLocationSheet(List<Sensorinfo> sensorinfoList,
                                      List<RecentDataVO> historyDataList, Sheet sheet, String sheetTitle) {
        // 给全局变量workbook赋值用于创建样式和字体
        workbook = sheet.getWorkbook();
        numericCellStyle = null;
        dateCellStyle = null;
        dateCellStyle = null;
        Row titleRow = sheet.createRow(TITLE_ROW_INDEX);
        assembleTitleRow(titleRow, sensorinfoList, sheetTitle);

        Row headRow = sheet.createRow(HEAD_ROW_INDEX);
        assembleLocationHeadRow(headRow, sensorinfoList);

        // 组装数据
        if (historyDataList != null && historyDataList.size() != 0) {
            for (int i = 0; i < historyDataList.size(); i++) {
                Row dataRow = sheet.createRow(i + 2);
                dataRow.setHeightInPoints(DATA_ROW_HEIGHE);
                RecentDataVO historyData = historyDataList.get(i);
                assembleDataRow(dataRow, sensorinfoList, historyData);
            }
        }
    }

    /**
     * 组装sheet标题
     *
     * @param titleRow   标题行
     * @param titleValue 标题内容
     * @author 许保吉
     * @date 2013-2-21
     * @deprecated
     */
    private void assembleTitleRow(Row titleRow,
                                  List<Sensorinfo> sensorinfoList, String titleValue) {
        titleRow.getSheet().autoSizeColumn(TITLE_CELL_INDEX);
        titleRow.setHeightInPoints(TITLE_ROW_HEIGHE);
        CellRangeAddress region = new CellRangeAddress(TITLE_ROW_INDEX,
                TITLE_ROW_INDEX, TITLE_CELL_INDEX, sensorinfoList.size() + 1);
        titleRow.getSheet().addMergedRegion(region);

        Cell titleCell = titleRow.createCell(TITLE_CELL_INDEX);
        titleCell.setCellValue(titleValue);
        titleCell.setCellStyle(createTitleStyle());
    }

    /**
     * 组装 sheet 文件列头行
     *
     * @param headRow        列头行
     * @param sensorinfoList 列头数据
     * @author 许月希
     * @date 2014-7-7
     */
    private void assembleLocationHeadRow(Row headRow,
                                         List<Sensorinfo> sensorinfoList) {
        headRow.setHeightInPoints(HEAD_ROW_HEIGHE);
        Cell cell = createCell(headRow, 0);
        cell.setCellValue("设备");
        cell.setCellStyle(createDateCellStyle());
        if (sensorinfoList != null && sensorinfoList.size() != 0) {
            for (int i = 1; i < sensorinfoList.size() + 1; i++) {
                Sensorinfo sensorinfo = sensorinfoList.get(i - 1);
                Cell cellTemp = createCell(headRow, i);
                cellTemp.setCellValue(sensorinfo.getCnName() + "("
                        + sensorinfo.getUnits() + ")");
                cellTemp.setCellStyle(createDefaultCellStyle());
                /* headRow.getSheet().setColumnWidth(i, COLUMN_WIDTH_ONE); */
                // 设置自动列宽
                headRow.getSheet().autoSizeColumn(i);

            }
        }

        Cell cell2 = createCell(headRow, sensorinfoList.size() + 1);
        cell2.setCellValue("状态");
        cell2.setCellStyle(createDateCellStyle());
        headRow.getSheet().setColumnWidth(sensorinfoList.size() + 1,
                COLUMN_WIDTH_ONE);
        Cell cell0 = createCell(headRow, sensorinfoList.size() + 2);
        cell0.setCellValue("电压(V)");
        cell0.setCellStyle(createDateCellStyle());
        // 设置固定列宽
        headRow.getSheet().setColumnWidth(sensorinfoList.size() + 2,
                COLUMN_WIDTH_ONE);

        Cell cell1 = createCell(headRow, sensorinfoList.size() + 3);
        cell1.setCellStyle(createDateCellStyle());
        cell1.setCellValue("时间");
        // 设置固定列宽
        headRow.getSheet().setColumnWidth(sensorinfoList.size() + 3,
                COLUMN_WIDTH_TWO);
    }


    /**
     * 位置点数据组装excel 文件数据行
     *
     * @param dataRow        数据行
     * @param sensorinfoList 列头信息用于取对应的map集合中数据
     * @param historyData    一行数据信息
     * @author 许月希
     * @date 2014-7-8
     */
    private void assembleDataRow(Row dataRow,
                                 List<Sensorinfo> sensorinfoList, RecentDataVO historyData) {
        Cell cell2 = createCell(dataRow, 0);
        String deviceId = historyData.getDeviceId();
        cell2.setCellValue(deviceId.substring(8));
        cell2.setCellStyle(createDateCellStyle());
        Map<Integer, LocationDataVO> locationDateMap = historyData
                .getSensorInfoMap();
        // 监测指标数据
        for (int i = 1; i < sensorinfoList.size() + 1; i++) {
            Sensorinfo sensorinfo = sensorinfoList.get(i - 1);
            LocationDataVO deviceData = locationDateMap.get(sensorinfo.getSensorphysicalid());
            Cell cellTemp = createCell(dataRow, i);
            if (deviceData != null && deviceData.getState() == 1) {
                if (sensorinfo.getShowtype() != 1) {
                    if(sensorinfo.getShowtype() ==3 ) {
                       //String value = "0".equals(deviceData.getSensorPhysicalValue())  ? "关" : "开";
                        Integer value = Integer.parseInt(deviceData.getSensorPhysicalValue());
                        cellTemp.setCellValue(value);
                    } else {
                        Double value = Double.parseDouble(deviceData.getSensorPhysicalValue());
                        cellTemp.setCellValue(value);
                    }
                } else {
                    cellTemp.setCellValue(deviceData.getSensorPhysicalValue());
                }
            } else {
                cellTemp.setCellValue("--");
            }
            cellTemp.setCellStyle(createDefaultCellStyle());
        }
        Cell cell5 = createCell(dataRow, sensorinfoList.size() + 1);
        cell5.setCellStyle(createDefaultCellStyle());
        cell5.setCellValue(disposeAnomalyToString(historyData.getAnomaly()));

        // 电压数据
        Cell cell3 = createCell(dataRow, sensorinfoList.size() + 2);
        cell3.setCellStyle(createNumericCellStyle());
        if (historyData.getLowvoltage() != -1) {
            cell3.setCellValue(historyData.getLowvoltage());
        } else {
            cell3.setCellValue("--");
        }

        // 时间数据
        Cell cell4 = createCell(dataRow, sensorinfoList.size() + 3);
        cell4.setCellStyle(createDateCellStyle());
        cell4.setCellValue(historyData.getStamp());
    }

    /**
     * 将 设备状态 字段转换成字符串
     *
     * @param anomaly
     * @return String
     * @author xu.baoji
     * @date 2013-6-25
     */
    private static String disposeAnomalyToString(int anomaly) {
        String stringAnomaly = null;
        switch (anomaly) {
            case 0:
                stringAnomaly = "正常";
                break;
            case 1:
                stringAnomaly = "低压";
                break;
            case 2:
                stringAnomaly = "掉电";
                break;
        }
        return stringAnomaly;
    }

    /**
     * 创建cell 并设置默认格式
     *
     * @param row 行
     * @param i   cell 所在位置
     * @author 许保吉
     * @date 2013-2-6
     */
    private static Cell createCell(Row row, int i) {
        Cell cell = row.createCell(i);
        return cell;
    }

    /**
     * 创建cell样式
     *
     * @author 许保吉
     * @date 2013-2-6
     */
    private CellStyle createNumericCellStyle() {
        if (numericCellStyle == null) {
            numericCellStyle = workbook.createCellStyle();// 设置边框
            numericCellStyle.setBorderBottom(CellStyle.BORDER_THIN); // 下边框
            numericCellStyle.setBorderLeft(CellStyle.BORDER_THIN);// 左边框
            numericCellStyle.setBorderTop(CellStyle.BORDER_THIN);// 上边框
            numericCellStyle.setBorderRight(CellStyle.BORDER_THIN);// 右边框

            // 设置文字居中
            numericCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
            numericCellStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0.0"));

            // 设置字体样式
            numericCellStyle.setFont(createFont());

            numericCellStyle.setLocked(false);
        }
        return numericCellStyle;
    }

    /**
     * 创建cell样式
     *
     * @author 许保吉
     * @date 2013-2-6
     */
    private CellStyle createDefaultCellStyle() {
        if (defaultCellStyle == null) {
            defaultCellStyle = workbook.createCellStyle();// 设置边框
            defaultCellStyle.setBorderBottom(CellStyle.BORDER_THIN); // 下边框
            defaultCellStyle.setBorderLeft(CellStyle.BORDER_THIN);// 左边框
            defaultCellStyle.setBorderTop(CellStyle.BORDER_THIN);// 上边框
            defaultCellStyle.setBorderRight(CellStyle.BORDER_THIN);// 右边框
            defaultCellStyle.setWrapText(true);

            // 设置文字居中
            defaultCellStyle.setAlignment(CellStyle.ALIGN_CENTER);

            // 设置字体样式
            defaultCellStyle.setFont(createFont());

            defaultCellStyle.setLocked(false);
        }
        return defaultCellStyle;
    }

    private CellStyle createDateCellStyle() {
        if (dateCellStyle == null) {
            dateCellStyle = workbook.createCellStyle();

            // 设置边框
            dateCellStyle.setBorderBottom(CellStyle.BORDER_THIN); // 下边框
            dateCellStyle.setBorderLeft(CellStyle.BORDER_THIN);// 左边框
            dateCellStyle.setBorderTop(CellStyle.BORDER_THIN);// 上边框
            dateCellStyle.setBorderRight(CellStyle.BORDER_THIN);// 右边框

            // 设置文字居中
            dateCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
            dateCellStyle.setDataFormat(workbook.createDataFormat().getFormat("yyyy-MM-dd hh:mm:ss"));

            // 设置字体样式
            dateCellStyle.setFont(createFont());

            dateCellStyle.setLocked(false);
        }
        return dateCellStyle;
    }

    /**
     * 创建标题样式
     *
     * @author 许保吉
     * @date 2013-2-6
     */
    private CellStyle createTitleStyle() {
        CellStyle cellStyle = workbook.createCellStyle();

        // 设置文字居中
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);

        // 设置标题自动换行
        cellStyle.setWrapText(true);

        // 设置字体样式
        cellStyle.setFont(createTitleFont());

        cellStyle.setLocked(false);
        return cellStyle;
    }

    /**
     * 创建字体样式
     *
     * @author 许保吉
     * @date 2013-2-6
     */
    private Font createFont() {
        Font font = workbook.createFont();
        font.setFontName("华文楷体");
        font.setFontHeightInPoints(BODY_SIZE);
        return font;
    }

    /**
     * 创建sheet标题字体样式
     *
     * @author 许保吉
     * @date 2013-2-6
     */
    private Font createTitleFont() {
        Font font = workbook.createFont();
        font.setFontName("华文楷体");
        font.setFontHeightInPoints(BODY_SIZE);
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        return font;
    }
}

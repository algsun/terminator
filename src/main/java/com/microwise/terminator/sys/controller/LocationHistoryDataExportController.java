package com.microwise.terminator.sys.controller;

import com.microwise.terminator.sys.service.LocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.util.Date;

/**
 * 位置点历史数据导出controller
 *
 * @author bai.weixing
 * @since 2017/10/10.
 */
@RequestMapping("/locationHistoryDataExport")
@Controller
public class LocationHistoryDataExportController {

    @Autowired
    private LocationService locationService;

    private static final Logger logger = LoggerFactory.getLogger(LocationHistoryDataExportController.class);

    @GetMapping
    public String export(@RequestParam String locationId, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date beginTime,
                         @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime, HttpServletResponse res) throws IOException {
        String encoding = "ISO8859-1";
        try {
            String fileName = locationService.getFileName(locationId, beginTime, endTime);
            fileName = new String(fileName.getBytes(), encoding);
            res.setHeader("Content-disposition", "attachment;filename=\"" + fileName + "\"");
            res.setContentType("application/octet-stream;charset=" + encoding);
        } catch (UnsupportedEncodingException e) {
            logger.error("历史数据导出", e);
        }
        OutputStream out = res.getOutputStream();
        try {
            locationService.exportHistoryData(locationId, beginTime, endTime, out);
        } catch (SocketException ie) {
            logger.error("历史数据导出时，客户端操作异常", ie);
        } catch (IOException se) {
            logger.error("历史数据导出时，客户端操作异常", se);
        } catch (Exception e) {
            logger.error("历史数据导出", e);
        }finally {
            out.flush();
            out.close();
        }
        return null;
    }
}

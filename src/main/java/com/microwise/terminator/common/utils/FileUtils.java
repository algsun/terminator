package com.microwise.terminator.common.utils;

import com.google.common.io.ByteStreams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 通用上传下载工具
 *
 * @author: baiweixing
 * @since 2017-03-28
 */
public class FileUtils {

    public static Logger logger = LoggerFactory.getLogger(FileUtils.class);

    /**
     * 文件下载
     *
     * @param url 文件名
     * @return true 下载成功
     * false 下载失败
     */
    public static boolean fileDownLoad(String url, HttpServletResponse response) {
        OutputStream outputStream = null;
        InputStream inputStream = null;
        try {

            File file = new File(url);
            Long filelength = file.length();
            String fileName = fileNameToNoDate(url.substring(url.lastIndexOf(File.separator) + 1, url.length()));
            response.setHeader("Location", fileName);
            int cacheTime = 10;
            response.setHeader("Cache-Control", "max-age=" + cacheTime);
            response.setContentType("application/octet-stream; charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));
            response.setContentLength(filelength.intValue());
            outputStream = response.getOutputStream();
            inputStream = new FileInputStream(file);
            ByteStreams.copy(inputStream, outputStream);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param file       上传文件
     * @param uploadPath 上传文件目的路径
     * @throws Exception
     */
    public static void fileUpload(MultipartFile file, String uploadPath) throws Exception {
        byte[] bytes = file.getBytes();
        File f = new File(uploadPath);
        if (!f.exists()) {
            f.getParentFile().mkdirs();
        }
        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(uploadPath)));
        stream.write(bytes);
        stream.flush();
        stream.close();
    }

    /**
     * 删除上传文件
     *
     * @param filePath 上传文件路径
     */
    public static boolean deleteFile(String filePath) {

        try {
            filePath = getServerResourcesPath(filePath);
            File file = new File(filePath);
            if (file.exists() && file.isFile()) {
                file.delete();
            }
            logger.info("文件删除成功");
        } catch (Exception e) {
            logger.error("文件删除失败", e);
            return false;
        }


        return true;
    }

    /**
     * 处理上传文件
     *
     * @param srcUploadFile 文件
     * @return 文件名
     */
    public static String doFile(MultipartFile srcUploadFile, String path) {
        String returnfileName;
        String fileName = srcUploadFile.getOriginalFilename();
        fileName = fileName.substring(0, fileName.lastIndexOf(".")) + "[" + System.currentTimeMillis() + "]" + fileName.substring(fileName.lastIndexOf("."), fileName.length());
        String filePath = getServerResourcesPath(path);
        boolean flag = uploadFile(fileName, filePath, srcUploadFile);
        if (flag) {
            returnfileName = fileName;
        } else {
            returnfileName = null;
        }

        return returnfileName;
    }

    /**
     * 获取资源url
     *
     * @param request request
     * @return 资源url
     */
    public static String getServerResourcesUrl(HttpServletRequest request) {
        //  获取服务器地址
        return request.getScheme() + "://"
                + request.getServerName() + ":" + request.getServerPort() + "/terminatorResources/";
    }

    /**
     * 获取资源路径
     *
     * @param
     */
    public static String getServerResourcesPath(String path) {
        String filePath = System.getProperty("catalina.home") + File.separator + "webapps" + File.separator + "terminatorResources" + File.separator + path;
        return filePath;
    }


    /**
     * 去掉文件名的日期
     *
     * @param fileName 原始文件名
     * @return
     */
    public static String fileNameToNoDate(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf("[")) + fileName.substring(fileName.lastIndexOf("."), fileName.length());
    }

    /**
     * 文件名拼接时间后缀
     *
     * @param fileName
     * @return
     */
    public static String fileNameFormart(String fileName) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH mm ss");
        fileName = fileName.substring(0, fileName.lastIndexOf(".")) + "[" + dateFormat.format(new Date()) + "]" + fileName.substring(fileName.lastIndexOf("."), fileName.length());
        return fileName;
    }

    /**
     * 上传文件
     *
     * @param fileName 文件名称
     * @param filePath 文件路径
     * @param file     文件
     * @return 上传是否成功
     */
    public static boolean uploadFile(String fileName, String filePath, MultipartFile file) {
        if (file.isEmpty()) {
            return false;
        }
        File dest = new File(filePath + fileName);
        // 检测是否存在目录
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
            return true;
        } catch (IllegalStateException e) {
            logger.error("上传文件失败:" + dest, e);
        } catch (IOException e) {
            logger.error("上传文件失败:" + dest, e);
        }
        return false;
    }
}




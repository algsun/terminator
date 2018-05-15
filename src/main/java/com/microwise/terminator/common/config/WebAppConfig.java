package com.microwise.terminator.common.config;

import ch.qos.logback.core.util.FileUtil;
import com.microwise.terminator.common.utils.FileUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author bai.weixing
 * @date 2017/09/25
 */
@Configuration
public class WebAppConfig extends WebMvcConfigurerAdapter {
    //获取配置文件中图片的路径
    @Value("${terminator.imagesPath}")
    private String imagesPath;

    //访问图片方法
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /*if (mImagesPath.equals("") || mImagesPath.equals("${cbs.imagesPath}")) {
            String imagesPath = WebAppConfig.class.getClassLoader().getResource("").getPath();
            if (imagesPath.indexOf(".jar") > 0) {
                imagesPath = imagesPath.substring(0, imagesPath.indexOf(".jar"));
            } else if (imagesPath.indexOf("classes") > 0) {
                imagesPath = "file:" + imagesPath.substring(0, imagesPath.indexOf("classes"));
            }
            imagesPath = imagesPath.substring(0, imagesPath.lastIndexOf("/")) + "/image/";
            mImagesPath = imagesPath;
        }*/
        imagesPath ="file:"+FileUtils.getServerResourcesPath(imagesPath);
        LoggerFactory.getLogger(WebAppConfig.class).info("imagesPath=" + imagesPath);
        registry.addResourceHandler("/images/**").addResourceLocations(imagesPath);
        super.addResourceHandlers(registry);
    }
}

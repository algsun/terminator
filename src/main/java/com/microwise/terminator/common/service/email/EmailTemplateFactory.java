package com.microwise.terminator.common.service.email;

import com.google.common.io.CharStreams;
import com.google.common.io.Closeables;
import com.google.common.io.Resources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * EmailTemplate文件加载类，可公用
 *
 * @author zhangpeng
 * @date 2012-11-15
 * @check 2012-11-27 xubaoji svn:381
 */
@Component
public class EmailTemplateFactory {

    @Autowired
    private TemplateEngine templateEngine;

    // 模版池
    private Map<String, EmailTemplate> templatePool = new HashMap<String, EmailTemplate>();

    // 模版工厂,负责生成创建邮件模版对象
    private static EmailTemplateFactory templateFactory = new EmailTemplateFactory();

    private EmailTemplateFactory() {
    }

    public synchronized static EmailTemplateFactory getInstance() {
        return templateFactory;
    }

    /**
     * 获取模版文件
     *
     * @param key 模版文件路径常量
     * @return EmailTemplate 模版对象
     */
    public EmailTemplate getEmailTemplate(String key) {
        EmailTemplate emailTemplate = null;
        if (templatePool.containsKey(key)) {
            emailTemplate = templatePool.get(key);
        } else {
            // 获得新模版
            emailTemplate = new EmailTemplate(key);
            // 放入模版池
            templatePool.put(key, emailTemplate);
        }
        return emailTemplate;
    }

    /**
     * 获得已创建的模版文件数量
     *
     * @return int 已创建模版文件数量
     */
    public int getEmailTemplateTotal() {
        return templatePool.size();
    }

    /**
     * 模版文件加载类
     *
     * @author zhangpeng
     * @date 2012-11-12
     */
    public class EmailTemplate {

        /**
         * 模版对象
         */
        private String template;

        /**
         * 构造函数
         *
         * @param emailTemplate 模版文件路径常量
         */
        private EmailTemplate(String emailTemplate) {
            URL url = Resources.getResource(emailTemplate);
            File file;
            try {
                file = new File(URLDecoder.decode(url.getFile(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                throw new IllegalStateException(e);
            }

            Reader reader = null;
            try {
                reader = new InputStreamReader(new FileInputStream(file),
                        "utf-8");
            } catch (FileNotFoundException e) {
                throw new IllegalStateException(e);
            } catch (UnsupportedEncodingException e) {
                throw new IllegalStateException(e);
            }

            try {
                template = CharStreams.toString(reader);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
            Closeables.closeQuietly(reader);
        }

        /**
         * 获取模版文件
         *
         * @param args 占位符数据
         * @return 格式化后的邮件模版
         */
        public String mergeEmailTemplate(String... args) {
            return MessageFormat.format(template, args);
        }

        /**
         * 由邮件模板生成邮件
         *
         * @param key     模板路径
         * @param context 给模板填充的数据
         * @return 格式化后的邮件模板
         */
        public String build(String key, Context context) {
            return templateEngine.process(key, context);
        }
    }
}
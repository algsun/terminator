package com.microwise.terminator.common.service.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

/**
 * 发送邮件服务
 *
 * @author sun.cong
 * @date 2017-11-24
 */
@Service
public class EmailService {

    @Value("${smtp.from}")
    private static String smtpFrom;
    @Value("${smtp.author}")
    private static String smtpAuthor;

    /**
     * 邮件发送对象
     */
    @Autowired
    private static MailSender mailSender;

    /**
     * 批量发送邮件对象
     */
    @Autowired
    private static MailSender batchMailSender;


    /**
     * 通过spring发送邮件
     *
     * @param recipient 目标email地址
     * @param title     邮件标题
     * @param emailText 邮件正文
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     * @author zhangpeng
     * @date 2012-10-10
     */
    public static void sendEmail(String recipient, String title,
                                 String emailText) throws MessagingException,
            UnsupportedEncodingException {
        MimeMessage mimeMessage = ((JavaMailSender) mailSender).createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        helper.setFrom(smtpFrom, smtpAuthor);
        helper.setTo(recipient);
        helper.setSubject(title);
        helper.setText(emailText, true);
        ((JavaMailSender) mailSender).send(mimeMessage);
    }

    /**
     * 通过spring 批量发送邮件
     *
     * @param recipient 目标email地址
     * @param title     邮件标题
     * @param emailText 邮件正文
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     * @author zhangpeng
     * @date 2012-10-10
     */
    public static void sendBatchEmail(String title, String emailText,
                                      String... recipient) throws MessagingException,
            UnsupportedEncodingException {
        MimeMessage mimeMessage = ((JavaMailSender) batchMailSender).createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        helper.setFrom(smtpFrom, smtpAuthor);
        helper.setTo(recipient);
        helper.setSubject(title);
        helper.setText(emailText, true);
        ((JavaMailSender) batchMailSender).send(mimeMessage);
    }

    //TODO 这个set目测不需要静态的，结合spring里的配置看一下
    public static void setBatchMailSender(MailSender batchMailSender) {
        EmailService.batchMailSender = batchMailSender;
    }
}

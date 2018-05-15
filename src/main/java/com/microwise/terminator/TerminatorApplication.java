package com.microwise.terminator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@ServletComponentScan
public class TerminatorApplication {

    @Autowired
    private RestTemplateBuilder builder;

    @Value("${smtp.host}")
    private String host;
    @Value("${smtp.port}")
    private Integer port;
    @Value("${smtp.username}")
    private String userName;
    @Value("${smtp.password}")
    private String passWord;

    // 使用RestTemplateBuilder来实例化RestTemplate对象，spring默认已经注入了RestTemplateBuilder实例
    @Bean
    public RestTemplate restTemplate() {
        return builder.requestFactory(new HttpComponentsClientHttpRequestFactory()).build();
    }

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(host);
        javaMailSender.setPort(port);
        javaMailSender.setUsername(userName);
        javaMailSender.setPassword(passWord);
        return javaMailSender;
    }

    public static void main(String[] args) {
        SpringApplication.run(TerminatorApplication.class, args);
    }
}

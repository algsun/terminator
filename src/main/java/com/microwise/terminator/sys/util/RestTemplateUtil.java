package com.microwise.terminator.sys.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microwise.terminator.sys.entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

/**
 * RestTemplate工具
 *
 * @author bai.weixing
 * @since 2017/11/9.
 */
@Component
public class RestTemplateUtil {

    @Autowired
    private RestTemplate restTemplate;

    private final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();


    /**
     * 查询
     *
     * @param url
     * @return
     */
    public Result find(String url) {
        return this.exchange(url, HttpMethod.GET, Result.class);
    }

    /**
     * 保存或更新object
     *
     * @param object
     * @param url
     * @return
     */
    public Result execute(Object object, String url) {
        return restTemplate.postForObject(url, object, Result.class);
    }


    /**
     * 删除
     *
     * @param url
     * @return
     */
    public Result delete(String url) {
        return this.exchange(url, HttpMethod.DELETE, Result.class);
    }

    /**
     * 发送/获取 服务端数据(主要用于解决发送put,delete方法无返回值问题).
     *
     * @param url      绝对地址
     * @param method   请求方式
     * @param bodyType 返回类型
     * @param <T>      返回类型
     * @return 返回结果(响应体)
     */
    private <T> T exchange(String url, HttpMethod method, Class<T> bodyType) {
        // 请求头
        HttpHeaders headers = new HttpHeaders();
        MimeType mimeType = MimeTypeUtils.parseMimeType("application/json");
        MediaType mediaType = new MediaType(mimeType.getType(), mimeType.getSubtype(), Charset.forName("UTF-8"));
        // 请求体
        headers.setContentType(mediaType);
        //提供json转化功能
        ObjectMapper mapper = new ObjectMapper();
        String str = null;
        try {
            if (!params.isEmpty()) {
                str = mapper.writeValueAsString(params);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        // 发送请求
        HttpEntity<String> entity = new HttpEntity<>(str, headers);
        ResponseEntity<T> resultEntity = restTemplate.exchange(url, method, entity, bodyType);
        return resultEntity.getBody();
    }
}

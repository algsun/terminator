package com.microwise.terminator.sys.util;


import com.google.common.io.CharStreams;
import com.google.common.io.Closeables;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component
public class BPHttpApiClient extends HttpApiClient {

    private HttpClient httpClient = new DefaultHttpClient();

    @Value("${url}")
    private String url;

    /**
     * 通知中间件位置点操作
     *
     * @param deviceId 设备编号
     * @return
     */
    public Map<String, Object> notifyLocationChanged(String deviceId) throws Exception {
        HttpGet get = new HttpGet(url + "struts/devices/" + deviceId + "/reload-device-cache");
        JSONObject result = requestAndParse(get);
        Map<String, Object> map = new HashMap<>();
        map.put("success", result.getBoolean("success"));
        return map;
    }

    /**
     * 返回执行请求的 HttpClient 对象
     *
     * @return
     */
    @Override
    public HttpClient getHttpClient() {
        return httpClient;
    }

    /**
     * 修改设备工作周期
     *
     * @param deviceId
     * @param interval
     * @return
     */
    public Boolean modifyInterval(String deviceId, int interval) throws Exception {
        HttpGet get = new HttpGet(url + "struts/devices/modify_interval?deviceId=" + deviceId + "&interval=" + interval);
        return requestAndParse(get).getBoolean("sendSuccess");
    }

    public Boolean modifySensitivity(String deviceId, int sensitivity) throws Exception {
        HttpGet get = new HttpGet(url + "struts/devices/sensitivity?deviceId=" + deviceId + "&level=" + sensitivity);
        return requestAndParse(get).getBoolean("sendSuccess");
    }


    /**
     * 删除设备数据表
     *
     * @param deviceId
     */
    public Boolean deleteDevice(String deviceId) throws Exception {
        HttpGet get = new HttpGet(url + "struts/deleteDevice?deviceId=" + deviceId);
        return request(get).getBoolean("success");
    }

    private JSONObject request(HttpGet get) throws IOException,
            JSONException {
        InputStream input = null;
        try {
            HttpResponse response = new DefaultHttpClient().execute(get);
            input = response.getEntity().getContent();
            InputStreamReader reader = new InputStreamReader(response
                    .getEntity().getContent(), "utf-8");
            return new JSONObject(CharStreams.toString(reader));
        } finally {
            Closeables.close(input, true);
        }
    }
}

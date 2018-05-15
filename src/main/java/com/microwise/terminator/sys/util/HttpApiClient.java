package com.microwise.terminator.sys.util;

import com.google.common.base.Strings;
import com.google.common.io.CharStreams;
import com.google.common.io.Closeables;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 提供 HTTP 本地接口支持
 *
 * @author gaohui
 * @date 13-4-26 09:03
 * @check wanggeng 13-05-06 #3075
 */
public abstract class HttpApiClient {

    /**
     * 发起 get 请求，并以 json object 的方式解析响应内容
     *
     * @param get
     * @return
     * @throws IOException
     * @throws JSONException
     */
    protected JSONObject requestAndParse(HttpGet get) throws IOException,
            JSONException {
        InputStream input = null;
        try {
            HttpResponse response = getHttpClient().execute(get);
            input = response.getEntity().getContent();
            InputStreamReader reader = new InputStreamReader(response
                    .getEntity().getContent(), "utf-8");
            String resultText = CharStreams.toString(reader);
            return new JSONObject(resultText);
        } finally {
            Closeables.close(input, true);
        }
    }

    /**
     * 发起 get 请求，并以 json string
     *
     * @param get
     * @return
     * @throws IOException
     * @throws JSONException
     */
    protected String get(HttpGet get) throws IOException,
            JSONException {
        InputStream input = null;
        try {
            HttpResponse response = getHttpClient().execute(get);
            input = response.getEntity().getContent();
            InputStreamReader reader = new InputStreamReader(response
                    .getEntity().getContent(), "UTF-8");
            return CharStreams.toString(reader);
        } finally {
            Closeables.close(input, true);
        }
    }

    /**
     * 发起 put 请求，并以 json object 的方式解析响应内容
     *
     * @param put
     * @return
     * @throws IOException
     * @throws JSONException
     */
    protected JSONObject requestAndParse(HttpPut put) throws IOException,
            JSONException {
        InputStream input = null;
        try {
            HttpResponse response = getHttpClient().execute(put);
            input = response.getEntity().getContent();
            response.addHeader("Content-type", "application/x-www-form-urlencoded");
            InputStreamReader reader = new InputStreamReader(response
                    .getEntity().getContent(), "utf-8");
            String resultText = CharStreams.toString(reader);
            return new JSONObject(resultText);
        } finally {
            Closeables.close(input, true);
        }
    }

    public JSONObject post(String url, String jsonStr) throws IOException, JSONException {
        HttpPost post = null;
        InputStream input = null;
        try {
            if (!Strings.isNullOrEmpty(jsonStr)) {
                post = new HttpPost(url);
                StringEntity s = new StringEntity(jsonStr, "UTF-8");
                s.setContentEncoding("UTF-8");
                s.setContentType("application/json");
                post.setEntity(s);
            }

            HttpResponse res = getHttpClient().execute(post);
            input = res.getEntity().getContent();
            InputStreamReader reader = new InputStreamReader(res
                    .getEntity().getContent(), "UTF-8");
            String result = CharStreams.toString(reader);
            return new JSONObject(result);
        } finally {
            Closeables.close(input, true);
        }
    }

    public JSONObject post(String url, String key, String value) throws IOException, JSONException {
        // 建立参数列表
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(key, value));
        HttpPost post = null;
        InputStream input = null;
        try {
            post = new HttpPost(url);
            UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(params, "UTF-8");
            post.setHeader("Content-Type",
                    "application/x-www-form-urlencoded");
            post.setEntity(uefEntity);

            HttpResponse res = getHttpClient().execute(post);
            input = res.getEntity().getContent();
            InputStreamReader reader = new InputStreamReader(res
                    .getEntity().getContent(), "utf-8");
            String resultText = CharStreams.toString(reader);
            return new JSONObject(resultText);
        } finally {
            if (post != null) {
                post.releaseConnection();
            }
            Closeables.close(input, true);
        }
    }


    /**
     * 返回执行请求的 HttpClient 对象
     *
     * @return
     */
    public abstract HttpClient getHttpClient();
}

package xyz.kwin.yapi.util;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import xyz.kwin.yapi.exceptions.HttpException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * http请求类
 *
 * @author kwin
 * @since 2021/12/13 5:04 下午
 */
public class HttpUtil {

    private static final CloseableHttpClient httpClient;
    private static final RequestConfig requestConfig;

    static {
        httpClient = HttpClients.createDefault();
        requestConfig = RequestConfig.custom()
                .setConnectTimeout(3000)
                .setConnectionRequestTimeout(3000)
                .setSocketTimeout(3000)
                .build();
    }

    /**
     * 执行uri请求
     *
     * @param request uri请求
     * @return 返回结果
     */
    public static String execute(HttpUriRequest request) {
        try (CloseableHttpResponse httpResponse = httpClient.execute(request)) {
            HttpEntity entity = httpResponse.getEntity();
            if (entity == null) {
                throw new HttpException(HttpException.HTTP_ENTITY_EMPTY);
            }
            return EntityUtils.toString(entity);
        } catch (Exception e) {
            throw new HttpException();
        }
    }

    /**
     * 获取httpGet请求
     *
     * @param url url
     * @return httpGet
     */
    public static HttpGet httpGet(String url) {
        return httpGet(url, null, null);
    }

    /**
     * 获取httpGet请求
     *
     * @param url    url
     * @param params 参数
     * @return httpGet
     */
    public static HttpGet httpGet(String url, Map<String, Object> params) {
        return httpGet(url, params, null);
    }

    /**
     * 获取httpGet请求
     *
     * @param url     url
     * @param params  参数
     * @param headers 请求头
     * @return httpGet
     */
    public static HttpGet httpGet(String url, Map<String, Object> params, Map<String, String> headers) {
        url += toRequestStr(params);
        HttpGet httpGet = new HttpGet(url);

        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> headerEntry : headers.entrySet()) {
                httpGet.setHeader(headerEntry.getKey(), headerEntry.getValue());
            }
        }

        httpGet.setConfig(requestConfig);
        return httpGet;
    }

    /**
     * 获取httpPost请求
     *
     * @param url url
     * @return httpPost
     */
    public static HttpPost httpPost(String url) {
        return httpPost(url, (String) null, Collections.emptyMap());
    }

    /**
     * 获取httpPost请求
     *
     * @param url    url
     * @param params 参数
     * @return httpPost
     */
    public static HttpPost httpPost(String url, Map<String, Object> params) {
        return httpPost(url, params, null);
    }

    /**
     * 获取httpPost请求
     *
     * @param url     url
     * @param params  参数
     * @param headers 请求头
     * @return httpPost
     */
    public static HttpPost httpPost(String url, Map<String, Object> params, Map<String, String> headers) {
        try {
            HttpPost httpPost = new HttpPost(url);

            if (headers != null && !headers.isEmpty()) {
                for (Map.Entry<String, String> headerEntry : headers.entrySet()) {
                    httpPost.setHeader(headerEntry.getKey(), headerEntry.getValue());
                }
            }

            if (params == null || params.isEmpty()) {
                return httpPost;
            }
            List<NameValuePair> paramList = new ArrayList<>();
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                String parameterName = entry.getKey();
                Object parameterValue = entry.getValue();
                paramList.add(new BasicNameValuePair(parameterName, String
                        .valueOf(parameterValue)));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(paramList));

            httpPost.setConfig(requestConfig);
            return httpPost;
        } catch (UnsupportedEncodingException e) {
            throw new HttpException();
        }
    }

    /**
     * 获取httpPost请求
     *
     * @param url  url
     * @param body body
     * @return httpPost
     */
    public static HttpPost httpPost(String url, String body) {
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-type", "application/json;charset=utf-8");
            HttpEntity reqEntity = new StringEntity(body == null ? "" : body, "UTF-8");
            httpPost.setEntity(reqEntity);
            httpPost.setConfig(requestConfig);
            return httpPost;
        } catch (Exception e) {
            throw new HttpException();
        }
    }

    /**
     * 获取httpPost请求
     *
     * @param url     url
     * @param body    body
     * @param headers 请求头
     * @return httpPost
     */
    public static HttpPost httpPost(String url, String body, Map<String, String> headers) {
        try {
            HttpPost httpPost = new HttpPost(url);

            if (headers != null && !headers.isEmpty()) {
                for (Map.Entry<String, String> headerEntry : headers.entrySet()) {
                    httpPost.setHeader(headerEntry.getKey(), headerEntry.getValue());
                }
            }

            HttpEntity reqEntity = new StringEntity(body == null ? "" : body, "UTF-8");
            httpPost.setEntity(reqEntity);
            httpPost.setConfig(requestConfig);
            return httpPost;
        } catch (Exception e) {
            throw new HttpException();
        }
    }

    private static String toRequestStr(Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            return "";
        }
        List<String> paramList = new ArrayList<>();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            paramList.add(entry.getKey() + "=" + entry.getValue());
        }
        return "?" + String.join("&", paramList);
    }

}

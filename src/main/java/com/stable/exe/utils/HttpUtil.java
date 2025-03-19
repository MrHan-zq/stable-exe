package com.stable.exe.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


@Slf4j
public class HttpUtil {

    private static final Integer TIME_OUT = 3000;

    public static JSONObject httpPost(Map<String, String> header, String url, String data)
            throws IOException {
        String applicationType = "application/json";
        String charset = "UTF-8";
        JSONObject jsonRet = null;
        CloseableHttpResponse httpResponse;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        try {
            setHeader(header, post);
            if (StringUtils.isNotBlank(data)) {
                StringEntity stringEntity = new StringEntity(data, charset);
                stringEntity.setContentEncoding(charset);
                stringEntity.setContentType(applicationType);
                post.setEntity(stringEntity);
            }
            httpResponse = httpClient.execute(post);
            jsonRet = CvtHttpResp2JsonObj(httpResponse);
        } catch (UnsupportedEncodingException e) {
            log.info("Post UnsupportedEncodingException,e:{}", e);
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            log.info("Post ClientProtocolException,e:{}", e);
            e.printStackTrace();
        } catch (IOException e) {
            log.info("Post IOException,e:{}", e);
            e.printStackTrace();
        } catch (Exception e) {
            log.info("Post Exception,e:{}", e);
            e.printStackTrace();
        } finally {
            httpClient.close();
        }
        return jsonRet;
    }

    public static String postOfString(Map<String, String> header, String url, String data, Integer timeOut)
            throws IOException {
        String applicationType = "application/json";
        if (null == timeOut) {
            timeOut = TIME_OUT;
        }
        String charset = "UTF-8";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        try {
            post.setConfig(RequestConfig.custom().setConnectionRequestTimeout(timeOut)
                    .setSocketTimeout(timeOut).setConnectTimeout(timeOut).build());
            setHeader(header, post);
            if (StringUtils.isNotBlank(data)) {
                StringEntity stringEntity = new StringEntity(data, charset);
                stringEntity.setContentEncoding(charset);
                stringEntity.setContentType(applicationType);
                post.setEntity(stringEntity);
            }
            CloseableHttpResponse httpResponse = httpClient.execute(post);
            return toString(httpResponse);
        } catch (UnsupportedEncodingException e) {
            log.info("Post UnsupportedEncodingException,e:{}", e);
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            log.info("Post ClientProtocolException,e:{}", e);
            e.printStackTrace();
        } catch (IOException e) {
            log.info("Post IOException,e:{}", e);
            e.printStackTrace();
        } catch (Exception e) {
            log.info("Post Exception,e:{}", e);
            e.printStackTrace();
        } finally {
            httpClient.close();
        }
        return null;
    }

    private static void setHeader(Map<String, String> header, HttpPost post) {
        if (null != header) {
            Set<Map.Entry<String, String>> set = header.entrySet();
            Iterator it = set.iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
                post.addHeader(entry.getKey(), entry.getValue());
            }

        } else {
            post.addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome123.0.0.0 Safari/537.36 Edg/123.0.0.0");
            post.addHeader("Accept-Encoding", "gzip, deflate, br");
            post.addHeader("Connection", "keep-alive");
            post.addHeader("Accept", "*/*");
            post.addHeader("Content-Type",  "application/json; charset=UTF-8");
        }
    }

    public static JSONObject CvtHttpResp2JsonObj(CloseableHttpResponse httpResp) throws ParseException, IOException {
        JSONObject jsonRet = null;
        if (null != httpResp) {
            if (httpResp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity respEntity = httpResp.getEntity();
                String respJSONStr = EntityUtils.toString(respEntity, "UTF-8");
                jsonRet = JSON.parseObject(respJSONStr);
            } else {
                 log.error("HttpResponse的响应码为：" + httpResp.getStatusLine().getStatusCode());
                log.error("HttpResponse的响应报文为：" + httpResp.getEntity().toString());
            }
        } else {
            log.error("HttpResponse为空!!!");
        }
        return jsonRet;
    }

    public static String toString(CloseableHttpResponse httpResp) throws ParseException, IOException {
        if (null != httpResp) {
            if (httpResp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity respEntity = httpResp.getEntity();
                return EntityUtils.toString(respEntity, "UTF-8");
            } else {
                log.error("HttpResponse的响应码为：" + httpResp.getStatusLine().getStatusCode());
                log.error("HttpResponse的响应报文为：" + httpResp.getEntity().toString());
            }
        } else {
            log.error("HttpResponse为空!!!");
        }
        return null;
    }


    public static JSONObject HttpPut(Map<String, String> header, String url, JSONObject jsonObject) throws IOException {
        JSONObject jsonRet = null;
        CloseableHttpResponse httpResponse = null;
        String applicationType = "application/json";
        String charset = "UTF-8";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPut httpPut = new HttpPut(url);
        try {
            if (null != header) {
                Set<Map.Entry<String, String>> set = header.entrySet();
                Iterator it = set.iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
                    httpPut.addHeader(entry.getKey(), entry.getValue());
                }
            }

            httpPut.addHeader("Content-Type", applicationType);
            StringEntity stringEntity = new StringEntity(jsonObject.toString(), charset);
            stringEntity.setContentEncoding(charset);
            stringEntity.setContentType(applicationType);
            httpPut.setEntity(stringEntity);
            httpResponse = httpClient.execute(httpPut);
            jsonRet = CvtHttpResp2JsonObj(httpResponse);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpClient.close();
        }
        return jsonRet;
    }

    public static JSONObject HttpGet(Map<String, String> header, String url) throws IOException {
        String applicationType = "application/json";
        String charset = "UTF-8";
        CloseableHttpResponse httpResponse = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        if (null != header) {
//            httpGet.addHeader("token", header.get("token"));
            Set<Map.Entry<String, String>> set = header.entrySet();
            Iterator it = set.iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
                httpGet.addHeader(entry.getKey(), entry.getValue());
            }
        }

        httpGet.addHeader("Content-Type", applicationType);
        JSONObject jsonRet = null;
        try {
            httpResponse = httpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = httpResponse.getEntity();
                String respJSONStr = EntityUtils.toString(entity, charset);
                jsonRet = JSON.parseObject(respJSONStr);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            //关闭连接 ,释放资源
            httpClient.close();
        }
        return jsonRet;
    }


}
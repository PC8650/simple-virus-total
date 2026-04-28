package com.vt.atp.component;

import com.google.api.client.http.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vt.atp.exception.WrapperException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * virus total 调用器
 */
@Component
@RequiredArgsConstructor
public class VtRemoter {

    private final HttpHeaders headers;

    private final HttpRequestFactory requestFactory;

    private final Gson gson;

    public <T> T post(String url, HttpContent content, TypeToken<T> typeToken) {
        HttpRequest request = requestConstruct(true, url, content);
        return remote(request, typeToken);
    }

    public <T> T get(String url, TypeToken<T> typeToken) {
        HttpRequest request = requestConstruct(false, url, null);
        return remote(request, typeToken);
    }

    /**
     * 构建请求
     * @param post 是否post调用，vt接口只有post和get
     * @param url url
     * @param content {@link HttpContent 请求体/表单 数据}
     * @return HttpRequest
     */
    private HttpRequest requestConstruct(boolean post, String url, HttpContent content) {
        try {
            HttpRequest request;
            if (post) request = requestFactory.buildPostRequest(new GenericUrl(url), content);
            else request = requestFactory.buildGetRequest(new GenericUrl(url));
            request.setHeaders(headers);
            return request;
        }catch (Exception e){
            throw new WrapperException("virus total 请求参数构建异常", e);
        }
    }

    private <T> T remote(HttpRequest request, TypeToken<T> typeToken) {
        HttpResponse response;
        try {
            response = request.execute();
        }catch (Exception e){
            throw new WrapperException("virus total 调用失败", e);
        }

        try {
            String body = response.parseAsString();
            return gson.fromJson(body, typeToken.getType());
        }catch (Exception e){
            throw new WrapperException("virus total 响应解析异常", e);
        }
    }

}

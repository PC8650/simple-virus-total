package com.vt.remote.component;

import com.google.api.client.http.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vt.remote.dto.VtResult;
import com.vt.exception.WrapperException;
import com.vt.enums.MsgEnum;
import com.vt.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * virus total 调用器
 */
@Slf4j
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
     * post 并只返回响应包装的data
     */
    public <T> T postResultData(String url, HttpContent content, Class<T> clazz) {
        VtResult<T> vtResult = post(url, content, new TypeToken<VtResult<T>>() {
        });
        return vtResult.getData();
    }

    /**
     * get 并只返回响应包装的data
     */
    public <T> T getResultData(String url, Class<T> clazz) {
        VtResult<T> vtResult = get(url, new TypeToken<VtResult<T>>() {
        });
        return vtResult.getData();
    }

    /**
     * 构建请求
     * 
     * @param post    是否post调用，vt接口只有post和get
     * @param url     url
     * @param content {@link HttpContent 请求体/表单 数据}
     * @return HttpRequest
     */
    private HttpRequest requestConstruct(boolean post, String url, HttpContent content) {
        try {
            HttpRequest request;
            if (post)
                request = requestFactory.buildPostRequest(new GenericUrl(url), content);
            else
                request = requestFactory.buildGetRequest(new GenericUrl(url));
            request.setHeaders(headers);
            return request;
        } catch (Exception e) {
            throw new WrapperException(MessageUtils.getMessage(MsgEnum.VT_ERROR_BUILD), e);
        }
    }

    private <T> T remote(HttpRequest request, TypeToken<T> typeToken) {
        HttpResponse response;
        try {
            response = request.execute();
        } catch (Exception e) {
            throw new WrapperException(MessageUtils.getMessage(MsgEnum.VT_ERROR_CALL, e.getMessage()), e);
        }

        String body = "";
        try {
            body = response.parseAsString();
            return gson.fromJson(body, typeToken.getType());
        } catch (Exception e) {
            log.info("virus total response: {}", body);
            throw new WrapperException(MessageUtils.getMessage(MsgEnum.VT_ERROR_PARSE), e);
        }
    }

}

package com.vt.atp.remote.dto;

import com.vt.atp.remote.dto.vt.Link;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.Map;

@Data
public class VtResult<T> {

    private T data;

    private Link links;

    private Map<String, Object> meta;

    private String error;

    public static VtResult<?> error(String error) {
        VtResult<?> vtResult = new VtResult<>();
        vtResult.error = error;
        return vtResult;
    }

    public boolean isSuccess() {
        return !StringUtils.hasText(error);
    }

}

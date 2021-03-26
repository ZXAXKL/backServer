package com.graduation.common.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDto implements Serializable {
    private static final long serialVersionUID = -6757080704476385401L;

    private int code;
    private String msg;
    private Object payload;

    public static final int CODE_TOO_MANY_REQUEST = -4;
    public static final int CODE_METHOD_NOT_ALLOWED = -3;
    public static final int CODE_BAD_REQUEST = -2;
    public static final int CODE_SERVER_ERROR = -1;
    public static final int CODE_SUCCESS = 0;
    public static final int CODE_NOT_LOGIN = 1;
    public static final int CODE_ERROR = 2;
    public static final int CODE_WARNING = 3;


    public void setValue(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public void setValue(int code, String msg, Object payload) {
        this.code = code;
        this.msg = msg;
        this.payload = payload;
    }

}

package com.graduation.common.result;

public class ResultUtils {
    //服务器异常
    public static ResponseDto serviceError() {
        return serviceError("服务器异常", null);
    }

    //服务器异常（带提示）
    public static ResponseDto serviceError(String msg) {
        ResponseDto dto = new ResponseDto();
        dto.setValue(ResponseDto.CODE_SERVER_ERROR, msg);
        return dto;
    }

    //服务器异常（带数据和提示）
    public static ResponseDto serviceError(String msg, Object payload) {
        ResponseDto dto = new ResponseDto();
        dto.setValue(ResponseDto.CODE_SERVER_ERROR, msg, payload);
        return dto;
    }

    //提交方式错误
    public static ResponseDto methodNotAllowed() {
        ResponseDto dto = new ResponseDto();
        dto.setValue(ResponseDto.CODE_METHOD_NOT_ALLOWED, "请求方式错误");
        return dto;
    }

    //404
    public static ResponseDto notFound() {
        ResponseDto dto = new ResponseDto();
        dto.setValue(ResponseDto.CODE_BAD_REQUEST, "没有这个路由");
        return dto;
    }

    //请求太多
    public static ResponseDto tooManyRequest() {
        ResponseDto dto = new ResponseDto();
        dto.setValue(ResponseDto.CODE_TOO_MANY_REQUEST, "请求过多，请稍后再试");
        return dto;
    }

    //错误的请求
    public static ResponseDto badRequest() {
        ResponseDto dto = new ResponseDto();
        dto.setValue(ResponseDto.CODE_BAD_REQUEST, "错误的请求，请检查参数和格式");
        return dto;
    }

    //成功
    public static ResponseDto success() {
        return success("成功", null);
    }

    //返回成功信息
    public static ResponseDto success(String msg) {
        return success(msg, null);
    }

    //返回成功信息（带数据）
    public static ResponseDto success(Object payload) {
        return success("成功", payload);
    }

    //返回成功信息（带数据和提示）
    public static ResponseDto success(String msg, Object payload) {
        ResponseDto dto = new ResponseDto();
        dto.setValue(ResponseDto.CODE_SUCCESS, msg, payload);
        return dto;
    }

    //返回未登录错误
    public static ResponseDto unLogin() {
        ResponseDto dto = new ResponseDto();
        dto.setValue(ResponseDto.CODE_NOT_LOGIN, "你还没有登陆");
        return dto;
    }

    //权限不足错误
    public static ResponseDto permissionDenied() {
        ResponseDto dto = new ResponseDto();
        dto.setValue(ResponseDto.CODE_NOT_LOGIN, "你没有权限执行这个操作");
        return dto;
    }

    //错误
    public static ResponseDto error() {
        return error("错误", null);
    }

    //返回错误信息（带提示）
    public static ResponseDto error(String msg) {
        return error(msg, null);
    }

    //返回错误信息（带数据）
    public static ResponseDto error(Object payload) {
        return error("错误", payload);
    }

    //返回错误信息（带数据和提示）
    public static ResponseDto error(String msg, Object payload) {
        ResponseDto dto = new ResponseDto();
        dto.setValue(ResponseDto.CODE_ERROR, msg, payload);
        return dto;
    }

    //警告
    public static ResponseDto warning() {
        return warning("警告", null);
    }

    //返回警告信息（带提示）
    public static ResponseDto warning(String msg) {
        return warning(msg, null);
    }

    //返回警告信息（带数据）
    public static ResponseDto warning(Object payload) {
        return warning("警告", payload);
    }

    //返回警告信息（带数据和提示）
    public static ResponseDto warning(String msg, Object payload) {
        ResponseDto dto = new ResponseDto();
        dto.setValue(ResponseDto.CODE_WARNING, msg, payload);
        return dto;
    }

    //返回信息（带数据和提示）
    public static ResponseDto result(int code, String msg, Object payload) {
        ResponseDto dto = new ResponseDto();
        dto.setValue(code, msg, payload);
        return dto;
    }

    //返回信息（带提示）
    public static ResponseDto result(int code, String msg) {
        ResponseDto dto = new ResponseDto();
        dto.setValue(code, msg);
        return dto;
    }
}


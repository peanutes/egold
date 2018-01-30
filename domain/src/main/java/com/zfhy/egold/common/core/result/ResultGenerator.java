package com.zfhy.egold.common.core.result;



public class ResultGenerator {
    private static final String DEFAULT_SUCCESS_MESSAGE = "SUCCESS";

    public static<T> Result<T> genSuccessResult() {
        return new Result<T>()
                .setCode(ResultCode.SUCCESS)
                .setMsg(DEFAULT_SUCCESS_MESSAGE);
    }

    public static <T> Result<T> genSuccessResult(T data) {
        return new Result<T>()
                .setCode(ResultCode.SUCCESS)
                .setMsg(DEFAULT_SUCCESS_MESSAGE)
                .setData(data);

    }

    public static<T> Result<T> genFailResult(String message) {
        return new Result<T>()
                .setCode(ResultCode.FAIL)
                .setMsg(message);
    }

    public static <T> Result<T> genFailResult(ResultCode resultCode, String message) {
        return new Result<T>()
                .setCode(resultCode)
                .setMsg(message);
    }

}

package com.zfhy.egold.common.exception;


import com.zfhy.egold.common.core.result.ResultCode;
import org.apache.commons.lang3.StringUtils;


public class BusException extends RuntimeException{

    private ResultCode resultCode;

    public BusException(String message) {
        super(message);
        this.resultCode = ResultCode.BUS_EXCEPTION;
    }

    public BusException(ResultCode resultCode, String message) {
        super(message);
        this.resultCode = resultCode;
    }


    public BusException(String message, Throwable th) {
        super(message, th);
        this.resultCode = ResultCode.BUS_EXCEPTION;
    }

    public BusException(ResultCode resultCode, String message, Throwable th) {
        super(message, th);
        this.resultCode = resultCode;
    }

    public static BusExceptionBuilder builder() {
        return new BusExceptionBuilder();
    }


    public ResultCode getResultCode() {
        return resultCode;
    }

    public void setResultCode(ResultCode resultCode) {
        this.resultCode = resultCode;
    }

    public static class BusExceptionBuilder {
        private ResultCode resultCode;
        private String message;
        private Throwable throwable;

        public BusExceptionBuilder resultCode(ResultCode resultCode) {
            this.resultCode = resultCode;
            return this;
        }

        public BusExceptionBuilder message(String message) {
            this.message = message;
            return this;
        }

        public BusExceptionBuilder throwable(Throwable throwable) {
            this.throwable = throwable;
            return this;
        }


        public BusException build() {
            if (StringUtils.isBlank(message)) {
                message = "UN_KNOW EXCEPTION";
            }
            return resultCode == null ? new BusException(message, throwable) : new BusException(resultCode, message, throwable);
        }

    }


}

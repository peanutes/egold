package com.zfhy.egold.common.exception;

import lombok.Data;


@Data
public class BindErrorMsg {
    private String field;
    private String defaultMessage;
}


package com.zfhy.egold.common.exception;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.core.result.ResultCode;
import com.zfhy.egold.common.core.result.ResultGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result exceptionHandler(Exception e) {
        log.error("统一处理系统异常：", e);
        return ResultGenerator.genFailResult(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(BusException.class)
    @ResponseBody
    public Result busExceptionHandler(BusException e) {
        Result resultData = new Result();

        resultData.setCode(e.getResultCode());
        resultData.setMsg(e.getMessage());
        log.error("统一处理业务异常：", e);
        return resultData;
    }

    
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public Result handleConstraintViolationException(ConstraintViolationException cve){
        Set<ConstraintViolation<?>> cves = cve.getConstraintViolations();
        String messages = cves.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(";"));
        return ResultGenerator.genFailResult(ResultCode.PARAM_VALIDATE, messages);

    }

    @ExceptionHandler(BindException.class)
    @ResponseBody
    public Result handleConstraintViolationException(BindException be) {
        BindingResult bindingResult = be.getBindingResult();
        List<ObjectError> allErrors = bindingResult.getAllErrors();

        List<BindErrorMsg> bindErrorMsgs = new Gson().fromJson(new Gson().toJson(allErrors), new TypeToken<List<BindErrorMsg>>() {}.getType());

        String errorMsg = bindErrorMsgs.stream().map(e -> String.join(":", e.getField() , e.getDefaultMessage())).collect(Collectors.joining(";"));
        return ResultGenerator.genFailResult(ResultCode.PARAM_VALIDATE, errorMsg);

    }

}

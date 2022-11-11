package com.boyun.boyunfile.advice;



import com.boyun.boyunfile.common.RestResult;
import com.boyun.boyunfile.constant.ResultCodeEnum;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j

//这是一个增强的 Controller。使用这个 Controller ，可以实现三个方面的功能：1、全局异常处理， 2、全局数据绑定， 3、全局数据预处理
@ControllerAdvice
public class GlobalExceptionHandlerAdvice {
    /**-------- 通用异常处理方法 --------**/
    @ExceptionHandler(Exception.class)//    异常类型
    @ResponseBody
    public RestResult error(Exception e) {
        e.printStackTrace();
        log.error("全局异常捕获：" + e);
        return RestResult.fail();    // 通用异常结果
    }

    /**-------- 指定异常处理方法 --------**/
    @ExceptionHandler(NullPointerException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestResult error(NullPointerException e) {
        e.printStackTrace();
        log.error("全局异常捕获：" + e);
        return RestResult.setResult(ResultCodeEnum.NULL_POINT);
    }

    //下标越界处理方法
    @ExceptionHandler(IndexOutOfBoundsException.class)
    @ResponseBody
    public RestResult error(IndexOutOfBoundsException e) {
        e.printStackTrace();
        log.error("全局异常捕获：" + e);
        return RestResult.setResult(ResultCodeEnum.INDEX_OUT_OF_BOUNDS);
    }


}
package com.xuecheng.framework.exception;

import com.google.common.collect.ImmutableMap;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author liuwei
 * @description 异常捕获类
 * @date 2020/02/01
 */

@ControllerAdvice
public class ExceptionCatch {
    //添加日志类
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionCatch.class);


    //使用exceptions 存放异常类型和错误代码的映射，ImmutableMap的特点是一旦创建不可改变，并且线程安全
    private static ImmutableMap<Class<? extends Throwable>, ResultCode> EXCEPTIONS;
    //用builder来构建一个异常类型和错误代码的异常
    protected static ImmutableMap.Builder<Class<? extends Throwable>, ResultCode> builder = ImmutableMap.builder();

    static {
        //这里加入一些基础的异常类型判断
        builder.put(HttpMessageNotReadableException.class,CommonCode.INVALID_PARAM);
    }

    //捕获customException
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ResponseResult customException(CustomException e) {
        LOGGER.error("catchexception:{}\r\nexception:", e.getMessage(), e);
        ResultCode resultCode = e.getResultCode();
        return new ResponseResult(resultCode);
    }

    //捕获exception 异常
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult excetion(Exception e) {
        LOGGER.error("catchexception:{}\r\nexception:", e.getMessage(), e);
        if (EXCEPTIONS == null)
            EXCEPTIONS = builder.build();
            final ResultCode resultCode = EXCEPTIONS.get(e.getClass());
            final ResponseResult responseResult;
            if (resultCode!=null){
                responseResult = new ResponseResult(resultCode);
            }else {
                responseResult=new ResponseResult(CommonCode.SERVER_ERROR);
            }
            return responseResult;
        }

}

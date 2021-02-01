package com.xuecheng.framework.exception;

import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOGGER= LoggerFactory.getLogger(ExceptionCatch.class);

    //捕获customException
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ResponseResult customException(CustomException e){
        LOGGER.error("catchexception:{}\r\nexception:",e.getMessage(),e);
        ResultCode resultCode = e.getResultCode();
        return new ResponseResult(resultCode);
    }
}

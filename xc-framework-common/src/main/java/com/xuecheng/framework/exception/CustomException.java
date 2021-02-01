package com.xuecheng.framework.exception;

import com.xuecheng.framework.model.response.ResultCode;

/**
 * @author liuwei
 * @description 自定义异常类
 * @date 2021/02/01
 */

public class CustomException extends RuntimeException {
    private ResultCode resultCode;

    public CustomException(ResultCode resultCode){
        //异常信息为：异常代码+异常信息
        super("错误代码："+resultCode.code()+"错误信息："+resultCode.message());
        this.resultCode=resultCode;
    }

    public ResultCode getResultCode(){
        return resultCode;
    }

}

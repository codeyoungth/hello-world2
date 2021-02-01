package com.xuecheng.framework.exception;

import com.xuecheng.framework.model.response.ResultCode;

/**
 * @author liuwei
 * @description 异常抛出类
 * @date 2021/02/01
 */

public class ExceptionCast {
    //使用此静态方法抛出异常
    public static void cast(ResultCode resultCode){
        throw new CustomException(resultCode);
    }
}

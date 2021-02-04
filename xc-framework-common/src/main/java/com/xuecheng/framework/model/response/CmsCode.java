package com.xuecheng.framework.model.response;

import lombok.ToString;

/**
 * @author liuwei
 * @description 业务异常代码
 * @date 2020/02/01
 */
@ToString
public enum CmsCode implements ResultCode{
    CMS_PAGE_EXIST(false,24001,"页面信息已存在!"),
    CMS_PAGE_NOTEXISTS(false,24003,"页面信息不存在!"),
    CMS_GENERATEHTML_DATAURLISNULL(false,24004,"页面dataUrl为空!"),
    CMS_GENERATEHTML_TEMPLATEISNULL(false,24005,"页面模板数据为空!"),
    CMS_GENERATEHTML_DATAISNULL(false,24006,"模型数据为空!"),
    PARAMS_IS_NULL(false,24002,"非法请求，参数为空！");

    //操作结果
    boolean success;
    //操作代码
    int code;
    //提示信息
    String message;

    private  CmsCode(boolean success,int code,String message){
        this.success=success;
        this.code=code;
        this.message=message;
    }

    @Override
    public boolean success() {
        return success;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
};

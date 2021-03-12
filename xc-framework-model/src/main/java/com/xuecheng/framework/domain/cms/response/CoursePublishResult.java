package com.xuecheng.framework.domain.cms.response;

import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CoursePublishResult extends ResponseResult {

    String pageUrl;
    public CoursePublishResult(ResultCode resultCode, String pageUrl){
        super(resultCode);
        this.pageUrl=pageUrl;
    }
}

package com.xuecheng.framework.domain.cms.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryPageRequest {
    //接收页面查询条件

    //站点id
    @ApiModelProperty("站点id")
    private String  siteId;
    //模板id
    @ApiModelProperty("模板id")
    private String  templateId;
    //页面别名
    @ApiModelProperty("页面别名")
    private String  pageAliase;
    //页面名称
    @ApiModelProperty("页面名称")
    private String  pageName;
    //页面id
    @ApiModelProperty("页面id")
    private String  pageId;
    //页面类型
    @ApiModelProperty("页面类型")
    private String  pageType;

}

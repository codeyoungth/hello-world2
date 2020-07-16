package com.xuecheng.api.cms;

import com.xuecheng.framework.model.response.QueryResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "站点接口",description = "站点接口，提供站点的增删改查")
public interface CmsSiteControllerApi {

    //页面查询信息
    @ApiOperation("查询所有的站点")
    public QueryResponseResult findList();
}

package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value = "cms页面管理接口",description = "cms页面管理接口，提供页面的增删改查")
public interface CmsPageControllerApi {
    //页面查询信息
    @ApiOperation("分页查询页面列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page",value = "页码",required = true,paramType = "path",dataType = "int"),
            @ApiImplicitParam(name = "size",value = "每页记录数",required = true,paramType = "path",dataType = "int")
    })
    QueryResponseResult findList(int page, int size, QueryPageRequest request);

    //添加页面
    @ApiOperation("添加页面")
    CmsPageResult addPage(CmsPage cmsPage);

    //根据id查询页面
    @ApiOperation("通过id查询页面")
    CmsPage findById(String id);

    //修改页面
    @ApiOperation("修改页面")
    CmsPageResult editPage(String id,CmsPage cmsPage);

    //删除页面
    @ApiOperation("删除页面")
    ResponseResult delete(String id);

    //发布页面
    @ApiOperation("发布页面")
    ResponseResult post(String pageId);
}

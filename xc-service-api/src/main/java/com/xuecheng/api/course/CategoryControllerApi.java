package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "分类接口api",description = "分类相关接口api说明")
public interface CategoryControllerApi {
    @ApiOperation("查询课程分类列表")
    CategoryNode findCategoryList();
}

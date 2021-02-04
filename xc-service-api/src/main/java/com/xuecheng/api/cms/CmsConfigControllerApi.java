package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Api(value = "cms页面管理接口",description = "cms页面管理接口，提供页面的增删改查")
public interface CmsConfigControllerApi {

    @ApiOperation("根据id查询CMS配置信息")
    CmsConfig getModel(@PathVariable("id") String id);
}

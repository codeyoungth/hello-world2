package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.CmsSiteControllerApi;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.manage_cms.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liuwei
 * @description 站点相关接口类
 * @date 2020-07-02
 */

@RestController
@RequestMapping("/cms/site")
public class CmsSiteController implements CmsSiteControllerApi {


    @Autowired
    private SiteService siteService;

    @Override
    @GetMapping
    public QueryResponseResult findList() {
        return siteService.findAll();
    }
}

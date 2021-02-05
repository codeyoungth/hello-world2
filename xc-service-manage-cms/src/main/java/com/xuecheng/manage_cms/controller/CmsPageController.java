package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.CmsPageControllerApi;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//相l当于Congtroller+ResponseBody,ResponseBody的作用是将返回结果转换为json
@RestController
@RequestMapping("cms/page")
public class CmsPageController implements CmsPageControllerApi {

    @Autowired
    private PageService pageService;


    @Override
    @GetMapping("/list/{page}/{size}")
    public QueryResponseResult findList(@PathVariable("page") int page, @PathVariable("size") int size, QueryPageRequest request) {
    return pageService.findCriteriaList(page,size,request);
    }

    @Override
    @PostMapping("/add")
    public CmsPageResult addPage(@RequestBody CmsPage cmsPage) {
       return  pageService.save(cmsPage);
    }


    @Override
    @GetMapping("/get/{id}")
    public CmsPage findById(@PathVariable("id") String id) {
        return pageService.getById(id);
    }

    @Override
    @PutMapping("/edit/{id}")//这里使用put方法，http中put表示更新
    public CmsPageResult editPage(@PathVariable("id") String id, @RequestBody CmsPage cmsPage) {
        return pageService.editPage(id,cmsPage);
    }

    @Override
    @DeleteMapping("/del/{id}")
    public ResponseResult delete(@PathVariable("id") String id) {
        return pageService.delete(id);
    }

    @Override
    public ResponseResult post(String pageId) {
        return null;
    }
}

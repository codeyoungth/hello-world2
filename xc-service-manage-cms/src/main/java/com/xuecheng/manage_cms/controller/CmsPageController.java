package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.CmsPageControllerApi;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;


@RestController//相当于Congtroller+ResponseBody,ResponseBody的作用是将返回结果转换为json

public class CmsPageController implements CmsPageControllerApi {


    @Override
    @GetMapping("/list/{page}/{size}")
    public QueryResponseResult findList(@PathParam("page") int page,@PathParam("size") int size, QueryPageRequest request) {

        QueryResult queryResult=new QueryResult();
        queryResult.setTotal(2);

        //静态数据列表
        List pageList = new ArrayList();
        CmsPage cmsPage = new CmsPage();
        cmsPage.setPageName("测试页面");
        pageList.add(cmsPage);

        queryResult.setList(pageList);


        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS,queryResult);
        return queryResponseResult;
    }
}

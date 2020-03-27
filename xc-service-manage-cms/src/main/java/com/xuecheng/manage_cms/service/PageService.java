package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PageService {

    @Autowired
    private CmsPageRepository cmsPageRepository;

    /*
     *页面列表分页查询
     */
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest){
        //判断queryPageRequest是否为空
        if (queryPageRequest==null){
            queryPageRequest=new QueryPageRequest();
        }

        //判断如果面小于或者等于0，则将page==1
        if (page<=0){
            page=1;
        }

        page=page-1;//为了适应mongoDB接口，将page-1

        //判断size如果<0,将size设为20
        if (size<0){
            size=20;
        }
        //分页对象
        Pageable pageable=new PageRequest(page,size);
        //分页查询
        Page<CmsPage> all = cmsPageRepository.findAll(pageable);


        //首先将查询出的内容先封装到queryResult中
        QueryResult<CmsPage> cmsPageQueryResult = new QueryResult<>();
        cmsPageQueryResult.setTotal(all.getTotalElements());
        cmsPageQueryResult.setList(all.getContent());

        //响应
        return new QueryResponseResult(CommonCode.SUCCESS,cmsPageQueryResult);
    }

}

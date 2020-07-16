package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
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

        /**
          *功能描述  动态拼接条件查询网站信息
          * @author 刘卫
          * @date 2020-07-02
          * @param  page 当前页数 size 每页显示条数 request 查询条件对象
          * @return QueryResponseResult
          */
   public QueryResponseResult findCriteriaList(int page,int size ,QueryPageRequest request){

       //条件匹配器
       //页面名称模糊查询，需要自定义字符串的匹配器实现模糊查询
       ExampleMatcher matcher = ExampleMatcher.matching();
       matcher=matcher.withMatcher("pageAliase",ExampleMatcher.GenericPropertyMatchers.contains());

       //条件值
       CmsPage cmsPage = new CmsPage();

       //站点id
       if (StringUtils.isNotBlank(request.getSiteId())){
           cmsPage.setSiteId(request.getSiteId());
       }
       //页面别名
       if (StringUtils.isNotBlank(request.getPageAliase())){
           cmsPage.setPageAliase(request.getPageAliase());
       }

       //创建条件实例
       Example<CmsPage> ex = Example.of(cmsPage, matcher);

       //页码
       page=page-1;
       //创建分页对象
       PageRequest pageRequest = PageRequest.of(page, size);

       //分页查询
       Page<CmsPage> all = cmsPageRepository.findAll(ex, pageRequest);

       //封装查询结果
       QueryResult<CmsPage> cmsPageQueryResult = new QueryResult<>();

       cmsPageQueryResult.setTotal(all.getTotalElements());

       cmsPageQueryResult.setList(all.getContent());

       //返回结果
       return new QueryResponseResult(CommonCode.SUCCESS,cmsPageQueryResult);

   }

     /**
       *功能描述 保存新增页面
       * @author 刘卫
       * @date 2020-07-02
       * @param  * @param null
       * @return CmsPageResult
       */
   public CmsPageResult save(CmsPage cmsPage){
       //检验页面是否存在，根据页面名称、站点id、访问路径
       CmsPage page = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(), cmsPage.getSiteId(), cmsPage.getPageWebPath());

       if (page==null){
           cmsPage.setPageId(null);
           cmsPageRepository.save(cmsPage);
           CmsPageResult cmsPageResult = new CmsPageResult(CommonCode.SUCCESS, cmsPage);
           return cmsPageResult;
       }
       return new CmsPageResult(CommonCode.FAIL,null);
   }

}

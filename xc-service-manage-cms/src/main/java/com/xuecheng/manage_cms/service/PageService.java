package com.xuecheng.manage_cms.service;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.*;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Service
public class PageService {

    @Autowired
    private CmsPageRepository cmsPageRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CmsTemplateRepository cmsTemplateRepository;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFSBucket gridFSBucket;

    /*
     *页面列表分页查询
     */
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest) {
        //判断queryPageRequest是否为空
        if (queryPageRequest == null) {
            queryPageRequest = new QueryPageRequest();
        }

        //判断如果面小于或者等于0，则将page==1
        if (page <= 0) {
            page = 1;
        }

        page = page - 1;//为了适应mongoDB接口，将page-1

        //判断size如果<0,将size设为20
        if (size < 0) {
            size = 20;
        }
        //分页对象
        Pageable pageable = new PageRequest(page, size);
        //分页查询
        Page<CmsPage> all = cmsPageRepository.findAll(pageable);


        //首先将查询出的内容先封装到queryResult中
        QueryResult<CmsPage> cmsPageQueryResult = new QueryResult<>();
        cmsPageQueryResult.setTotal(all.getTotalElements());
        cmsPageQueryResult.setList(all.getContent());

        //响应
        return new QueryResponseResult(CommonCode.SUCCESS, cmsPageQueryResult);
    }

    /**
     * 功能描述  动态拼接条件查询网站信息
     *
     * @param page 当前页数 size 每页显示条数 request 查询条件对象
     * @return QueryResponseResult
     * @author 刘卫
     * @date 2020-07-02
     */
    public QueryResponseResult findCriteriaList(int page, int size, QueryPageRequest request) {

        //条件匹配器
        //页面名称模糊查询，需要自定义字符串的匹配器实现模糊查询
        ExampleMatcher matcher = ExampleMatcher.matching();
        matcher = matcher.withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());
        matcher = matcher.withMatcher("siteId", ExampleMatcher.GenericPropertyMatchers.exact());
        matcher = matcher.withMatcher("pageName", ExampleMatcher.GenericPropertyMatchers.contains());
        matcher = matcher.withMatcher("pageType", ExampleMatcher.GenericPropertyMatchers.exact());


        //条件值
        CmsPage cmsPage = new CmsPage();

        //站点id
        if (StringUtils.isNotBlank(request.getSiteId())) {
            cmsPage.setSiteId(request.getSiteId());
        }
        //页面别名
        if (StringUtils.isNotBlank(request.getPageAliase())) {
            cmsPage.setPageAliase(request.getPageAliase());
        }

        //页面别名
        if (StringUtils.isNotBlank(request.getPageName())) {
            cmsPage.setPageName(request.getPageName());
        }

        //页面别名
        if (StringUtils.isNotBlank(request.getPageType())) {
            cmsPage.setPageType(request.getPageType());
        }
        //创建条件实例
        Example<CmsPage> ex = Example.of(cmsPage, matcher);

        //页码
        page = page - 1;
        //创建分页对象
        PageRequest pageRequest = PageRequest.of(page, size);

        //分页查询
        Page<CmsPage> all = cmsPageRepository.findAll(ex, pageRequest);

        //封装查询结果
        QueryResult<CmsPage> cmsPageQueryResult = new QueryResult<>();

        cmsPageQueryResult.setTotal(all.getTotalElements());

        cmsPageQueryResult.setList(all.getContent());

        //返回结果
        return new QueryResponseResult(CommonCode.SUCCESS, cmsPageQueryResult);

    }

    /**
     * 功能描述 保存新增页面
     *
     * @param * @param null
     * @return CmsPageResult
     * @author 刘卫
     * @date 2020-07-02
     */
    public CmsPageResult save(CmsPage cmsPage) {
        //先判断cmsPage是否为空
        if (cmsPage == null) {
            //抛出异常，非法请求
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }

        //检验页面是否存在，根据页面名称、站点id、访问路径
        CmsPage page = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(), cmsPage.getSiteId(), cmsPage.getPageWebPath());

        if (page != null) {
            //抛出异常，已存在相同的页面名称
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
        }
        cmsPage.setPageId(null);
        cmsPageRepository.save(cmsPage);
        CmsPageResult cmsPageResult = new CmsPageResult(CommonCode.SUCCESS, cmsPage);
        return cmsPageResult;
    }

    //根据id查询页面
    public CmsPage getById(String id) {
        Optional<CmsPage> optional = cmsPageRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    //更新页面信息
    public CmsPageResult editPage(String id, CmsPage cmsPage) {
        CmsPage one = this.getById(id);
        if (one != null) {
            //更新模板id
            one.setTemplateId(cmsPage.getTemplateId());
            //更新所属站点id
            one.setSiteId(cmsPage.getSiteId());
            //更新页面别名
            one.setPageAliase(cmsPage.getPageAliase());
            //更新页面名称
            one.setPageName(cmsPage.getPageName());
            //更新访问路径
            one.setPageWebPath(cmsPage.getPageWebPath());
            //更新物理路径
            one.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
            //更新模板数据url
            one.setDataUrl(cmsPage.getDataUrl());
            //执行更新
            CmsPage save = cmsPageRepository.save(one);
            if (save != null) {
                return new CmsPageResult(CommonCode.SUCCESS, save);
            }
        }

        return new CmsPageResult(CommonCode.FAIL, null);

    }


    //删除页面信息
    public ResponseResult delete(String id) {
        CmsPage byId = this.getById(id);
        if (byId != null) {
            cmsPageRepository.deleteById(id);
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);

    }

    //页面静态化方法
    /**
     * 静态化程序获取页面的DataUrl
     *
     * 静态化程序远程请求DataUrl获取数据模型。
     *
     * 静态化程序获取页面的模板信息
     *
     * 执行页面静态化
     */
    public String getPageHtml(String pageId){
        //获取数据模型
        Map model= getModelByPageId(pageId);
        if (model==null){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAISNULL);
        }
        //获取页面模板信息
        String template = getTemplateByPageId(pageId);
        if (StringUtils.isBlank(template)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }

        //执行静态化
        String html = generateHtml(template, model);
        return html;
    }

    //执行静态化
    private String generateHtml(String templateContent,Map model){
        //创建配置对象
        Configuration configuration = new Configuration(Configuration.getVersion());
        //创建模板加载器
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        stringTemplateLoader.putTemplate("template",templateContent);
        //向configuration配置模板加载器
        configuration.setTemplateLoader(stringTemplateLoader);
        try {
            //获取模板
            Template tempalte = configuration.getTemplate("tempalte");
            //调用api进行静态化
         //   String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
         //   return content;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    //获取页面对的模板信息
    private String getTemplateByPageId(String pageId) {
        //获取页面信息
        CmsPage cmsPage = this.getById(pageId);
        //页面不存在
        if (cmsPage == null) {
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        //获取页面的模板id
        String templateId = cmsPage.getTemplateId();
        if (StringUtils.isBlank(templateId)) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        //查询模板信息
        Optional<CmsTemplate> optional = cmsTemplateRepository.findById(templateId);
        if (optional.isPresent()) {
            CmsTemplate cmsTemplate = optional.get();
            //获取模板文件id
            String templateFileId = cmsTemplate.getTemplateFileId();
            //从GridFS中获取模板文件内容
            //根据文件id查询文件
            GridFSFile fsFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(templateFileId)));
            //打开一个下载流对象
            GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(fsFile.getObjectId());
            //创建GridFsResource对象，获取流
            GridFsResource gridFsResource = new GridFsResource(fsFile, gridFSDownloadStream);
            //从流中取数据
            try {
                String content = IOUtils.toString(gridFsResource.getInputStream(), "utf-8");
                return content;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //获取数据模型
    private Map getModelByPageId(String pageId) {
        //获取页面信息
        CmsPage cmsPage = this.getById(pageId);
        //页面不存在
        if (cmsPage == null) {
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        //取出页面的data_url
        String dataUrl = cmsPage.getDataUrl();
        if (StringUtils.isBlank(dataUrl)) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);
        }

        //通过restTemplate 请求dataUrl获取数据
        ResponseEntity<Map> forEntity = restTemplate.getForEntity(dataUrl, Map.class);
        Map respMap = forEntity.getBody();
        return respMap;
    }


}

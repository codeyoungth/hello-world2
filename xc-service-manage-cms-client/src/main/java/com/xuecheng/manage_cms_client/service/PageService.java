package com.xuecheng.manage_cms_client.service;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CmsCode;
import com.xuecheng.manage_cms_client.dao.CmsPageRepository;
import com.xuecheng.manage_cms_client.dao.CmsSiteRepository;
import jdk.nashorn.internal.objects.annotations.Where;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Service
public class PageService {

    @Autowired
    private CmsPageRepository cmsPageRepository;

    @Autowired
    private CmsSiteRepository cmsSiteRepository;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFSBucket gridFSBucket;

    public void savePageToServerPath(String pageId){
        Optional<CmsPage> cmsPageOptional = cmsPageRepository.findById(pageId);
        if (!cmsPageOptional.isPresent()){
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        //取出页面信息
        CmsPage cmsPage = cmsPageOptional.get();
        //取出所属站点
        CmsSite cmsSite = getCmsSiteById(cmsPage.getSiteId());
        //页面物理路径
        String pagePath=cmsSite.getSitePhysicalPath()+cmsPage.getPagePhysicalPath()+cmsPage.getPageName();
        //查询页面静态文件
        String htmlFileId = cmsPage.getHtmlFileId();
        InputStream htmlInputStream = getFileById(htmlFileId);

        if (htmlInputStream==null){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
        }

        FileOutputStream fileOutputStream=null;

        try {
            fileOutputStream=new FileOutputStream(new File(pagePath));
            IOUtils.copy(htmlInputStream,fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                htmlInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    public InputStream getFileById(String fileId){
        try {
            GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(fileId)));
            GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
            GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFSDownloadStream);
            return gridFsResource.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public CmsSite getCmsSiteById(String siteId){
        Optional<CmsSite> cmsSiteOptional = cmsSiteRepository.findById(siteId);
        if (cmsSiteOptional.isPresent()){
            return cmsSiteOptional.get();
        }
        return null;
    }


}

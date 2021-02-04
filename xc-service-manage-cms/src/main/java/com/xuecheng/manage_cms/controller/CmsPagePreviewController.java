package com.xuecheng.manage_cms.controller;

import com.xuecheng.framework.web.BaseController;
import com.xuecheng.manage_cms.service.PageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletOutputStream;
import java.io.IOException;

/**
 * @author liuwei
 * @description 预览页面controller
 * @date
 */

@Controller
public class CmsPagePreviewController extends BaseController {
    @Autowired
    private PageService pageService;

    //接收页面id
    @RequestMapping(value = "cms/preview/{pageId}",method = RequestMethod.GET)
    public void preview(@PathVariable String id){
        String pageHtml = pageService.getPageHtml(id);
        if (StringUtils.isNotEmpty(pageHtml)){
            try {
                ServletOutputStream outputStream = response.getOutputStream();
                outputStream.write(pageHtml.getBytes("utf-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

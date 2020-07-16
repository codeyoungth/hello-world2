package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_cms.dao.CmsSiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liuwei
 * @description 站点service
 * @date 2020-07-02
 */

@Service
public class SiteService {
    @Autowired
    private CmsSiteRepository cmsSiteRepository;

    public QueryResponseResult findAll(){

        List<CmsSite> all = cmsSiteRepository.findAll();

        //组装返回对象
        QueryResult<CmsSite> queryResult = new QueryResult<>();
        queryResult.setList(all);
        queryResult.setTotal(all.size());
        return new QueryResponseResult(CommonCode.SUCCESS,queryResult);
    }

}

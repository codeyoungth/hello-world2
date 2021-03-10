package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.manage_course.dao.SysDictionaryRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysDictionaryService {
    @Autowired
    private SysDictionaryRepository dictionaryRepository;

    public SysDictionary getByType(String type){
        if (StringUtils.isBlank(type)){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        SysDictionary byDType = dictionaryRepository.findSysDictionaryByDType(type);
        return byDType;
    };
}

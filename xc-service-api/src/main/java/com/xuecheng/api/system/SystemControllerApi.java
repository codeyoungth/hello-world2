package com.xuecheng.api.system;

import com.xuecheng.framework.domain.system.SysDictionary;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PathVariable;

@Api(value ="字典API",description = "提供字典表操作响应api")
public interface SystemControllerApi {

    SysDictionary getByType(@PathVariable("type") String type);
}

package com.xuecheng.api.filesystem;

import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Api(value = "文件系统",description = "文件系统接口说明")
public interface FileSystemControllerApi {

    @ApiOperation("上传文件")
    public UploadFileResult upload(@RequestParam("file") MultipartFile file,
                                   @RequestParam(value = "filetag") String filetag,
                                   @RequestParam("businesskey") String businesskey,
                                   @RequestParam("metada") String metada);
}

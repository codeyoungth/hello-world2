package com.xuecheng.api.course;

import com.xuecheng.framework.domain.cms.response.CoursePreviewResult;
import com.xuecheng.framework.domain.cms.response.CoursePublishResult;
import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@Api(value = "课程计划接口",description = "课程计划接口")
public interface CourseControllerApi {

    @ApiOperation("查询课程计划")
    TeachplanNode findTeachplanList(String courseId);

    @ApiOperation("增加课程计划")
    ResponseResult addTeachPlan(Teachplan teachplan);

    @ApiOperation("根据id查询课程信息")
    CourseBase getCourseBaseById(String id);

    @ApiOperation("更新课程基础信息")
    ResponseResult updateCourseBase(String id, CourseBase courseBase);

    @ApiOperation("新增课程基础信息")
    ResponseResult saveCourseBase(CourseBase courseBase);

    @ApiOperation("获取课程列表")
    QueryResponseResult findCourseList(int page, int size, CourseListRequest request);

    @ApiOperation("获取课程营销信息")
    CourseMarket getCourseMarketById(String id);

    @ApiOperation("更新课程营销信息")
    ResponseResult updateCourseMarket(String id,CourseMarket courseMarket);

    @ApiOperation("新增课程图片")
    ResponseResult addCoursePic(@RequestParam("courseId") String courseId, @RequestParam("pic") String pic);

    @ApiOperation("获取课程图片")
    CoursePic findCoursePic(String  courseId);

    @ApiOperation("删除课程图片")
    ResponseResult delCoursePic(String courseId);

    @ApiOperation("课程视图查询")
    CourseView findCourseView(String courseId);

    @ApiOperation("课程预览")
    CoursePreviewResult preview(String id);

    @ApiOperation("发布课程")
    CoursePublishResult publish(@PathVariable String id);



}

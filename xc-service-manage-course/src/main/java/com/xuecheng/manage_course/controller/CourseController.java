package com.xuecheng.manage_course.controller;

import com.xuecheng.api.course.CourseControllerApi;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/course")
public class CourseController implements CourseControllerApi {

    @Autowired
    private CourseService courseService;

    @Override
    @GetMapping("/teachplan/list/{courseId}")
    public TeachplanNode findTeachplanList(@PathVariable("courseId") String courseId) {
        TeachplanNode teachplanList = courseService.findTeachplanList(courseId);
        return teachplanList;
    }

    @Override
    public ResponseResult addTeachPlan(Teachplan teachplan) {
        return courseService.addTeachplan(teachplan);
    }

    @Override
    @GetMapping("/base/{id}")
    public CourseBase getCourseBaseById(@PathVariable("id") String id) {
        return courseService.getCourseBaseById(id);
    }

    @Override
    @PutMapping("/base/{id}")
    public ResponseResult updateCourseBase(@PathVariable("id") String id, CourseBase courseBase) {
        return courseService.updateCourse(id,courseBase);
    }

    @Override
    @PostMapping("/base/add")
    public ResponseResult saveCourseBase(@RequestBody CourseBase courseBase) {
        return courseService.addCourseBase(courseBase);
    }

    @Override
    @GetMapping("/base/list/{page}/{size}")
    public QueryResponseResult findCourseList(@PathVariable("page") int page,@PathVariable("size") int size, CourseListRequest request) {
        return courseService.findCourseList(page,size,request);
    }

    @Override
    @GetMapping("/market/{id}")
    public CourseMarket getCourseMarketById(@PathVariable("id") String id) {
        return courseService.getCourseMarket(id);
    }

    @Override
    @PutMapping("/market/{id}")
    public ResponseResult updateCourseMarket(@PathVariable("id") String id, CourseMarket courseMarket) {
        return courseService.updateCourseMarket(id,courseMarket);
    }
}

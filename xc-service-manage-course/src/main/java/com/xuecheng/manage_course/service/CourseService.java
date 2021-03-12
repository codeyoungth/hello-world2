package com.xuecheng.manage_course.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.domain.cms.response.CoursePreviewResult;
import com.xuecheng.framework.domain.cms.response.CoursePublishResult;
import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.CourseCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.client.CmsPageClient;
import com.xuecheng.manage_course.dao.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private TeachplanMapper teachplanMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private CourseBaseRepository courseBaseRepository;

    @Autowired
    private TeachPlanRepository teachPlanRepository;

    @Autowired
    private CourseMarketRepository courseMarketRepository;

    @Autowired
    private CoursePicRepository coursePicRepository;

    @Autowired
    private CmsPageClient cmsPageClient;

    @Value("${course-publish.dataUrlPre}")
    private String publish_dataUrlPre;
    @Value("${course-publish.pagePhysicalPath}")
    private String publish_page_physicalpath;
    @Value("${course-publish.pageWebPath}")
    private String publish_page_webpath;
    @Value("${course-publish.siteId}")
    private String publish_siteId;
    @Value("${course-publish.templateId}")
    private String publish_templateId;
    @Value("${course-publish.previewUrl}")
    private String previewUrl;

    //查询相应的课程计划
    public TeachplanNode findTeachplanList(String courseId) {
        TeachplanNode teachplanNode = teachplanMapper.selectList(courseId);
        return teachplanNode;
    }

    //查询课程分类
    public CategoryNode findCategoryList() {
        CategoryNode categoryNode = categoryMapper.findCategoryList();
        return categoryNode;
    }


    //获取课程根节点，如果没有需要添加根节点
    public String getTeachPlanRoot(String courseId) {
        //校验课程id:如果课程不存在，课程计划无意义
        Optional<CourseBase> courseBase = courseBaseRepository.findById(courseId);
        if (!courseBase.isPresent()) {
            return null;
        }
        CourseBase course = courseBase.get();
        //如果课程存在，验证其是否有根节点
        List<Teachplan> teachplanList = teachPlanRepository.findByCourseidAndParentid(courseId, "0");
        if (teachplanList == null || teachplanList.size() == 0) {
            //如果没有根节点，需要添加根节点
            Teachplan teachRoot = new Teachplan();
            teachRoot.setCourseid(courseId);
            //根节点名称就是课程名称
            teachRoot.setPname(course.getName());
            //根节点的父节点id为0
            teachRoot.setParentid("0");
            //根节点的级别为：1
            teachRoot.setGrade("1");
            //发布状态：未发布
            teachRoot.setStatus("0");
            //保存
            teachRoot = teachPlanRepository.save(teachRoot);
            return teachRoot.getId();
        }
        //如果根节点存在，则取出来，返回其id
        Teachplan teachplan = teachplanList.get(0);
        return teachplan.getId();
    }

    //添加课程计划
    @Transactional
    public ResponseResult addTeachplan(Teachplan teachplan) {
        //校验课程id和课程计划名称：如果课程计划为空，或者courseId,pname为空，直接抛异常
        if (teachplan == null ||
                StringUtils.isBlank(teachplan.getCourseid()) ||
                StringUtils.isBlank(teachplan.getPname())) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }

        //取出课程id
        String courseId = teachplan.getCourseid();
        //取出父亲节点id
        String parentid = teachplan.getParentid();
        //如果父节点为空，则获取根节点
        if (StringUtils.isBlank(parentid)) {
            parentid = getTeachPlanRoot(courseId);
        }

        //取出父节点信息
        Optional<Teachplan> parent = teachPlanRepository.findById(parentid);
        //如果父节点不存在，则抛异常
        if (!parent.isPresent()) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }

        Teachplan parentTeachPlan = parent.get();
        //父节点级别
        String parentGrade = parentTeachPlan.getGrade();

        //设置父节点
        teachplan.setParentid(parentid);
        //发布状态
        teachplan.setStatus("0");
        //子节点级别，根据父节点判断
        if (parentGrade.equals("1")) {
            teachplan.setGrade("2");
        } else if (parentGrade.equals("2")) {
            teachplan.setGrade("3");
        }
        teachPlanRepository.save(teachplan);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    public CourseBase getCourseBaseById(String id){
        if (StringUtils.isBlank(id)){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        Optional<CourseBase> courseBase = courseBaseRepository.findById(id);
        if (courseBase.isPresent()){
            return courseBase.get();
        }
        return new CourseBase();
    }

    /**
     * 新增课程基础部分
     * @param courseBase
     * @return
     */
    public ResponseResult addCourseBase(CourseBase courseBase){
        if (courseBase == null ||
                StringUtils.isBlank(courseBase.getName()) ||
                StringUtils.isBlank(courseBase.getGrade())||
                StringUtils.isBlank(courseBase.getStudymodel())) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        //设置课程发布状态
       // courseBase.setStatus("0");
        courseBaseRepository.save(courseBase);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 修改课程基础课程
     */
    public ResponseResult updateCourse(String id,CourseBase courseBase){
        if (courseBase == null ||
                StringUtils.isBlank(courseBase.getName()) ||
                StringUtils.isBlank(courseBase.getGrade())||
                StringUtils.isBlank(courseBase.getStudymodel())||
                StringUtils.isBlank(id)) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        Optional<CourseBase> optionalCourseBase = courseBaseRepository.findById(id);
        if (!optionalCourseBase.isPresent()){
            ExceptionCast.cast(CommonCode.FAIL);
        }
        CourseBase oldCourseBase = optionalCourseBase.get();
        BeanUtils.copyProperties(courseBase,oldCourseBase);
        courseBaseRepository.save(oldCourseBase);
        return new ResponseResult(CommonCode.SUCCESS);

    }

    /**
     * 分页获取课程列表
     */
    public QueryResponseResult findCourseList(int page, int size, CourseListRequest request){
        //设置分页查询条件
        if(page>=0&&size>0){
            PageHelper.startPage(page,size);
        }
        Page<CourseInfo> courseListPage = courseMapper.findCourseListPage(request);
        //获取查询结果
        List<CourseInfo> coursList = courseListPage.getResult();
        //封装result
        QueryResult<CourseInfo> courseInfoQueryResult = new QueryResult<>();
        courseInfoQueryResult.setList(coursList);
        courseInfoQueryResult.setTotal(coursList.size());
        return new QueryResponseResult(CommonCode.SUCCESS,courseInfoQueryResult);
    }

    /**
     * 获取课程营销消息
     * @param id
     * @return
     */
    public CourseMarket getCourseMarket(String id){
        //判断条件
        if (StringUtils.isBlank(id)){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        Optional<CourseMarket> courseMarketOptional = courseMarketRepository.findById(id);
        return courseMarketOptional.get();
    }

    /**
     * 更新课程营销信息
     */
    public ResponseResult updateCourseMarket(String id,CourseMarket courseMarket){
        if (StringUtils.isBlank(id)) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        Optional<CourseMarket> courseMarketOptional = courseMarketRepository.findById(id);
        if (!courseMarketOptional.isPresent()){
            ExceptionCast.cast(CommonCode.FAIL);
        }
        CourseMarket courseMarketOld = courseMarketOptional.get();
        BeanUtils.copyProperties(courseMarket,courseMarketOld);
        courseMarketRepository.save(courseMarketOld);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * @desc 保存课程图片
     * @param courseId
     * @param pic
     * @return
     */
    @Transactional
    public ResponseResult saveCoursePic(String courseId,String pic){
        //查询课程图片
        Optional<CoursePic> coursePicOptional = coursePicRepository.findById(courseId);
        CoursePic coursePic=null;
        if (coursePicOptional.isPresent()){
            coursePic=coursePicOptional.get();
        }
        if (coursePic==null){
            coursePic=new CoursePic();
        }
        coursePic.setCourseid(courseId);
        coursePic.setPic(pic);
        coursePicRepository.save(coursePic);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * @desc 获取课程图片
     * @param courseId
     * @return
     */
    public CoursePic findCoursePic(String courseId){
        if (StringUtils.isBlank(courseId)){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        Optional<CoursePic> coursePicOption = coursePicRepository.findById(courseId);
        return coursePicOption.get();
    }

    /**
     * 删除课程图片
     */
    public ResponseResult delCoursePic(String courseId){
        if (StringUtils.isBlank(courseId)){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        long result = coursePicRepository.deleteByCourseid(courseId);
        if (result>0){
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

    //查询课程视图
    public CourseView findCourseView(String courseId){
        if (StringUtils.isBlank(courseId)){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        CourseView courseView = new CourseView();
        //查询课程基础信息
        Optional<CourseBase> courseBaseOptional = courseBaseRepository.findById(courseId);
        if (courseBaseOptional.isPresent()){
            courseView.setCourseBase(courseBaseOptional.get());
        }

        //查询课程营销信息
        Optional<CourseMarket> courseMarketOptional = courseMarketRepository.findById(courseId);
        if (courseBaseOptional.isPresent()){
            courseView.setCourseMarket(courseMarketOptional.get());
        }

        //查询课程图片信息
        Optional<CoursePic> coursePicOptional = coursePicRepository.findById(courseId);
        if (coursePicOptional.isPresent()){
            courseView.setCoursePic(coursePicOptional.get());
        }


        //查询课程计划
        TeachplanNode teachplanNode = teachplanMapper.selectList(courseId);
        if (teachplanNode!=null){
            courseView.setTeachplanNode(teachplanNode);
        }
        return courseView;
    }

    //根据id查询课程基础信息
    public CourseBase findCourseBaseById(String id){
        Optional<CourseBase> courseBaseOptional = courseBaseRepository.findById(id);
        if (courseBaseOptional.isPresent()){
            return courseBaseOptional.get();
        }
        ExceptionCast.cast(CourseCode.COURS_GET_NOTEXISTS);
        return null;
    }

    //课程预览
    public CoursePreviewResult preview(String courseId){
        CmsPage cmsPage = packageCmsPage(courseId);
        //远程请求cms保存页面信息
        CmsPageResult cmsPageResult = cmsPageClient.save(cmsPage);
        if (!cmsPageResult.isSuccess()){
            return new CoursePreviewResult(CommonCode.FAIL,null);
        }
        //页面id
        String pageId = cmsPageResult.getCmsPage().getPageId();
        //页面url
        String pageUrl=previewUrl+pageId;
        return new CoursePreviewResult(CommonCode.SUCCESS,pageUrl);
    }

    //页面发布
    @Transactional
    public CoursePublishResult publish(String courseId){
        //课程信息
        CourseBase courseBase = this.findCourseBaseById(courseId);
        //发布课程详情页面
        CmsPostPageResult cmsPostPageResult=publish_page(courseId);
        if (!cmsPostPageResult.isSuccess()){
            ExceptionCast.cast(CommonCode.FAIL);
        }
        //更新课程状态
        courseBase=saveCoursePubState(courseId);
        //课程索引
        //课程缓存

        //页面url
        String pageUrl = cmsPostPageResult.getPageUrl();
        return new CoursePublishResult(CommonCode.SUCCESS,pageUrl);
    }


    private CmsPostPageResult publish_page(String courseId) {
        //封装要发布的页面
        CmsPage cmsPage = packageCmsPage(courseId);
        //发布页面
        CmsPostPageResult cmsPostPageResult = cmsPageClient.postPageQuick(cmsPage);
        return cmsPostPageResult;
    }

    private CmsPage packageCmsPage(String courseId) {
        CourseBase courseBase = this.findCourseBaseById(courseId);
        //发布课程预览页面
        CmsPage cmsPage = new CmsPage();
        //站点
        cmsPage.setSiteId(publish_siteId);
        //模板
        cmsPage.setTemplateId(publish_templateId);
        //页面名称
        cmsPage.setPageName(courseId+".html");
        //页面别名
        cmsPage.setPageAliase(courseBase.getName());
        //页面访问路径
        cmsPage.setPageWebPath(publish_page_webpath);
        //页面存储路径
        cmsPage.setPagePhysicalPath(publish_page_physicalpath);
        //数据url
        cmsPage.setDataUrl(publish_dataUrlPre+courseId);
        return cmsPage;
    }

    private CourseBase saveCoursePubState(String courseId) {
        CourseBase courseBase = this.findCourseBaseById(courseId);
        courseBase.setStatus("202002");
        courseBase = courseBaseRepository.save(courseBase);
        return courseBase;
    }

}

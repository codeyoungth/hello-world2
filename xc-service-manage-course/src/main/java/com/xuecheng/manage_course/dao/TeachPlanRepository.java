package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.Teachplan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeachPlanRepository extends JpaRepository<Teachplan,String> {

    //根据此方法，可以判断此节点是由有根节点
    List<Teachplan> findByCourseidAndParentid(String courseId,String parentId);

}

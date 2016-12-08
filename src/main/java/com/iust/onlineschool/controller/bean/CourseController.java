package com.iust.onlineschool.controller.bean;

import com.iust.onlineschool.enumaration.Field;
import com.iust.onlineschool.enumaration.Grade;
import com.iust.onlineschool.enumaration.RoleType;
import com.iust.onlineschool.model.bean.authentication.Authentication;
import com.iust.onlineschool.model.bean.authentication.AuthenticationDAO;
import com.iust.onlineschool.model.bean.course.Course;
import com.iust.onlineschool.model.bean.course.CourseDAO;
import com.iust.onlineschool.model.bean.course.CourseModel;
import com.iust.onlineschool.model.bean.membership.Membership;
import com.kendoui.spring.models.DataSourceResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohsen on 11/7/2016.
 */
@Controller
@RequestMapping(value = "/course")
@CrossOrigin
public class CourseController {

    @Autowired
    CourseDAO courses;
    @Autowired
    AuthenticationDAO authentications;
    AuthenticationCheck authenticationCheck;
    @RequestMapping(value = "/read", method = RequestMethod.POST)
    @ResponseBody
    public DataSourceResult read() {
        DataSourceResult dsr = new DataSourceResult();
        List<Course> result = courses.getAll();
        dsr.setData(result);
        return dsr;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = "application/json")
    public DataSourceResult create(@RequestBody CourseModel course, HttpServletRequest request) throws Exception {
        DataSourceResult dsr = new DataSourceResult();
        authenticationCheck = new AuthenticationCheck();
        RoleType role = authenticationCheck.isAuthenticated(course.getSessionId());
        if (role !=null && role.equals(RoleType.teacher)) {
            Course myCourse = new Course(new Membership(),course.getCreateDate(),course.isValidate()
                , Field.valueOf(course.getField()), Grade.valueOf(course.getGrade()),course.getWeekCount(),course.getName());
            courses.saveOrUpdate(myCourse);
            ArrayList<Course> courseArrayList = new ArrayList<Course>();
            courseArrayList.add(myCourse);
            dsr.setData(courseArrayList);
            return dsr;
        }
        else {
            return dsr;
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = "application/json")
    public DataSourceResult update(@RequestBody CourseModel course, HttpServletRequest request) throws Exception {
        DataSourceResult dsr = new DataSourceResult();
        authenticationCheck = new AuthenticationCheck();
        RoleType role = authenticationCheck.isAuthenticated(course.getSessionId());
        if (role !=null && role.equals(RoleType.teacher)) {
            Course myCourse = new Course(new Membership(),course.getCreateDate(),course.isValidate()
                    , Field.valueOf(course.getField()), Grade.valueOf(course.getGrade()),course.getWeekCount(),course.getName());
            courses.saveOrUpdate(myCourse);
            ArrayList<Course> courseArrayList = new ArrayList<Course>();
            courseArrayList.add(myCourse);
            dsr.setData(courseArrayList);
            return dsr;
        }
        else {
            return dsr;
        }
    }

}

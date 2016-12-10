package com.iust.onlineschool.controller.bean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iust.onlineschool.enumaration.Field;
import com.iust.onlineschool.enumaration.Grade;
import com.iust.onlineschool.enumaration.RoleType;
import com.iust.onlineschool.model.bean.Response;
import com.iust.onlineschool.model.bean.authentication.Authentication;
import com.iust.onlineschool.model.bean.authentication.AuthenticationDAO;
import com.iust.onlineschool.model.bean.course.Course;
import com.iust.onlineschool.model.bean.course.CourseDAO;
import com.iust.onlineschool.model.bean.course.CourseModel;
import com.iust.onlineschool.model.bean.membership.Membership;
import com.iust.onlineschool.model.bean.membership.MembershipDAO;
import com.kendoui.spring.models.DataSourceResult;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

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
    @Autowired
    MembershipDAO memberships;

    @RequestMapping(value = "/read", method = RequestMethod.POST)
    @ResponseBody
    public List<Course> read() {
        List<Course> result = courses.getAll();
        return result;
    }

    @RequestMapping(value = "/search")
    @ResponseBody
    public List<Course> search(@RequestParam("key") String key) {
        List<Course> result = courses.getAllBySearch(key);
        return result;
    }
    @RequestMapping(value = "/course")
    @ResponseBody
    public Course course(@RequestParam("id") long id) {
        Course course = courses.findById(id);
        return course;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Response create(@RequestBody CourseModel course, HttpServletRequest request) throws Exception {
        RoleType role = null;
        if (course.getSessionId() != "" && course.getSessionId() != null)
            role = isAuthenticated(course.getSessionId());
        if (role != null && role.equals(RoleType.teacher)) {
            try {
                Course myCourse = new Course(authentications.findBySession(course.getSessionId()).getMembership(), course.getCreateDate(), course.isValidate()
                        , Field.valueOf(course.getField()), Grade.valueOf(course.getGrade()), course.getWeekCount(), course.getName());
                courses.saveOrUpdate(myCourse);
            } catch (Exception e) {
                return new Response("inout does not correct");
            }
            return new Response("ok");
        } else {
            return new Response("inout does not correct");
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Response update(@RequestBody CourseModel course, HttpServletRequest request) throws Exception {
        RoleType role = null;
        if (course.getSessionId() != "" && course.getSessionId() != null)
            role = isAuthenticated(course.getSessionId());
        if (role != null && role.equals(RoleType.teacher)) {
            try {
                Course myCourse = new Course(authentications.findBySession(course.getSessionId()).getMembership(), course.getCreateDate(), course.isValidate()
                        , Field.valueOf(course.getField()), Grade.valueOf(course.getGrade()), course.getWeekCount(), course.getName());
                myCourse.setId(myCourse.getId());
                courses.saveOrUpdate(myCourse);
            } catch (Exception e) {
                return new Response("input does not correct");
            }
            return new Response("ok");
        } else {
            return new Response("input does not correct");
        }
    }

    @RequestMapping(value = "/addcourse", method = RequestMethod.POST, produces = "application/json")
    public DataSourceResult addCourse(@RequestBody CourseModel course, HttpServletRequest request) throws Exception {
        Authentication authentication;
        if (course!=null && course.getSessionId()!=null && course.getSessionId()!=""){
            Authentication strudent = authentications.findBySession(course.getSessionId());
            Membership m = memberships.findByUserName(strudent.getMembership().getUsername());
            Course c = courses.findById(course.getId());
            c.getStudents().add(m);
            //c.setId(0);
            try {
                courses.saveOrUpdate(c);
            }catch (Exception e)
            {
                System.out.println(e);
            }
        }
        return null;
    }

    public RoleType isAuthenticated(String sessionId) {
        Authentication authentication = null;
        if (sessionId != null && sessionId != "") {
            try {
                authentication = authentications.findBySession(sessionId);
            } catch (Exception e) {
                return null;
            }

            if (authentication != null)
                return authentication.getMembership().getRole();
            else return null;
        } else return null;

    }
}

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
import com.iust.onlineschool.model.bean.request.Request;
import com.iust.onlineschool.model.bean.request.RequestDAO;
import com.kendoui.spring.models.DataSourceRequest;
import com.kendoui.spring.models.DataSourceResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohsen on 11/7/2016.
 */
@Controller
@RequestMapping(value = "/request")
@CrossOrigin

public class RequestController {

    @Autowired
    RequestDAO requests;
    @Autowired
    AuthenticationDAO authentications;
    @Autowired
    CourseDAO courses;

    @RequestMapping(value = {"/home", "/"})
    public ModelAndView showHome(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Authentication authentication = authentications.findBySession(request.getSession().getId());
        ModelAndView result;
        if (authentication != null) {
            result = new ModelAndView("request");
            result.addObject("status", true);
            result.addObject("sessionId", authentication.getSessionId());
            return result;
        } else {
            response.sendRedirect("../");
            return null;
        }
    }
/*
    @RequestMapping(value = "/read", method = RequestMethod.POST)
    @ResponseBody
    public List<Course> read() {
        List<Course> result = courses.getAll();
        return result;
    }*/

    @RequestMapping(value = { "/read" }, method = { RequestMethod.POST }, produces = { "application/json" })
    @ResponseBody
    public DataSourceResult read(@RequestBody DataSourceRequest request) {
        return this.requests.getList(request);

    }


    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public DataSourceResult create(@RequestBody Request requestCourse, HttpServletRequest request) throws Exception {
        DataSourceResult dsr = new DataSourceResult();
        RoleType role = isAuthenticated(request.getSession().getId());
        if (role != null && (   role.equals(RoleType.teacher)|| role.equals(RoleType.admin)) ) {
            requestCourse.getCourse().setTeacher(requestCourse.getApplicant());
            courses.saveOrUpdate(requestCourse.getCourse());
            requests.saveOrUpdate(requestCourse);
            ArrayList<Request> courseArrayList = new ArrayList<Request>();
            courseArrayList.add(requestCourse);
            dsr.setData(courseArrayList);
            return dsr;
        } else {
            return dsr;
        }
    }

    @RequestMapping(value = "/destroy", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public DataSourceResult destroy(@RequestBody Request requestCourse, HttpServletRequest request) throws Exception {
        DataSourceResult dsr = new DataSourceResult();
        RoleType role = isAuthenticated(request.getSession().getId());
        if (role != null && (   role.equals(RoleType.teacher)|| role.equals(RoleType.admin)) ) {
            requests.delete(requestCourse);
            ArrayList<Request> courseArrayList = new ArrayList<Request>();
            courseArrayList.add(requestCourse);
            dsr.setData(courseArrayList);
            return dsr;
        } else {
            return dsr;
        }
    }

    public RoleType isAuthenticated(String sessionId) {
        Authentication authentication = null;
        if (sessionId != null) {
            try {
                authentication = authentications.findBySession(sessionId);
            } catch (Exception e) {
                System.out.println(e);
            }

            if (authentication != null)
                return authentication.getMembership().getRole();
            else return null;
        } else return null;

    }
}

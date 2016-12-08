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
import com.kendoui.spring.models.DataSourceResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    @RequestMapping(value = "/read", method = RequestMethod.POST)
    @ResponseBody
    public DataSourceResult read() {
        DataSourceResult dsr = new DataSourceResult();
        List<Request> result = requests.getAll();
        dsr.setData(result);
        return dsr;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = "application/json")
    public DataSourceResult create(@RequestBody CourseModel course, HttpServletRequest request) throws Exception {
        DataSourceResult dsr = new DataSourceResult();
        RoleType role = isAuthenticated(course.getSessionId());
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

    public RoleType isAuthenticated (String sessionId){
        Authentication authentication =null;
        if (sessionId!=null) {
            try {
                authentication = authentications.findBySession(sessionId);
            } catch (Exception e) {
                System.out.println(e);
            }

            if (authentication != null)
                return authentication.getMembership().getRole();
            else return null;
        }
        else return null;

    }
}

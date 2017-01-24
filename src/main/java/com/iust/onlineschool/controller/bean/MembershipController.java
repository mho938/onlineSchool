package com.iust.onlineschool.controller.bean;

import com.iust.onlineschool.model.bean.authentication.Authentication;
import com.iust.onlineschool.model.bean.authentication.AuthenticationDAO;
import com.iust.onlineschool.model.bean.authentication.AuthenticationModel;
import com.iust.onlineschool.model.bean.course.Course;
import com.iust.onlineschool.model.bean.course.CourseModel;
import com.iust.onlineschool.model.bean.membership.Membership;
import com.iust.onlineschool.model.bean.membership.MembershipDAO;
import com.iust.onlineschool.model.bean.person.Person;
import com.iust.onlineschool.model.bean.person.PersonDAO;
import com.kendoui.spring.models.DataSourceResult;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Mohsen on 12/10/2016.
 */
@Controller
@RequestMapping(value = "/membership")
@CrossOrigin
public class MembershipController {

    @Autowired
    AuthenticationDAO authentications;
    @Autowired
    MembershipDAO memberships;
    @Autowired
    PersonDAO persons;

    @RequestMapping(value = "/profile", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Membership getMembership(@RequestBody AuthenticationModel authenticationModel, HttpServletRequest request)throws Exception {
        Authentication authentication = authentications.findBySession(authenticationModel.getSessionId());
        Membership membership = memberships.findByUserName(authentication.getMembership().getUsername());
        return membership;
    }
    @RequestMapping(value = { "/teachers" }, method = { RequestMethod.GET }, produces = { "application/json" })
    @ResponseBody
    public Set<Person> readTeachers() {
        List<Membership>teachers= memberships.getAllTeachers();
        Set<Person>teachersPerson= new HashSet();
        for (Membership m:teachers) {
            teachersPerson.add(persons.findById(m.getId()));
        }
        return teachersPerson;
    }
}

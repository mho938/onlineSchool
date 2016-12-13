package com.iust.onlineschool.controller.bean;

import com.iust.onlineschool.model.bean.authentication.Authentication;
import com.iust.onlineschool.model.bean.authentication.AuthenticationDAO;
import com.iust.onlineschool.model.bean.authentication.AuthenticationModel;
import com.iust.onlineschool.model.bean.course.Course;
import com.iust.onlineschool.model.bean.course.CourseModel;
import com.iust.onlineschool.model.bean.membership.Membership;
import com.iust.onlineschool.model.bean.membership.MembershipDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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

    @RequestMapping(value = "/profile", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Membership course(@RequestBody AuthenticationModel authenticationModel, HttpServletRequest request)throws Exception {
        Authentication authentication = authentications.findBySession(authenticationModel.getSessionId());
        Membership membership = memberships.findByUserName(authentication.getMembership().getUsername());
        return membership;
    }
}

package com.iust.onlineschool.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iust.onlineschool.enumaration.Field;
import com.iust.onlineschool.enumaration.Grade;
import com.iust.onlineschool.enumaration.RoleType;

import com.iust.onlineschool.model.bean.Response;
import com.iust.onlineschool.model.bean.authentication.Authentication;
import com.iust.onlineschool.model.bean.authentication.AuthenticationDAO;
import com.iust.onlineschool.model.bean.authentication.AuthenticationModel;
import com.iust.onlineschool.model.bean.authentication.LoginAnswere;
import com.iust.onlineschool.model.bean.course.Course;
import com.iust.onlineschool.model.bean.membership.Membership;
import com.iust.onlineschool.model.bean.membership.MembershipDAO;
import com.iust.onlineschool.model.bean.membership.PersonModel;
import com.iust.onlineschool.model.bean.person.Person;
import com.iust.onlineschool.model.bean.person.PersonDAO;
import com.iust.onlineschool.utility.PathUtility;
import com.kendoui.spring.models.DataSourceResult;
import org.bouncycastle.jcajce.provider.asymmetric.ec.KeyFactorySpi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Role;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Controller
@CrossOrigin
public class AuthenticationController {
    @Autowired
    SessionLocaleResolver localeResolver;
    @Autowired
    private MembershipDAO memberships;
    @Autowired
    private AuthenticationDAO authentications;
    @Autowired
    private PersonDAO persons;


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public LoginAnswere loginGet(@RequestBody String user, HttpServletRequest request)
            throws IOException {
        LoginAnswere loginAnswere = null;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String userJson = gson.toJson(user);
        AuthenticationModel member = gson.fromJson(user, AuthenticationModel.class);
        Membership existMember = memberships.findByUserName(member.getUsername(), member.getPassword());
        Authentication mymember = null;
        if (existMember != null && existMember.getId() > 0) {
            if (member.getSessionId() != null) {
                mymember = authentications.findBySession(existMember, member.getSessionId());
                loginAnswere = new LoginAnswere(member.getSessionId(), member.getRole().name());
                return loginAnswere;
            } else {
                Authentication authentication = new Authentication();
                authentication.setId(0);
                authentication.setMembership(existMember);
                authentication.setSessionId(request.getSession().getId());
                try {
                    authentications.saveOrUpdate(authentication);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    loginAnswere = new LoginAnswere(authentication.getSessionId(), authentication.getMembership().getRole().name());
                } catch (Exception e) {
                    System.out.println(e);
                }
                return loginAnswere;
            }
        } else {
            loginAnswere = new LoginAnswere(null, null);
            return loginAnswere;
        }

    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST, produces = "application/json")
    @CrossOrigin
    public
    @ResponseBody
    Person signUp(@RequestBody PersonModel personModel, HttpServletResponse response, HttpServletRequest request) {
        Person person = null;
        DataSourceResult dsr = new DataSourceResult();
        try {
            boolean userExist = true;//memberships.isUsernameExist(personModel.getUsername());
            if (!userExist) {
                person = new Person(personModel.getName(), personModel.getFamily()
                        , personModel.getBalance(), personModel.getEmail(),
                        personModel.getPhoneNumber(), personModel.getNationalNumber()
                        , personModel.getBirthDate(), Field.valueOf(personModel.getField()),
                        Grade.valueOf(personModel.getGrade()));
                person.setUsername(personModel.getUsername());
                person.setPassword(personModel.getPassword());
                person.setRole(RoleType.valueOf(personModel.getRole()));
                persons.saveOrUpdate(person);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<Person> membershipArrayList = new ArrayList<Person>();
        membershipArrayList.add(person);
        dsr.setData(membershipArrayList);
        return person;
    }

    @RequestMapping(value = "/signout", method = RequestMethod.POST)
    @ResponseBody
    public Response signUp(@RequestBody AuthenticationModel authenticationModel, HttpServletRequest request)
            throws IOException {
        if (authenticationModel.getSessionId() != "" && authenticationModel.getSessionId() != null) {
            Authentication authentication = authentications.findBySession(authenticationModel.getSessionId());
            if (authentication!=null) {
                authentications.delete(authentication);
                return new Response("ok");
            }
            else
                return new Response("error!!");
        }
        return new Response("error!!");
    }
}
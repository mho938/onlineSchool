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
import com.iust.onlineschool.model.bean.membership.Membership;
import com.iust.onlineschool.model.bean.membership.MembershipDAO;
import com.iust.onlineschool.model.bean.person.PersonModel;
import com.iust.onlineschool.model.bean.person.Person;
import com.iust.onlineschool.model.bean.person.PersonDAO;
import com.kendoui.spring.models.DataSourceResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Controller
@CrossOrigin
public class AuthenticationController {
    @Autowired
    private MembershipDAO memberships;
    @Autowired
    private AuthenticationDAO authentications;
    @Autowired
    private PersonDAO persons;

    @RequestMapping(value = {"/"})
    public ModelAndView showHome(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ModelAndView result = new ModelAndView("login");
        result.addObject("status", false);
        return result;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ModelAndView login1(@RequestParam("username") String username, @RequestParam("password") String password, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        ModelAndView result = new ModelAndView("login");

        Membership existMember = null;
        try {
            existMember = memberships.findByUserName(username, password);
        } catch (Exception e) {
            result.addObject("status", false);
            return result;
        }
        if (existMember != null && existMember.getId() > 0) {
            if (existMember.getRole() == RoleType.valueOf("admin")) {
                Authentication authentication = authentications.findBySession(request.getSession().getId());
                if (authentication == null) {
                    authentication = new Authentication();
                    authentication.setId(0);
                    authentication.setMembership(existMember);
                    authentication.setSessionId(request.getSession().getId());
                }
                try {
                    authentications.saveOrUpdate(authentication);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                response.sendRedirect("request/home");

                return null;

            } else {
                result.addObject("status", false);
                return result;
            }
        } else {
            result.addObject("status", false);
            return result;
        }

    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public LoginAnswere login(@RequestBody String user, HttpServletRequest request)
            throws IOException {
        LoginAnswere loginAnswere = null;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        AuthenticationModel member = gson.fromJson(user, AuthenticationModel.class);
        Membership existMember = null;
        try {
            existMember = memberships.findByUserName(member.getUsername(), member.getPassword());
        } catch (Exception e) {
            loginAnswere = new LoginAnswere(null, null);
            return loginAnswere;
        }
        Authentication mymember = null;
        if (existMember != null && existMember.getId() > 0) {
            if (member.getSessionId() != null) {
                mymember = authentications.findBySession(existMember, member.getSessionId());
                loginAnswere = new LoginAnswere(mymember.getMembership().getRole().name(), mymember.getSessionId());
                return loginAnswere;
            } else {

                Authentication authentication = authentications.findBySession(request.getSession().getId());
                if (authentication == null) {
                    authentication = new Authentication();
                    authentication.setId(0);
                    authentication.setMembership(existMember);
                    authentication.setSessionId(request.getSession().getId());
                }
                try {
                    authentications.saveOrUpdate(authentication);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    loginAnswere = new LoginAnswere(authentication.getMembership().getRole().name(), authentication.getSessionId());
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
    PersonModel signUp(@RequestBody PersonModel personModel, HttpServletResponse response, HttpServletRequest request) {
        Person person = null;
        try {
            boolean userExist = memberships.isUsernameExist(personModel.getUsername());
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
                PersonModel answere = new PersonModel();
                answere.setRole(person.getRole().name());
                answere.setUsername(person.getUsername());
                return answere;
            } else {
                Membership m = memberships.findByUserName(personModel.getUsername());
                person = new Person(personModel.getName(), personModel.getFamily()
                        , personModel.getBalance(), personModel.getEmail(),
                        personModel.getPhoneNumber(), personModel.getNationalNumber()
                        , personModel.getBirthDate(), Field.valueOf(personModel.getField()),
                        Grade.valueOf(personModel.getGrade()));
                person.setUsername(personModel.getUsername());
                person.setPassword(personModel.getPassword());
                person.setRole(RoleType.valueOf(personModel.getRole()));
                person.setId(personModel.getId());
                persons.saveOrUpdate(person);
                PersonModel answere = new PersonModel();
                answere.setRole(m.getRole().name());
                answere.setUsername(m.getUsername());
                return answere;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        PersonModel answere = new PersonModel();
        answere.setRole("");
        answere.setUsername("");
        return answere;
    }

    @RequestMapping(value = "/signout", method = RequestMethod.POST)
    @ResponseBody
    public Response signout(@RequestBody AuthenticationModel authenticationModel, HttpServletRequest request)
            throws IOException {
        if (authenticationModel.getSessionId() != "" && authenticationModel.getSessionId() != null) {
            Authentication authentication = authentications.findBySession(authenticationModel.getSessionId());
            if (authentication != null) {
                authentications.delete(authentication);
                return new Response("ok");
            } else
                return new Response("error!!");
        }
        return new Response("error!!");
    }

    @RequestMapping(value = "/adminsignout", method = RequestMethod.POST)
    @ResponseBody
    public Response adminsignout(@RequestBody AuthenticationModel authenticationModel, HttpServletRequest request)
            throws IOException {

        if (authenticationModel.getSessionId() != "" && authenticationModel.getSessionId() != null) {
            Authentication authentication = authentications.findBySession(authenticationModel.getSessionId());
            if (authentication != null) {
                authentications.delete(authentication);
                return new Response("ok");
            } else {
                return new Response("error");
            }
        }
        return new Response("error");
    }
}
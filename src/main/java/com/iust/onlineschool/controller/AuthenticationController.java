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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public LoginAnswere login(@RequestBody String user, HttpServletRequest request)
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
                loginAnswere = new LoginAnswere(mymember.getMembership().getRole().name(),mymember.getSessionId());
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
                    loginAnswere = new LoginAnswere(authentication.getMembership().getRole().name(),authentication.getSessionId());
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
package com.iust.onlineschool.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iust.onlineschool.enumaration.Field;
import com.iust.onlineschool.enumaration.Grade;
import com.iust.onlineschool.enumaration.RoleType;

import com.iust.onlineschool.model.bean.authentication.Authentication;
import com.iust.onlineschool.model.bean.authentication.AuthenticationDAO;
import com.iust.onlineschool.model.bean.authentication.AuthenticationModel;
import com.iust.onlineschool.model.bean.course.Course;
import com.iust.onlineschool.model.bean.membership.Membership;
import com.iust.onlineschool.model.bean.membership.MembershipDAO;
import com.iust.onlineschool.model.bean.membership.PersonModel;
import com.iust.onlineschool.model.bean.person.Person;
import com.iust.onlineschool.model.bean.person.PersonDAO;
import com.iust.onlineschool.utility.PathUtility;
import com.kendoui.spring.models.DataSourceResult;
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

    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    @ResponseBody
    public String employee(Model model, HttpServletRequest request) {
        //System.out.println(UserDetail.getCurrentUser(request).getUsername());
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public String loginGet(@RequestBody String user, HttpServletRequest request)
            throws IOException {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String userJson = gson.toJson(user);
        AuthenticationModel member = gson.fromJson(user, AuthenticationModel.class);
        Membership existMember = memberships.findByUserName(member.getUsername(),member.getPassword());
        Authentication mymember = null;
        if (existMember!=null && existMember.getId()>0){
            if (member.getSessionId()!=null) {
                mymember = authentications.findBySession(existMember, member.getSessionId());
                return member.getSessionId();
            }
            else {
                Authentication authentication = new Authentication();
                authentication.setId(0);
                authentication.setMembership(existMember);
                authentication.setSessionId(request.getSession().getId());
                try {
                    authentications.saveOrUpdate(authentication);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return authentication.getSessionId();
            }
        }
        else {
            return "error";
        }

    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST, produces = "application/json")
    @CrossOrigin
    public  @ResponseBody Person signUp(@RequestBody PersonModel personModel, HttpServletResponse response, HttpServletRequest request) {
        Person person =null;
        DataSourceResult dsr = new DataSourceResult();
        try {
            boolean userExist = true;//memberships.isUsernameExist(personModel.getUsername());
            if (!userExist) {
                person = new Person(personModel.getName(),personModel.getFamily()
                                            ,personModel.getBalance(),personModel.getEmail(),
                                            personModel.getPhoneNumber(),personModel.getNationalNumber()
                                            ,personModel.getBirthDate(), Field.valueOf(personModel.getField()),
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
    @RequestMapping(value = "/about", method = RequestMethod.GET)
    public String about(HttpServletResponse response, HttpServletRequest request)
            throws IOException {
        return "about";
    }

    @RequestMapping(value = "/addPerson",
            method = RequestMethod.POST,
            headers = {"Content-type=application/json"})
    @ResponseBody
    public String addPerson(@RequestBody Person person) {
        System.out.println(person.toString());
        return "";
    }

    @RequestMapping(value = "/authentication", method = RequestMethod.GET)
    public void login(HttpServletRequest request,
                      HttpServletResponse response,
                      Map<String, Object> model) throws IOException {
        String serverId = request.getParameter("serverId");
        String sessionId = request.getParameter("sid");
        String home = request.getParameter("home");
        String lang = request.getParameter("lang");
        localeResolver.setLocale(request, response, new Locale(lang));
        if (home != null) {
            home = URLDecoder.decode(home, "UTF-8");
        } /*else {
            home = Config.i().getWelcomeRoot();
        }*/
       /* if (sessionId != null && sessionId.length() > 0) {
            tv.samim.tools.url.UrlCaller.jsonFix = true;
            tv.samim.tools.url.UrlAnswer<AuthenticationResult> res = tv.samim.tools.url.UrlCaller
                    .call(AuthenticationResult.class,
                            UrlCaller.ResponseType.json, sessionId, home
                                    + "/authentication");

            if (res != null && res.getResult().getStatus().equals("fail")) {
                model.put("showMsg", true);
                model.put("message", res.getResult().getText());// -can not
                // welcome
                model.put("status", "error");
                response.sendRedirect(PathUtility.buildRedirectUrlToHomePage(request, response));
                return;
            }

            if (res != null && res.getResult().isAthenticate()) {
                List<Groups> authority = res.getResult().getGroups();
                String userRoles = "ROLE_USER";
                if (authority != null) {
                    for (Groups groups : authority) {
                        List<Roles> roles = groups.getRoles();
                        if (roles != null)
                            for (Roles rol : roles) {
                                userRoles = rol.getName() + "," + userRoles;
                            }
                    }
                }
                List<GrantedAuthority> auth = AuthorityUtils.commaSeparatedStringToAuthorityList(userRoles);
                CurrentUser u = new CurrentUser(-1l, res.getResult().getUsername(), "******",
                        true, serverId, sessionId,
                        home,
                        localeResolver.resolveLocale(request),
                        auth);
                UsernamePasswordAuthenticationToken loggedIn = new UsernamePasswordAuthenticationToken(
                        u, u.getPassword(), u.getAuthorities());
                loggedIn.setDetails(u);
                SecurityContextHolder.getContext().setAuthentication(loggedIn);

                response.sendRedirect(PathUtility.buildRedirectUrlToHomePage(request, response));
                return;
            }

            response.sendRedirect(PathUtility.buildRedirectUrlToHomePage(
                    request, response));
            return;
        }*/
        response.sendRedirect( "index");

    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    @ResponseBody
    public String signUp(@RequestBody String sessionId, HttpServletRequest request)
            throws IOException {

        Authentication authentication = authentications.findBySession(sessionId);
        authentications.delete(authentication);
        return "ok";
    }

}
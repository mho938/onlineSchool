package com.iust.onlineschool.controller.bean;

import com.iust.onlineschool.model.bean.membership.Membership;
import com.iust.onlineschool.model.bean.membership.MembershipDAO;
import com.kendoui.spring.models.DataSourceResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

/**
 * Created by Mohsen on 11/7/2016.
 */
@Controller
@RequestMapping(value = "/user")
@CrossOrigin

public class MembershipController {

    @Autowired
    MembershipDAO memberships;


    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    @ResponseBody
    public DataSourceResult create(@RequestBody Model model, HttpServletRequest request) throws Exception {
        DataSourceResult dsr = new DataSourceResult();
        System.out.println(model.toString());
        /*memberships.saveOrUpdate(membership);
        ArrayList<Membership> membershipArrayList = new ArrayList<Membership>();
        membershipArrayList.add(membership);
        dsr.setData(membershipArrayList);
        */return dsr;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public DataSourceResult update(@RequestBody Membership membership, HttpServletRequest request) throws Exception {
        DataSourceResult dsr = new DataSourceResult();
        memberships.saveOrUpdate(membership);
        ArrayList<Membership> membershipArrayList = new ArrayList<Membership>();
        membershipArrayList.add(membership);
        dsr.setData(membershipArrayList);
        return dsr;
    }

}

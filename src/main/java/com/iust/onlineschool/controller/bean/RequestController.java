package com.iust.onlineschool.controller.bean;

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

    @PreAuthorize("hasAnyRole('superuser')")
    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public String Request(Model model, HttpServletRequest request) {
        return "request";
    }

    @PreAuthorize("hasAnyRole('superuser')")
    @RequestMapping(value = "/read", method = RequestMethod.POST)
    @ResponseBody
    public DataSourceResult read() {
        DataSourceResult dsr = new DataSourceResult();
        List<Request> result = requests.getAll();
        dsr.setData(result);
        return dsr;
    }

    @PreAuthorize("hasRole('superuser')")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public DataSourceResult create(Request _request, HttpServletRequest request) throws Exception {
        DataSourceResult dsr = new DataSourceResult();
        requests.saveOrUpdate(_request);
        ArrayList<Request> requestArrayList = new ArrayList<Request>();
        requestArrayList.add(_request);
        dsr.setData(requestArrayList);
        return dsr;
    }

    @PreAuthorize("hasRole('superuser')")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public DataSourceResult update(@RequestBody Request _request, HttpServletRequest request) throws Exception {
        DataSourceResult dsr = new DataSourceResult();
        requests.saveOrUpdate(_request);
        ArrayList<Request> requestArrayList = new ArrayList<Request>();
        requestArrayList.add(_request);
        dsr.setData(requestArrayList);
        return dsr;
    }

}

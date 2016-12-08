package com.iust.onlineschool.controller.bean;

import com.kendoui.spring.models.DataSourceResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import tv.samim.tools.error.LocalizedException;
import tv.samim.tools.error.MessageHelper;
import tv.samim.tools.error.SpringFieldValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mohsen.oloumi on 15/11/2015.
 */
@CrossOrigin

public class BaseController {

    protected SpringFieldValidator validator;
    protected MessageHelper messageHelper;

    protected void internal(DataSourceResult dsr) throws LocalizedException {
        validator.throwLocalizedException("server.internal.error", dsr);
    }

    protected void error(String code, DataSourceResult dsr) throws LocalizedException {
        validator.throwLocalizedException(code, dsr);
    }

    protected <T> void stack(DataSourceResult dsr, T entity) {
        List<T> stack = new ArrayList<>();
        stack.add(entity);
        dsr.setData(stack);
    }

    @ExceptionHandler(LocalizedException.class)
    protected ResponseEntity<DataSourceResult> handleLocalizedException(LocalizedException e) {
        if (e.getReflector() != null && e.getReflector() instanceof DataSourceResult) {
            DataSourceResult dsr = (DataSourceResult) e.getReflector();
            if (e.getLocalizedReason() != null)
                dsr.addError(e.getLocalizedReason());
            else
                dsr.addError(messageHelper.getStringByCode(e.getReasonCode()));
            return new ResponseEntity<>(dsr, HttpStatus.OK);
        }
        return null;
    }

    @ExceptionHandler(AccessDeniedException.class)
    @PreAuthorize("isAnonymous()")
    public void handleResourceNotFoundException(Exception ex, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String contentType = request.getContentType();
        if (contentType != null && contentType.startsWith("application/json"))
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        else
            response.sendRedirect(request.getContextPath() + "/login");
    }
}

<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>

<c:set var="req_username">
    username
</c:set>
<c:set var="req_password">
    password
</c:set>


<form id="loginForm" enctype='application/json' role="form" name="form"
      action="/online-school/" method="post">

    <c:if test="${not empty SPRING_SECURITY_LAST_EXCEPTION}">
        <div class="alert alert-danger">
            <div class="validation-summary-errors">
                <ul style="margin:0px;">
                    <li>login faild</li>
                </ul>
            </div>
        </div>
        <c:remove var="SPRING_SECURITY_LAST_EXCEPTION" scope="session" />
    </c:if>

    <div class="form-group">
        <label for="username"> نام کاربری
        </label>
        <div class="input-group">
            <span class="input-group-addon"><i class="fa fa-user"></i></span>
            <input id="username"
                   type="text"
                   name="username"
                   class="form-control"
                   data-val-required="نیازمند نام کاربری"
                   style="direction: ltr;"
                   maxlength="45" />
        </div>
        <span data-for='username' class='k-invalid-msg'></span>
    </div>

    <div class="form-group">
        <label for="password"> رمز عبور
        </label>
        <div class="input-group">
            <span class="input-group-addon"><i class="fa fa-key"></i></span>
            <input id="password"
                   type="password"
                   name="password"
                   class="form-control"
                   data-val-required="نیازمند رمز عبور"
                   style="direction: ltr;"
                   value=""
                   maxlength="45" />
        </div>
        <span data-for='password' class='k-invalid-msg'></span>
    </div>

    <div>
        <button id="submit" class="k-button pull-btn" type="submit"
                onclick="return validator.validate();"
                style="padding-top: 2px; padding-bottom: 2px;">
            login
        </button>
    </div>
</form>
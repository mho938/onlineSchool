<%@page import="tv.samim.tools.logging.Logger"%>
<%@ page language="java" contentType="text/html; UTF-8"
	pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	//     User user = CustomUserDetailsService.getCurrentUser();
%>
<style type="text/css">
.main-menu a {
	cursor: pointer;
}
</style>
<s:url value="/" var="rootUrl" />
<%
	String home = null;

		//home = UserDetail.getCurrentUser(request).getHome() + "index.jsp";
	boolean is_auth = false;

	if (is_auth == true) {
%>
<div class="collapse navbar-collapse" id="main-header">
	<ul class="main-menu nav navbar-nav main-header">
		<li class="nav"><a href="<%=home%>" style="padding: 17px 15px;">
		s		<i class="fa fa-home fa-lg"></i>
		</a></li>

		<%
			//if (UserDetail.hasRole(request, "admin")) {
		%>

		<li class="nav"><a href="${rootUrl}about"> درباره ما
		</a></li>


<%

	} else {
%>
<ul class="main-menu nav navbar-nav main-header">
	<li class="nav"><span> مدیریت مدرسه ی آنلاین</span></li>

</ul>
<%
	}
%>


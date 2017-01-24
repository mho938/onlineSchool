<%@ page language="java" contentType="text/html; UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>

<!DOCTYPE html>
<html lang="true">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <s:url value="/" var="url"/>
    <link type="image/x-icon" href="<s:url value="/resources/favicon.ico" />"
          rel="shortcut icon">
    <meta name="description" content="">

    <link href="<s:url value="/resources/cdn/v0.2/css/lib/font-awesome.css"/>"
          rel="stylesheet"/>
    <link href="<s:url value="/resources/cdn/v0.2/css/lib/kendo.common.css"/>" rel="stylesheet"/>
    <link href="<s:url value="/resources/cdn/v0.2/css/lib/kendo.default.css"/>" rel="stylesheet"/>

    <link href="<s:url value="/resources/cdn/v0.2/css/lib/fa/bootstrap.css"/>" rel="stylesheet"/>
    <link href="<s:url value="/resources/cdn/v0.2/css/lib/fa/kendo.rtl.css"/>" rel="stylesheet"/>
    <link href="<s:url value="/resources/cdn/v0.2/css/lib/fa/messagebox.css"/>" rel="stylesheet"/>
    <link href="<s:url value="/resources/cdn/v0.2/css/mainStyle.css"/>" rel="stylesheet"/>
    <link href="<s:url value="/resources/cdn/v0.2/css/fa/mainStyle.css"/>" rel="stylesheet"/>

    <style>
        html, body {height: 100%;}
        .has-warning {
            background: none repeat scroll 0 0 #FFF4C9 !important;
            border-color: #FFE79E !important;
            color: #635145 !important;
        }

        .k-button-icon span {
            margin-left: 0.5rem;
            margin-right: 0rem;
            vertical-align: middle;
            display: inline-block;
            font-family: FontAwesome;
            font-style: normal;
            font-weight: 400;
            height: 16px;
            line-height: 1;
            overflow: hidden;
            text-align: center;
            width: 16px;
            color: rgba(0, 0, 0, 0);
            height: auto;
            line-height: 0;
        }

        .k-button-icon span:before {
            color: #2E2E2E;
            line-height: 1;
            visibility: visible;
        }

        .k-icon-delete span:before {
            content: "\f00d";
        }

        .k-button-icon span {
            margin-left: 0.5rem;
            margin-right: 0rem;
            vertical-align: middle;
            display: inline-block;
            font-family: FontAwesome;
            font-style: normal;
            font-weight: 400;
            height: 16px;
            line-height: 1;
            overflow: hidden;
            text-align: center;
            width: 16px;
            color: rgba(0, 0, 0, 0);
            height: auto;
            line-height: 0;
        }
        #grid {min-height:181px;}
    </style>


    <script src="<s:url value="/resources/cdn/v0.2/scripts/lib/jquery.js"/>"></script>
    <script src="<s:url value="/resources/cdn/v0.2/scripts/lib/kendo.all.js"/>"></script>
    <script src="<s:url value="/resources/cdn/v0.2/scripts/lib/kendo.aspnetmvc.js"/>"></script>
    <script src="<s:url value="/resources/cdn/v0.2/scripts/lib/kendo.helper.js"/>"></script>
    <script src="<s:url value="/resources/cdn/v0.2/scripts/lib/moment.js"/>"></script>

    <script src="<s:url value="/resources/cdn/v0.2/scripts/lib/fa/bootstrap.js"/>"></script>
    <script src="<s:url value="/resources/cdn/v0.2/scripts/lib/fa/dictionary.js"/>"></script>
    <script src="<s:url value="/resources/cdn/v0.2/scripts/lib/fa/kendo.dictionary.js"/>"></script>
    <script src="<s:url value="/resources/cdn/v0.2/scripts/lib/fa/messagebox.js"/>"></script>
    <script src="<s:url value="/resources/cdn/v0.2/scripts/lib/fa/moment.js"/>"></script>

    <script src="<s:url value="/resources/cdn/v0.2/scripts/lib/bootstrap-hover-dropdown.js"/>"></script>
    <script src="<s:url value="/resources/cdn/v0.2/scripts/lib/json.js"/>"></script>
    <script type="text/javascript">
        String.prototype.format = function() {
            var formatted = this;
            for (var i = 0; i < arguments.length; i++) {
                var arg = arguments[i];
                formatted = formatted.replace("{" + i + "}", arg);
            }
            return formatted;
        };

    </script>
    <script type="text/javascript" src="<s:url value="/resources/cdn/v0.2/scripts/lib/jquery.cookie.js"/>"></script>
    <tiles:insertAttribute name="head"/>
</head>
<body  class="k-rtl" >
<header>
    <nav id="menu" class="navbar navbar-default navbar-fixed-top"
         role="navigation">
        <div class="container-fluid">
            <ul class="secondary-menu nav navbar-nav">
                    <tiles:insertAttribute name="status"/>
                <li class="nav">
                    <img class="mainLogo"
                           <%-- <%if (!Config.i().isStandalone()) {%>
                         src="<%=UserDetail.getCurrentUser(request).getHome()%>pic/second-logo.png?<%=java.util.Calendar.getInstance().getTimeInMillis()%>"
                            <%} else {%>
                         src="<s:url value="/resources/pic/second-logo.png" />" <%}%>--%>
                         alt="logo" />
                </li>
            </ul>
            <div class="navbar-header">
                <button data-target="#main-header" data-toggle="collapse"
                        type="button" class="navbar-toggle pull-right collapsed">
                    <i class="fa fa-bars"></i>
                </button>
            </div>
            <tiles:insertAttribute name="menu"/>
        </div>
    </nav>
</header>

<section class="container" id="mainPanel">
    <tiles:insertAttribute name="content"/>
</section>


<tiles:insertAttribute name="footer"/>
</body>
</html>
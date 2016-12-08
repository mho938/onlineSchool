package com.iust.onlineschool.utility;

import org.springframework.security.web.PortResolver;
import org.springframework.security.web.PortResolverImpl;
import org.springframework.security.web.util.RedirectUrlBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * @author mojtaba khallash and AhmadReza Nazemi
 */
public class PathUtility {

    public static String localAddress = null;
    private static final PortResolver portResolver = new PortResolverImpl();

    public static String getUploadPath() {
        return localAddress + "resources" +
                File.separator + "upload" +
                File.separator + "temp" +
                File.separator;
    }

    public static String getFfmpegPath() {
        return localAddress + "WEB-INF" +
                File.separator + "ffmpeg" +
                File.separator;
    }

    public static File getSitePath() { return getSitePath(""); }
    public static File getSitePath(String context) {
        File file = new File(localAddress);
        return new File(file.getParentFile().getAbsolutePath() + File.separator + context);
    }

    public static String buildRedirectUrlToLoginPage(
            HttpServletRequest request, HttpServletResponse response) {
        int serverPort = portResolver.getServerPort(request);
        if (serverPort != 0) {
            RedirectUrlBuilder urlBuilder = buildURl(request, serverPort);
            urlBuilder.setPathInfo("/login");
            return urlBuilder.getUrl();
        }
        return null;
    }

    public static String buildRedirectUrlToCurrentPath(
            HttpServletRequest request, HttpServletResponse response) {
        int serverPort = portResolver.getServerPort(request);
        if (serverPort != 0) {
            RedirectUrlBuilder urlBuilder = buildURl(request, serverPort);
            urlBuilder.setServletPath(request.getServletPath());
            urlBuilder.setPathInfo(request.getPathInfo());
            urlBuilder.setQuery(request.getQueryString());
            return urlBuilder.getUrl();
        }
        return null;
    }

    public static String buildRedirectUrlToLogoutPage(
            HttpServletRequest request, HttpServletResponse response) {
        int serverPort = portResolver.getServerPort(request);
        if (serverPort != 0) {
            RedirectUrlBuilder urlBuilder = buildURl(request, serverPort);
            urlBuilder.setPathInfo("/signout");
            return urlBuilder.getUrl();
        }
        return null;
    }

    public static String buildRedirectUrlToHomePage(HttpServletRequest request,
                                                    HttpServletResponse response) {
        int serverPort = portResolver.getServerPort(request);
        if (serverPort != 0) {
            RedirectUrlBuilder urlBuilder = buildURl(request, serverPort);
            urlBuilder.setPathInfo("");
            return urlBuilder.getUrl();
        }
        return null;
    }

    public static String buildRedirectUrlToAccessLoginAuthPage(
            HttpServletRequest request, HttpServletResponse response) {
        int serverPort = portResolver.getServerPort(request);
        if (serverPort != 0) {
            RedirectUrlBuilder urlBuilder = buildURl(request, serverPort);
            urlBuilder.setPathInfo("/access-auth");
            return urlBuilder.getUrl();
        }
        return null;
    }

    public static String buildURl(HttpServletRequest request) {
        return buildURl(request, "");
    }
    public static String buildURl(HttpServletRequest request,
                                  String contextPath) {
        int serverPort = portResolver.getServerPort(request);
        if (serverPort != 0) {
            RedirectUrlBuilder urlBuilder = buildURl(request, serverPort, contextPath);
            urlBuilder.setPathInfo("");
            return urlBuilder.getUrl();
        }
        return null;
    }

    private static RedirectUrlBuilder buildURl(HttpServletRequest request,
                                               int serverPort) {
        return buildURl(request, serverPort, request.getContextPath());
    }
    private static RedirectUrlBuilder buildURl(HttpServletRequest request,
                                               int serverPort,
                                               String contextPath) {
        RedirectUrlBuilder urlBuilder = new RedirectUrlBuilder();
        urlBuilder.setScheme(request.getScheme());
        urlBuilder.setServerName(request.getServerName());
        urlBuilder.setPort(serverPort);
        urlBuilder.setContextPath(contextPath);
        return urlBuilder;
    }
}

package be.kennyverheyden.processors;

import jakarta.servlet.http.HttpServletRequest;

// Reset forgotten password
public class Utility {
    public static String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }
}
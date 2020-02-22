package net.ddns.la90zy.mnemo.faces.boundary;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@Model
public class LogoutBacking {

    @Inject
    private HttpServletRequest httpServletRequest;

    public String submit() throws ServletException{
        httpServletRequest.logout();
        httpServletRequest.getSession().invalidate();
        return "/index?faces-redirect=true";
    }
}

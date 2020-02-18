package net.ddns.la90zy.mnemo.faces.boundary;

import javax.enterprise.inject.Model;
import javax.faces.annotation.ManagedProperty;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.SecurityContext;
import javax.security.enterprise.authentication.mechanism.http.AuthenticationParameters;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Model
public class Login {

    private String password;

    @Inject
    @ManagedProperty("#{param.login}")
    private String login;

    @Inject
    @ManagedProperty("#{param.new}")
    private boolean isNew;

    @Inject
    private Flash flash;

    @Inject
    private SecurityContext securityContext;

    @Inject
    private ExternalContext externalContext;

    @Inject
    private FacesContext facesContext;

    public void submit() throws IOException {
        switch(continueAuthentication()) {
            case SEND_CONTINUE:
                facesContext.responseComplete();
                break;
            case SEND_FAILURE:
                facesContext.addMessage(null, new FacesMessage(
                        FacesMessage.SEVERITY_ERROR, "Log in failed.", null));
                break;
            case SUCCESS:
                flash.setKeepMessages(true);
                facesContext.addMessage(null, new FacesMessage(
                        FacesMessage.SEVERITY_INFO, "Log in success.", null));
                externalContext.redirect(
                        externalContext.getRequestContextPath() + "/mnemo/index.xhtml");
                break;
            case NOT_DONE:
        }
    }

    private AuthenticationStatus continueAuthentication() {
        return securityContext.authenticate(
                (HttpServletRequest) externalContext.getRequest(),
                (HttpServletResponse) externalContext.getResponse(),
                AuthenticationParameters
                        .withParams()
                        .newAuthentication(isNew)
                        .credential(new UsernamePasswordCredential(login, password))
        );
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

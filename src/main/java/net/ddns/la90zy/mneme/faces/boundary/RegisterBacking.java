package net.ddns.la90zy.mneme.faces.boundary;

import net.ddns.la90zy.mneme.syncservice.boundary.MnemeFacade;

import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.IOException;

@Model
public class RegisterBacking {

    private UIComponent loginField;

    @NotNull
    private String login;

    @Email
    private String email;

    @NotNull
    private String password;

    @Inject
    private Flash flash;

    @Inject
    private FacesContext facesContext;

    @Inject
    private ExternalContext externalContext;

    @Inject
    private MnemeFacade mnemeFacade;

    public void submit() throws IOException {
        if(mnemeFacade.isLoginTaken(login)) {
            facesContext.addMessage(loginField.getClientId(facesContext), new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "This login already taken.", null));
        } else {
            mnemeFacade.registerUser(login, email, password);
            flash.setKeepMessages(true);
            facesContext.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_INFO,
                    "You successfully registered. Now you can log in.",
                    null));
            externalContext.redirect(
                    externalContext.getRequestContextPath() +
                            "/login.xhtml?faces-redirect=true&new=true&login=" +
                            login);
        }
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UIComponent getLoginField() {
        return loginField;
    }

    public void setLoginField(UIComponent loginField) {
        this.loginField = loginField;
    }
}

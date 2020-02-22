package net.ddns.la90zy.mnemo;

import net.ddns.la90zy.mnemo.syncservice.control.HostProviderService;
import net.ddns.la90zy.mnemo.syncservice.control.UserService;
import net.ddns.la90zy.mnemo.syncservice.control.HostProviderClientServiceFactory;
import net.ddns.la90zy.mnemo.syncservice.entity.HostProvider;
import net.ddns.la90zy.mnemo.syncservice.entity.User;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.*;

@Singleton
@Startup
public class DatabaseBootstrapBean {

    @Inject
    private UserService userService;

    @Inject
    private HostProviderService hostProviderService;

    @Inject
    @ConfigProperty(name = "net.ddns.la90zy.mnemo.defaultadminlogin",
            defaultValue = "admin")
    private String defaultAdminLogin;

    @Inject
    @ConfigProperty(name = "net.ddns.la90zy.mnemo.defaultadminpassword",
            defaultValue = "admin")
    private String defaultAdminPassword;

    @Inject
    private HostProviderClientServiceFactory hostProviderClientServiceFactory;

    @PostConstruct
    private void init() {
        if (!userService.getUserByLogin(defaultAdminLogin).isPresent()) createDefaultAdmin();
        addNewHostProviders();
    }

    private void addNewHostProviders() {
        List<HostProvider> existingHostProviders = hostProviderService.getHostProviders();
        hostProviderClientServiceFactory.getHostProviderNames().stream()
                .map(HostProvider::create)
                .filter(h -> !existingHostProviders.contains(h))
                .forEach(hostProviderService::saveHostProvider);
    }

    private void createDefaultAdmin() {
        User user = User.builder()
                .login(defaultAdminLogin)
                .password(defaultAdminPassword)
                .group(User.Group.ADMIN)
                .build();
        userService.saveUser(user);
    }

}

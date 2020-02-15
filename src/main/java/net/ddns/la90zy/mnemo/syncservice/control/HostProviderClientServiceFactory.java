package net.ddns.la90zy.mnemo.syncservice.control;

import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class HostProviderClientServiceFactory {

    @Inject
    @Any
    private Instance<HostProviderClientService> hostProviderClientServices;

    private final List<String> hostProviderNames = new ArrayList<>();

    @PostConstruct
    private void init() {
        hostProviderClientServices.stream()
                .map(HostProviderClientService::getName)
                .forEach(hostProviderNames::add);
    }

    public HostProviderClientService getHostProviderClientService(String name) {
        return hostProviderClientServices.select(new HostProviderNameLiteral(name)).get();
    }

    public List<String> getHostProviderNames() {
        return new ArrayList<>(hostProviderNames);
    }
}

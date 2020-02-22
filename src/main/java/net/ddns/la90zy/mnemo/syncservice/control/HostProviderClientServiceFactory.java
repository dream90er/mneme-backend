package net.ddns.la90zy.mnemo.syncservice.control;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Factory bean for host provider client services. On startup collect names of all supported host providers.
 *
 * @author DreameR
 */
@ApplicationScoped
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

    /**
     * Get host provider client service by name.
     * @param name host provider name specified in qualifier annotation.
     * @return provider client service bean.
     */
    public HostProviderClientService getHostProviderClientService(String name) {
        return hostProviderClientServices.select(new HostProviderNameLiteral(name)).get();
    }

    /**
     *
     * @return List of all supported host providers.
     */
    public List<String> getHostProviderNames() {
        return new ArrayList<>(hostProviderNames);
    }
}

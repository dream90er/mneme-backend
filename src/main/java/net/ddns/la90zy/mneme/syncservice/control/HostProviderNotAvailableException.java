package net.ddns.la90zy.mneme.syncservice.control;

import javax.ejb.ApplicationException;

/**
 * Exception thrown by {@link HostProviderClientService}.
 *
 * @see HostProviderClientService
 * @author DreameR
 */
@ApplicationException(rollback = true)
public class HostProviderNotAvailableException extends MnemeException {

    private final String hostProviderName;

    public HostProviderNotAvailableException(String message, String hostProviderName) {
        super(message);
        this.hostProviderName =hostProviderName;
    }

    public String getHostProviderName() {
        return hostProviderName;
    }
}

package net.ddns.la90zy.mneme.syncservice.control;

import javax.ejb.ApplicationException;

/**
 * Exception thrown by {@link HostProviderClientService}.
 *
 * @see HostProviderClientService
 * @author DreameR
 */
@ApplicationException
public class PlaylistNotFoundException extends MnemeException {

    private final String playlistExternalId;

    private final String hostProviderName;

    public PlaylistNotFoundException(String message, String playlistExternalId, String hostProviderName) {
        super(message);
        this.playlistExternalId = playlistExternalId;
        this.hostProviderName = hostProviderName;
    }

    public String getPlaylistExternalId() {
        return playlistExternalId;
    }

    public String getHostProviderName() {
        return hostProviderName;
    }
}

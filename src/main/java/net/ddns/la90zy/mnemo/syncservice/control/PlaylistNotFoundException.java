package net.ddns.la90zy.mnemo.syncservice.control;

import javax.ejb.ApplicationException;

@ApplicationException
public class PlaylistNotFoundException extends MnemoException{

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

package net.ddns.la90zy.mnemo.syncservice.control;

import net.ddns.la90zy.mnemo.syncservice.entity.Playlist;
import net.ddns.la90zy.mnemo.syncservice.entity.Track;

import java.util.List;

public interface HostProviderClientService {

    List<Track> getPlaylistTracks(String playlistId, String accessToken);
    Playlist getPlaylistInfo(String playlistId, String accessToken);
    String getAccessToken(String refreshToken);
    default String getName() {
        return this.getClass().getAnnotation(HostProviderName.class).value();
    }
}

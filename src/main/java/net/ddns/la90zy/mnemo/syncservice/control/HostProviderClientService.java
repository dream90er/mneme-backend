package net.ddns.la90zy.mnemo.syncservice.control;

import net.ddns.la90zy.mnemo.syncservice.entity.Playlist;
import net.ddns.la90zy.mnemo.syncservice.entity.Track;

import java.util.List;

/**
 * Interface for client services of all supported host providers.
 *
 * @author DreameR
 */
public interface HostProviderClientService {

    /**
     * Get all tracks of given playlist.
     * @param playlistId host provider specific playlist Id.
     * @param accessToken access token of concrete user, received from host provider. Blank if not provided.
     * @throws HostProviderNotAvailableException if host provider API endpoint not available.
     * @throws PlaylistNotFoundException if host provider send blank or error response.
     * @return List of Track entities.
     */
    List<Track> getPlaylistTracks(String playlistId, String accessToken);

    /**
     * Get playlist information(title, owner).
     * @param playlistId host provider specific playlist Id.
     * @param accessToken access token of concrete user, received from host provider. Blank if not provided.
     * @throws HostProviderNotAvailableException if host provider API endpoint not available.
     * @throws PlaylistNotFoundException if host provider send blank or error response.
     * @return Playlist entity, populated with title and owner fields.
     */
    Playlist getPlaylistInfo(String playlistId, String accessToken);

    /**
     * Get access token of concrete user.
     * @param refreshToken user refresh token received from host provider.
     * @throws HostProviderNotAvailableException if host provider API endpoint not available.
     * @return access token.
     */
    String getAccessToken(String refreshToken);

    /**
     * Get host provider name from qualifier annotation.
     * @return host provider name.
     */
    default String getName() {
        return this.getClass().getAnnotation(HostProviderName.class).value();
    }
}

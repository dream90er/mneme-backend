package net.ddns.la90zy.mnemo.youtubeclientservice.control;

import java.net.URI;

/**
 * Interface for Youtube request URL builders.
 *
 * @author DreameR
 */
public interface YoutubeRequestBuilder {
    /**
     * Build URL query for first page of
     * <a href="https://developers.google.com/youtube/v3/docs/playlistItems/list">playlistItems.list</a>
     * Youtube API method response with specified playlistId.
     * @param playlistId Id of Youtube playlist.
     * @param accessToken user specific access token. Blank string if not provided.
     * @return URL query to playlistItems.list Youtube API method.
     */
    URI buildPlaylistPageRequest(String playlistId, String accessToken);

    /**
     * Build URL query for token specified page of
     * <a href="https://developers.google.com/youtube/v3/docs/playlistItems/list">playlistItems.list</a>
     * Youtube API method response with specified playlistId.
     * @param playlistId Id of Youtube playlist.
     * @param pageToken token for concrete page of response.
     *                  Can be obtained from nextPageToken and prevPageToken properties of Youtube API response.
     * @param accessToken user specific access token. Blank string if not provided.
     * @return URL query to playlistItems.list Youtube API method.
     */
    URI buildPlaylistPageRequest(String playlistId, String pageToken, String accessToken);

    /**
     * Build URL query for
     * <a href="https://developers.google.com/youtube/v3/docs/playlists/list">playlists.list</a>
     * Youtube API method with specified playlistId.
     * @param playlistId Id of Youtube playlist.
     * @param accessToken user specific access token. Blank string if not provided.
     * @return URL query for playlists.list Youtube API method.
     */
    URI buildPlaylistInfoRequest(String playlistId, String accessToken);
}

package net.ddns.la90zy.mnemo.youtubeclientservice.control;

import net.ddns.la90zy.mnemo.syncservice.entity.Playlist;
import net.ddns.la90zy.mnemo.youtubeclientservice.entity.PlaylistPage;

import javax.json.JsonObject;

/**
 * Interface for Youtube response parsers.
 *
 * @author DreameR
 */
public interface YoutubeResponseParser {
    /**
     * Parse playlist items and token for next page from Youtube API response of
     * <a href="https://developers.google.com/youtube/v3/docs/playlistItems/list">playlistItems.list</a>
     * method.
     * @param page response from Youtube API in JSON format.
     * @throws YoutubeParserException if response blank or error.
     * @return PlaylistPage entity populated with playlist items and token for next page(if presented).
     */
    PlaylistPage parsePlaylistPage(JsonObject page);

    /**
     * Parse playlist title and owner from Youtube API response of
     * <a href="https://developers.google.com/youtube/v3/docs/playlists/list">playlists.list</a>
     * method.
     * @param info response from Youtube API in JSON format.
     * @throws YoutubeParserException if response blank or error.
     * @return Playlist entity populated with title and owner.
     */
    Playlist parsePlaylistInfo(JsonObject info);
}

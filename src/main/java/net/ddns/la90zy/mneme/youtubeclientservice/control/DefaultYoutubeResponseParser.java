package net.ddns.la90zy.mneme.youtubeclientservice.control;

import net.ddns.la90zy.mneme.syncservice.entity.Playlist;
import net.ddns.la90zy.mneme.syncservice.entity.Track;
import net.ddns.la90zy.mneme.youtubeclientservice.entity.PlaylistPage;

import javax.json.JsonObject;
import javax.json.JsonValue;
import java.util.ArrayList;
import java.util.List;

/**
 * Default implementation of {@link YoutubeResponseParser} interface.
 *
 * @author DreameR
 */
public class DefaultYoutubeResponseParser implements YoutubeResponseParser {

    @Override
    public Playlist parsePlaylistInfo(JsonObject info) {
        if (isError(info)) {
            throw new YoutubeParserException("Playlist not found or access forbidden.");
        }
        JsonObject snippet = info.getJsonArray("items")
                .getJsonObject(0)
                .getJsonObject("snippet");
        String title = snippet.getString("title");
        String ownerId = snippet.getString("channelId");
        return Playlist.builder()
                .title(title)
                .ownerId(ownerId)
                .build();
    }

    @Override
    public PlaylistPage parsePlaylistPage(JsonObject page) {
        if (page.containsKey("error")) {
            throw new YoutubeParserException("Playlist not found or access forbidden.");
        }
        List<Track> tracks = new ArrayList<>(50);
        String nextPageToken = page.containsKey("nextPageToken")
                ? page.getString("nextPageToken")
                : "";
        page.getJsonArray("items").stream()
                .map(JsonValue::asJsonObject)
                .map(j -> j.getJsonObject("snippet"))
                .map(this::parsePlaylistItemSnippet)
                .forEach(tracks::add);
        return PlaylistPage.create(tracks, nextPageToken);
    }

    //Utility method for Track entity parsing.
    private Track parsePlaylistItemSnippet(JsonObject snippet) {
        String videoId = snippet
                .getJsonObject("resourceId")
                .getString("videoId");
        String title = snippet
                .getString("title");
        String description = snippet
                .getString("description");
        String thumbnail = null;
        if (snippet.containsKey("thumbnails")) {
            JsonObject thumbnails = snippet.getJsonObject("thumbnails");
            String resKey = thumbnails
                    .keySet()
                    .stream()
                    .reduce((acc, next) -> next)
                    .orElse("");
            thumbnail = thumbnails
                    .getJsonObject(resKey)
                    .getString("url");
        }
        return Track.builder()
                .title(title)
                .trackIdInHostProvider(videoId)
                .description(description)
                .thumbnail(thumbnail)
                .build();
    }

    //Utility method for error response indicating.
    private boolean isError(JsonObject object) {
        return object.getJsonObject("pageInfo").getInt("totalResults") == 0;
    }
}

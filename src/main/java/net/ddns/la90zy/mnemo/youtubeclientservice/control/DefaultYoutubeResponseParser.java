package net.ddns.la90zy.mnemo.youtubeclientservice.control;

import net.ddns.la90zy.mnemo.syncservice.entity.Playlist;
import net.ddns.la90zy.mnemo.syncservice.entity.Track;
import net.ddns.la90zy.mnemo.youtubeclientservice.entity.PlaylistPage;

import javax.json.JsonObject;
import javax.json.JsonValue;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DefaultYoutubeResponseParser implements YoutubeResponseParser {

    public DefaultYoutubeResponseParser() {}

    @Override
    public Playlist parsePlaylistInfo(JsonObject info) {
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
        List<Track> tracks = new ArrayList<>(50);
        Optional<String> nextPageToken = page.containsKey("nextPageToken")
                ? Optional.of(page.getString("nextPageToken"))
                : Optional.empty();
        page.getJsonArray("items").stream()
                .map(JsonValue::asJsonObject)
                .map(j -> j.getJsonObject("snippet"))
                .map(this::parsePlaylistItemSnippet)
                .forEach(tracks::add);
        return PlaylistPage.create(tracks, nextPageToken);
    }

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
}

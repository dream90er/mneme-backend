package net.ddns.la90zy.mneme.youtubeclientservice.control;

import javafx.util.Pair;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

/**
 * Default implementation of {@link YoutubeRequestBuilder} interface.
 *
 * @author DreameR
 */
@Stateless
public class DefaultYoutubeRequestBuilder implements YoutubeRequestBuilder {

    private static final String PART = "snippet";

    private static final String MAX_RESULTS = "50";

    @Inject
    @ConfigProperty(name = "net.ddns.la90zy.mneme.youtubeapiurl",
            defaultValue = "https://www.googleapis.com/youtube/v3")
    private String API_URL;

    @Inject
    @ConfigProperty(name = "net.ddns.la90zy.mneme.youtubeapikey")
    private String  API_KEY;

    @Override
    public URI buildPlaylistPageRequest(String playlistId, String accessToken) {
        Pair<String, String> accessParam = accessToken.isEmpty()
                ? new Pair<>("key", API_KEY)
                : new Pair<>("access_token", accessToken);
        return UriBuilder.fromUri(API_URL)
                .path("playlistItems")
                .queryParam(accessParam.getKey(), accessParam.getValue())
                .queryParam("part", PART)
                .queryParam("maxResults", MAX_RESULTS)
                .queryParam("playlistId", playlistId)
                .build();
    }

    @Override
    public URI buildPlaylistPageRequest( String playlistId, String pageToken, String accessToken) {
        Pair<String, String> accessParam = accessToken.isEmpty()
                ? new Pair<>("key", API_KEY)
                : new Pair<>("access_token", accessToken);
        return UriBuilder.fromUri(API_URL)
                .path("playlistItems")
                .queryParam(accessParam.getKey(), accessParam.getValue())
                .queryParam("part", PART)
                .queryParam("maxResults", MAX_RESULTS)
                .queryParam("playlistId", playlistId)
                .queryParam("pageToken", pageToken)
                .build();
    }

    @Override
    public URI buildPlaylistInfoRequest(String playlistId, String accessToken) {
        Pair<String, String> accessParam = accessToken.isEmpty()
                ? new Pair<>("key", API_KEY)
                : new Pair<>("access_token", accessToken);
        return UriBuilder.fromUri(API_URL)
                .path("playlists")
                .queryParam(accessParam.getKey(), accessParam.getValue())
                .queryParam("part", PART)
                .queryParam("id", playlistId)
                .build();
    }
}

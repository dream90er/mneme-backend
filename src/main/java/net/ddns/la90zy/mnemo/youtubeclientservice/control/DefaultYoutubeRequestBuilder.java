package net.ddns.la90zy.mnemo.youtubeclientservice.control;

import javafx.util.Pair;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

/**
 * Default implementation of {@link YoutubeRequestBuilder} interface.
 *
 * @author DreameR
 */
public class DefaultYoutubeRequestBuilder implements YoutubeRequestBuilder {

    private static final String PART = "snippet";

    private static final String MAX_RESULTS = "50";

    private final String API_URL;

    private final String  API_KEY;

    /**
     *
     * @param apiUrl URL to Youtube API endpoint.
     * @param apiKey API key from <a href="https://console.developers.google.com/apis/credentials">Credentials page</a>.
     */
    public DefaultYoutubeRequestBuilder(String apiUrl, String apiKey) {
        this.API_URL = apiUrl;
        this.API_KEY = apiKey;
    }

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

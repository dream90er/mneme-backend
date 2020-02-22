package net.ddns.la90zy.mnemo.youtubeclientservice.control;

import net.ddns.la90zy.mnemo.syncservice.control.HostProviderClientService;
import net.ddns.la90zy.mnemo.syncservice.control.HostProviderName;
import net.ddns.la90zy.mnemo.syncservice.control.PlaylistNotFoundException;
import net.ddns.la90zy.mnemo.syncservice.entity.Playlist;
import net.ddns.la90zy.mnemo.syncservice.entity.Track;
import net.ddns.la90zy.mnemo.youtubeclientservice.boundary.YoutubeApiClient;
import net.ddns.la90zy.mnemo.youtubeclientservice.entity.PlaylistPage;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonObject;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link HostProviderClientService} interface for Youtube service.
 *
 * @author DreameR
 */
@HostProviderName("Youtube")
@Stateless
public class YoutubeClientService implements HostProviderClientService {

    private final YoutubeResponseParser youtubeResponseParser;

    private final YoutubeRequestBuilder youtubeRequestBuilder;

    @Inject
    private YoutubeApiClient youtubeApiClient;

    @Inject
    public YoutubeClientService(@ConfigProperty(name = "net.ddns.la90zy.mnemo.youtubeapiurl",
            defaultValue = "https://www.googleapis.com/youtube/v3") String apiUrl,
                                @ConfigProperty(name = "net.ddns.la90zy.mnemo.youtubeapikey") String apiKey) {
        youtubeResponseParser = new DefaultYoutubeResponseParser();
        youtubeRequestBuilder = new DefaultYoutubeRequestBuilder(apiUrl, apiKey);
    }

    @Override
    public List<Track> getPlaylistTracks(String playlistId, String accessToken) {
        List<Track> result = new ArrayList<>(100);
        URI request = youtubeRequestBuilder.buildPlaylistPageRequest(playlistId, accessToken);
        try {
            for (Optional<String> nextPageToken = processPlaylistPageRequest(request, result);
                 nextPageToken.isPresent();
                 nextPageToken = processPlaylistPageRequest(request, result)) {
                request = youtubeRequestBuilder.buildPlaylistPageRequest(playlistId, nextPageToken.get(), accessToken);
            }
        } catch (YoutubeParserException e) {
            throw new PlaylistNotFoundException(e.getMessage(), playlistId, "Youtube");
        }
        return result;
    }
    //Utility method for process single page request.
    private Optional<String> processPlaylistPageRequest(URI request, List<Track> result) {
        JsonObject response = youtubeApiClient.processRequest(request);
        PlaylistPage playlistPage = youtubeResponseParser.parsePlaylistPage(response);
        result.addAll(playlistPage.getTracks());
        return playlistPage.getNextPageToken();
    }

    @Override
    public Playlist getPlaylistInfo(String playlistId, String accessToken) {
        URI request = youtubeRequestBuilder.buildPlaylistInfoRequest(playlistId, accessToken);
        JsonObject response = youtubeApiClient.processRequest(request);
        Playlist playlist;
        try {
            playlist = youtubeResponseParser.parsePlaylistInfo(response);
        } catch (YoutubeParserException e) {
            throw new PlaylistNotFoundException(e.getMessage(), playlistId, "Youtube");
        }
        playlist.setPlaylistIdInHostProvider(playlistId);
        return playlist;
    }

    @Override
    public String getAccessToken(String refreshToken) {
        return null;
    }

}

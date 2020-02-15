package net.ddns.la90zy.mnemo.youtubeclientservice.control;

import net.ddns.la90zy.mnemo.syncservice.control.HostProviderClientService;
import net.ddns.la90zy.mnemo.syncservice.control.HostProviderName;
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

@HostProviderName("Youtube")
@Stateless
public class YoutubeClientService implements HostProviderClientService {

    @Inject
    YoutubeApiClient youtubeApiClient;

    private final YoutubeResponseParser youtubeResponseParser;
    private final YoutubeRequestBuilder youtubeRequestBuilder;

    @Inject
    public YoutubeClientService(@ConfigProperty(name = "net.ddns.la90zy.mnemo.youtubeapiurl") String apiUrl,
                                @ConfigProperty(name = "net.ddns.la90zy.mnemo.youtubeapikey") String apiKey) {
        youtubeResponseParser = new DefaultYoutubeResponseParser();
        youtubeRequestBuilder = new DefaultYoutubeRequestBuilder(apiUrl, apiKey);
    }

    @Override
    public List<Track> getPlaylistTracks(String playlistId) {
        List<Track> result = new ArrayList<>(100);
        URI request = youtubeRequestBuilder.buildPlaylistPageRequest(playlistId);
        for (Optional<String> nextPageToken = processPlaylistPageRequest(request, result);
             nextPageToken.isPresent();
             nextPageToken = processPlaylistPageRequest(request, result)) {
            request = youtubeRequestBuilder.buildPlaylistPageRequest(playlistId, nextPageToken.get());
        }
        return result;
    }

    private Optional<String> processPlaylistPageRequest(URI request, List<Track> result) {
        JsonObject response = youtubeApiClient.processRequest(request);
        PlaylistPage playlistPage = youtubeResponseParser.parsePlaylistPage(response);
        result.addAll(playlistPage.getTracks());
        return playlistPage.getNextPageToken();
    }

    @Override
    public List<Track> getPlaylistTracks(String playlistId, String accessToken) {
        return null;
    }

    @Override
    public Playlist getPlaylistInfo(String playlistId) {
        URI request = youtubeRequestBuilder.buildPlaylistInfoRequest(playlistId);
        JsonObject response = youtubeApiClient.processRequest(request);
        Playlist playlist = youtubeResponseParser.parsePlaylistInfo(response);
        playlist.setPlaylistIdInHostProvider(playlistId);
        return playlist;
    }

    @Override
    public Playlist getPlaylistInfo(String playlistId, String accessToken) {
        return null;
    }

    @Override
    public String getAccessToken(String refreshToken) {
        return null;
    }

}

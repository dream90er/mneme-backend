package net.ddns.la90zy.mneme.youtubeclientservice.control;

import net.ddns.la90zy.mneme.syncservice.control.HostProviderClientService;
import net.ddns.la90zy.mneme.syncservice.control.HostProviderName;
import net.ddns.la90zy.mneme.syncservice.control.PlaylistNotFoundException;
import net.ddns.la90zy.mneme.syncservice.entity.Playlist;
import net.ddns.la90zy.mneme.syncservice.entity.Track;
import net.ddns.la90zy.mneme.youtubeclientservice.boundary.YoutubeApiClient;
import net.ddns.la90zy.mneme.youtubeclientservice.entity.PlaylistPage;
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

    private YoutubeResponseParser youtubeResponseParser;

    private YoutubeRequestBuilder youtubeRequestBuilder;

    private YoutubeApiClient youtubeApiClient;

    @Inject
    public YoutubeClientService(YoutubeApiClient youtubeApiClient,
                                YoutubeResponseParser youtubeResponseParser,
                                YoutubeRequestBuilder youtubeRequestBuilder) {
        this.youtubeResponseParser = youtubeResponseParser;
        this.youtubeRequestBuilder = youtubeRequestBuilder;
        this.youtubeApiClient = youtubeApiClient;
    }

    protected YoutubeClientService() { }

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
        playlistPage.getTracks().forEach(result::add);
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

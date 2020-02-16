package net.ddns.la90zy.mnemo.youtubeclientservice.control;

import java.net.URI;
import java.util.Optional;

public interface YoutubeRequestBuilder {

    URI buildPlaylistPageRequest(String playlistId, String accessToken);
    URI buildPlaylistPageRequest(String playlistId, String pageToken, String accessToken);
    URI buildPlaylistInfoRequest(String playlistId, String accessToken);
}

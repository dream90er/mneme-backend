package net.ddns.la90zy.mnemo.youtubeclientservice.control;

import java.net.URI;

public interface YoutubeRequestBuilder {

    URI buildPlaylistPageRequest(String playlistId);
    URI buildPlaylistPageRequest(String playlistId, String pageToken);
    URI buildPlaylistInfoRequest(String playlistId);
}

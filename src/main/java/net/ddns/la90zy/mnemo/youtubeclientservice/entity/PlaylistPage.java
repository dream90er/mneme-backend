package net.ddns.la90zy.mnemo.youtubeclientservice.entity;

import net.ddns.la90zy.mnemo.syncservice.entity.Track;

import java.util.List;
import java.util.Optional;

public class PlaylistPage {

    public static PlaylistPage create(List<Track> tracks, Optional<String> nextPageToken) {
        return new PlaylistPage(tracks, nextPageToken);
    }

    private List<Track> tracks;
    private Optional<String> nextPageToken;

    public PlaylistPage(List<Track> tracks, Optional<String> nextPageToken) {
        this.tracks = tracks;
        this.nextPageToken = nextPageToken;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public Optional<String> getNextPageToken() {
        return nextPageToken;
    }
}

package net.ddns.la90zy.mneme.youtubeclientservice.entity;

import net.ddns.la90zy.mneme.syncservice.entity.Track;

import java.util.List;
import java.util.Optional;

public class PlaylistPage {

    public static PlaylistPage create(List<Track> tracks, String nextPageToken) {
        return new PlaylistPage(tracks, nextPageToken);
    }

    private List<Track> tracks;
    private String nextPageToken;

    public PlaylistPage(List<Track> tracks, String nextPageToken) {
        this.tracks = tracks;
        this.nextPageToken = nextPageToken;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public Optional<String> getNextPageToken() {
        return nextPageToken.isEmpty()
                ? Optional.empty()
                : Optional.of(nextPageToken);
    }
}

package net.ddns.la90zy.mnemo.syncservice.control;

import net.ddns.la90zy.mnemo.syncservice.entity.HostProvider;
import net.ddns.la90zy.mnemo.syncservice.entity.Playlist;
import net.ddns.la90zy.mnemo.syncservice.entity.Track;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

//TODO refactor this class
// maybe split on smaller classes
@Stateless
public class SyncService {

    @Inject
    private HostProviderClientServiceFactory hostProviderClientServiceFactory;

    @Inject
    private PlaylistService playlistService;

    @Inject
    private TrackService trackService;

    @Inject
    private HostProviderService hostProviderService;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void sync(Long playlistInternalId, String accessToken) {
        Playlist existingPlaylist = playlistService.getPlaylistById(playlistInternalId);
        HostProviderClientService hostProviderClientService = hostProviderClientServiceFactory
                .getHostProviderClientService(existingPlaylist.getHostProvider().getTitle());
        List<Track> receivedTracks = hostProviderClientService
                .getPlaylistTracks(existingPlaylist.getPlaylistIdInHostProvider(), accessToken);
        merge(existingPlaylist, receivedTracks);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Playlist syncNew(String hostProviderName, String playlistId, String accessToken) {
        HostProviderClientService hostProviderClientService = hostProviderClientServiceFactory
                .getHostProviderClientService(hostProviderName);
        Playlist playlist = hostProviderClientService.getPlaylistInfo(playlistId, accessToken);
        HostProvider hostProvider = hostProviderService.getHostProviderByTitle(hostProviderName).get();
        hostProvider.addPlaylist(playlist);
        List<Track> tracks = hostProviderClientService.getPlaylistTracks(playlistId, accessToken);
        tracks.forEach(t -> addTrack(playlist, t));
        return playlist;
    }

    private void addTrack(Playlist playlist, Track newTrack) {
        String videoId = newTrack.getTrackIdInHostProvider();
        HostProvider hostProvider = playlist.getHostProvider();
        Optional<Track> optionalTrack = trackService.getTrackByHostProviderSpecificId(videoId, hostProvider);
        Track track = optionalTrack.orElseGet(() -> {
            hostProvider.addTrack(newTrack);
            return newTrack;
        });
        playlist.addTrack(track);
    }

    private void merge(Playlist existingPlaylist, List<Track> receivedTracks) {
        Set<String> existingIds = existingPlaylist.getTracks()
                .stream()
                .map(Track::getTrackIdInHostProvider)
                .collect(Collectors.toSet());
        Set<String> receivedIds = receivedTracks
                .stream()
                .map(Track::getTrackIdInHostProvider)
                .collect(Collectors.toSet());
        Set<String> newIds = new HashSet<>(receivedIds);
        newIds.removeAll(existingIds);
        existingPlaylist.getTracks().stream()
                .forEach(t -> t.setAvailability(
                        receivedIds.contains(t.getTrackIdInHostProvider())));
        receivedTracks.stream()
                .filter(t -> newIds.contains(t.getTrackIdInHostProvider()))
                .forEach(t -> addTrack(existingPlaylist, t));
    }
}

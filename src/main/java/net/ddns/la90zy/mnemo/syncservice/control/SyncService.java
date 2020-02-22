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

/**
 * Playlist synchronization service bean.
 *
 * @author DreameR
 */
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

    /**
     * Synchronize playlist, that already exist in Mnemo service.
     * @param playlistInternalId Mnemo service specific Id of playlist.
     * @param accessToken access token of concrete user, received from host provider. Blank if not provided.
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void sync(Long playlistInternalId, String accessToken) {
        Playlist existingPlaylist = playlistService.getPlaylistById(playlistInternalId);
        HostProviderClientService hostProviderClientService = hostProviderClientServiceFactory
                .getHostProviderClientService(existingPlaylist.getHostProvider().getTitle());
        List<Track> receivedTracks;
        try {
            receivedTracks = hostProviderClientService
                    .getPlaylistTracks(existingPlaylist.getPlaylistIdInHostProvider(), accessToken);
            merge(existingPlaylist, receivedTracks);
            existingPlaylist.setAvailability(true);
        } catch (PlaylistNotFoundException e) {
            existingPlaylist.setAvailability(false);
        }
    }

    /**
     * Add and synchronize playlist, that not yet exist in Mnemo service.
     * @param hostProviderName name of host provider.
     * @param playlistId host provider specific playlist Id.
     * @param accessToken access token of concrete user, received from host provider. Blank if not provided.
     * @return fresh created and synchronized playlist entity.
     */
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

    //Utility method for adding new track in playlist. If Mnemo service already keep them(from another playlist),
    //then already existing version will be added.
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

    //Utility method for merging Mnemo version of playlist and track received from host provider.
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

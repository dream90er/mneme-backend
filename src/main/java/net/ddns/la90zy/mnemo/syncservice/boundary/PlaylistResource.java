package net.ddns.la90zy.mnemo.syncservice.boundary;

import net.ddns.la90zy.mnemo.syncservice.control.HostProviderService;
import net.ddns.la90zy.mnemo.syncservice.control.PlaylistService;
import net.ddns.la90zy.mnemo.syncservice.entity.HostProvider;
import net.ddns.la90zy.mnemo.syncservice.entity.Playlist;
import net.ddns.la90zy.mnemo.syncservice.entity.Track;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class PlaylistResource {

    @Inject
    private HostProviderService hostProviderService;

    @Inject
    private PlaylistService playlistService;

    @GET
    @Path("{hostProviderName}/{playlistId}")
    public List<Track> getPlaylist(@PathParam("hostProviderName") String hostProviderName,
                                   @PathParam("playlistId") String playlistId) {
            Optional<HostProvider> hostProvider = hostProviderService
                    .getHostProviderByTitle(hostProviderName);
            if (!hostProvider.isPresent()) {
                throw new NotFoundException("No such service found.");
            }
            Optional<Playlist> playlist = playlistService
                    .getPlaylistByHostProviderSpecificId(playlistId, hostProvider.get());
            if (!playlist.isPresent()) {
                throw new NotFoundException("No such playlist found.");
            }
        return new ArrayList<>(playlist.get()
                .getTracks());
    }
}

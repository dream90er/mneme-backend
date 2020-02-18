package net.ddns.la90zy.mnemo.syncservice.control;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

//TODO use ready-made batch processing solutions like JBatch
@ApplicationScoped
public class BatchSyncService {

    @Inject
    private PlaylistService playlistService;

    @Inject
    private SyncService syncService;

    public void sync() {

        //TODO partial this list on host service affiliation basis
        List<Long> playlistIDs = playlistService.getAllPlaylistIDs();
        for (Long id: playlistIDs) {
            try {
                syncService.sync(id, "");
            } catch (HostProviderNotAvailableException e) {
                //TODO retries
                // or maybe in service client class
                //TODO skip all playlists from service that not available
            }

        }
    }
}

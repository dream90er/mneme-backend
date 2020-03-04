package net.ddns.la90zy.mneme.faces.boundary;

import net.ddns.la90zy.mneme.syncservice.boundary.MnemeFacade;
import net.ddns.la90zy.mneme.syncservice.entity.Playlist;
import net.ddns.la90zy.mneme.syncservice.entity.Track;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Set;

@Named
@ViewScoped
public class PlaylistsBacking implements Serializable {

    @Inject
    private MnemeFacade mnemeFacade;

    private Set<Playlist> playlists;

    private Track selectedTrack;

    @PostConstruct
    public void init() {
        playlists = mnemeFacade.getUserPlaylists();
    }

    public void update() {
        playlists = mnemeFacade.getUserPlaylists();
    }

    public Track getSelectedTrack() {
        return selectedTrack;
    }

    public void setSelectedTrack(Track selectedTrack) {
        this.selectedTrack = selectedTrack;
    }

    public Set<Playlist> getPlaylists() {
        return playlists;
    }
}

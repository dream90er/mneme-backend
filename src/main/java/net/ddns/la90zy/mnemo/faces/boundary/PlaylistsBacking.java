package net.ddns.la90zy.mnemo.faces.boundary;

import net.ddns.la90zy.mnemo.syncservice.boundary.MnemoFacade;
import net.ddns.la90zy.mnemo.syncservice.entity.Playlist;
import net.ddns.la90zy.mnemo.syncservice.entity.Track;

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
    private MnemoFacade mnemoFacade;

    private Set<Playlist> playlists;

    private Track selectedTrack;

    @PostConstruct
    public void init() {
        playlists = mnemoFacade.getUserPlaylists();
    }

    public void update() {
        playlists = mnemoFacade.getUserPlaylists();
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

package net.ddns.la90zy.mnemo.faces.boundary;

import net.ddns.la90zy.mnemo.syncservice.boundary.MnemoFacade;
import net.ddns.la90zy.mnemo.syncservice.entity.Playlist;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.inject.Inject;
import java.util.Set;

@Model
public class MnemoMain {

    @Inject
    MnemoFacade mnemoFacade;

    private Set<Playlist> playlists;
    private String playlistId;

    @PostConstruct
    public void init() {
        playlists = mnemoFacade.getUserPlaylists();
    }

    public void submit() {
        mnemoFacade.addPlaylistUserRequest("Youtube", playlistId);
        playlists = mnemoFacade.getUserPlaylists();
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }

    public Set<Playlist> getPlaylists() {
        return playlists;
    }

    public String getPlaylistId() {
        return playlistId;
    }
}

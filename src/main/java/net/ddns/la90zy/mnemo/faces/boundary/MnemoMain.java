package net.ddns.la90zy.mnemo.faces.boundary;

import net.ddns.la90zy.mnemo.syncservice.boundary.MnemoFacade;
import net.ddns.la90zy.mnemo.syncservice.control.PlaylistNotFoundException;
import net.ddns.la90zy.mnemo.syncservice.entity.Playlist;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.util.Set;

@Model
public class MnemoMain {

    @Inject
    private MnemoFacade mnemoFacade;

    @Inject
    private FacesContext facesContext;

    private UIComponent playlistIdField;

    private Set<Playlist> playlists;

    private String playlistId;

    @PostConstruct
    public void init() {
        playlists = mnemoFacade.getUserPlaylists();
    }

    public void submit() {
        //TODO this is not how exception handling works in JSF
        // PS This is how exception handling works, but... not with AJAX :P
        try {
            mnemoFacade.addPlaylistUserRequest("Youtube", playlistId);
            playlists = mnemoFacade.getUserPlaylists();
        } catch (PlaylistNotFoundException e) {
            facesContext.addMessage(playlistIdField.getClientId(facesContext), new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    e.getMessage(),
                    String.format("Playlist ID: %s\nProvider: %s", e.getPlaylistExternalId(), e.getHostProviderName())));
        }
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

    public UIComponent getPlaylistIdField() {
        return playlistIdField;
    }

    public void setPlaylistIdField(UIComponent playlistIdField) {
        this.playlistIdField = playlistIdField;
    }
}

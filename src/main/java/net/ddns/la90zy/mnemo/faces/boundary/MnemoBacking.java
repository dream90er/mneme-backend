package net.ddns.la90zy.mnemo.faces.boundary;

import net.ddns.la90zy.mnemo.syncservice.boundary.MnemoFacade;
import net.ddns.la90zy.mnemo.syncservice.control.HostProviderNotAvailableException;
import net.ddns.la90zy.mnemo.syncservice.control.PlaylistNotFoundException;

import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;

@Model
public class MnemoBacking {

    @Inject
    private PlaylistsBacking playlistsBacking;

    @Inject
    private MnemoFacade mnemoFacade;

    @Inject
    private FacesContext facesContext;

    private UIComponent playlistIdField;

    @NotNull
    private String playlistId;

    public void submit() {
        try {
            mnemoFacade.addPlaylistUserRequest("Youtube", playlistId);
            playlistsBacking.update();
        } catch (PlaylistNotFoundException e) {
            facesContext.addMessage(playlistIdField.getClientId(facesContext), new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Playlist not found or access forbidden.",
                    null));
        } catch (HostProviderNotAvailableException e) {
            facesContext.addMessage(playlistIdField.getClientId(facesContext), new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Service not available. Please retry later.",
                    null));
        }
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
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

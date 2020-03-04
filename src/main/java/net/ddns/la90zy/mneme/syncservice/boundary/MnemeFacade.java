package net.ddns.la90zy.mneme.syncservice.boundary;

import net.ddns.la90zy.mneme.syncservice.control.*;
import net.ddns.la90zy.mneme.syncservice.entity.HostProvider;
import net.ddns.la90zy.mneme.syncservice.entity.Playlist;
import net.ddns.la90zy.mneme.syncservice.entity.User;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Optional;
import java.util.Set;

/**
 * Facade for Mneme service.
 *
 * @author DreameR
 */
@Stateless
public class MnemeFacade {

    @Inject
    private HostProviderService hostProviderService;

    @Inject
    private PlaylistService playlistService;

    @Inject
    private SyncService syncService;

    @Inject
    private UserService userService;

    @Resource
    private SessionContext sessionContext;

    /**
     * Serve request from user for playlist adding.
     * If playlist not yet exist in Mneme service, it will be added and synchronized.
     * @param hostProviderTitle host provider name.
     * @param id host provider specific playlist Id.
     */
    public void addPlaylistUserRequest(String hostProviderTitle, String id) {
        HostProvider hostProvider = hostProviderService.getHostProviderByTitle(hostProviderTitle).get();
        Optional<Playlist> existingPlaylist = playlistService.getPlaylistByHostProviderSpecificId(id, hostProvider);
        Playlist playlist = existingPlaylist.orElseGet(
                () -> syncService.syncNew(hostProviderTitle, id, ""));
        playlist = playlistService.savePlaylist(playlist);
        String login = sessionContext.getCallerPrincipal().getName();
        User user = userService.getUserByLogin(login).get();
        user.addPlaylist(playlist);
    }


    /**
     * Serve request from user for playlist synchronizing.
     * @param id Mneme specific playlist Id.
     */
    public void updatePlaylistUserRequest(Long id) {
        syncService.sync(id, "");
    }

    /**
     * Get all playlists associated with current user.
     * @return Set of playlist.
     */
    public Set<Playlist> getUserPlaylists() {
        String login = sessionContext.getCallerPrincipal().getName();
        User user = userService.getUserByLogin(login).get();
        return user.getPlaylists();
    }

    /**
     * Check if given login taken or free.
     * @param login User login.
     * @return true if taken, false otherwise.
     */
    public boolean isLoginTaken(String login) {
        return userService.getUserByLogin(login).isPresent();
    }

    /**
     * Register user with specified credentials.
     * @param login User login.
     * @param email User email. Empty string if not provided.
     * @param password User password.
     */
    public void registerUser(String login, String email, String password) {
        User user = User.builder()
                .login(login)
                .password(password)
                .group(User.Group.USER)
                .email(email)
                .build();
        userService.saveUser(user);
    }
}

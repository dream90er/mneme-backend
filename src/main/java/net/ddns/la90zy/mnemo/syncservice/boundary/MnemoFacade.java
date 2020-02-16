package net.ddns.la90zy.mnemo.syncservice.boundary;

import net.ddns.la90zy.mnemo.syncservice.control.*;
import net.ddns.la90zy.mnemo.syncservice.entity.HostProvider;
import net.ddns.la90zy.mnemo.syncservice.entity.Playlist;
import net.ddns.la90zy.mnemo.syncservice.entity.User;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Optional;
import java.util.Set;

@Stateless
public class MnemoFacade {

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

    public void addPlaylistUserRequest(String hostProviderTitle, String id) {
        HostProvider hostProvider = hostProviderService.getHostProviderByTitle(hostProviderTitle).get();
        Optional<Playlist> existingPlaylist = playlistService.getPlaylistByHostProviderSpecificId(id, hostProvider);
        Playlist playlist = existingPlaylist.orElseGet(() -> syncService.syncNew(hostProviderTitle, id, ""));
        playlist = playlistService.savePlaylist(playlist);
        String login = sessionContext.getCallerPrincipal().getName();
        User user = userService.getUserByLogin(login).get();
        user.addPlaylist(playlist);
    }

    public Set<Playlist> getUserPlaylists() {
        String login = sessionContext.getCallerPrincipal().getName();
        User user = userService.getUserByLogin(login).get();
        return user.getPlaylists();
    }

    public boolean isLoginTaken(String login) {
        return userService.getUserByLogin(login).isPresent();
    }

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

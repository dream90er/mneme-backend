package net.ddns.la90zy.mnemo.syncservice.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
@Table(name = "mnemo_user")
@NamedQueries({
        @NamedQuery(name = "User.findAll",
                query = "SELECT u FROM User u"),
        @NamedQuery(name = "User.findByLogin",
                query = "SELECT u FROM User u WHERE u.login = :login"),
        @NamedQuery(name = "User.findLikeLogin",
                query = "SELECT u FROM User u WHERE u.login LIKE CONCAT(:like, '%') ORDER BY u.login")})
public class User {

    public enum Group {
        ADMIN,
        USER
    }

    public static class Builder {

        private String login;
        private String password;
        private Group group;
        private String email;

        public Builder() {}

        public Builder login(String login) {
            this.login = login;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder group(Group group) {
            this.group = group;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public User build() {
            return new User(this);
        }

    }

    public static Builder builder() {
        return new Builder();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @NotNull
    @Size(min = 3, max = 100)
    @Column(name = "login", nullable = false)
    private String login;

    @Size(min = 3, max = 255)
    @Column(name = "email")
    private String email;

    @NotNull
    @Size(max = 255)
    @Column(name = "password", nullable = false)
    @Convert(converter = PasswordEncrypter.class)
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "mnemo_group", nullable = false)
    private User.Group group;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<HostProviderUser> hostProviders = new ArrayList<>();

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "mnemo_user_playlist",
            joinColumns = @JoinColumn(name = "mnemo_user_id"),
            inverseJoinColumns = @JoinColumn(name = "playlist_id")
    )
    private Set<Playlist> playlists = new HashSet<>();

    public User() {}

    private User(Builder builder) {
        this.login = builder.login;
        this.password = builder.password;
        this.group = builder.group;
        this.email = builder.email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Optional<String> getEmail() {
        return Optional.ofNullable(email);
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public List<HostProviderUser> getHostProviders() {
        return hostProviders;
    }

    public void setHostProviders(List<HostProviderUser> hostProviders) {
        this.hostProviders = hostProviders;
    }

    public Set<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(Set<Playlist> playlists) {
        this.playlists = playlists;
    }

    public void addPlaylist(Playlist playlist) {
        playlists.add(playlist);
        playlist.getUsers().add(this);
    }

    public void removePlaylist(Playlist playlist) {
        playlists.remove(playlist);
        playlist.getUsers().remove(this);
    }

    public void addHostProvider(HostProvider hostProvider, String userIdInHostProvider, String refreshToken) {
        HostProviderUser hostProviderUser = new HostProviderUser(
                this,
                hostProvider,
                userIdInHostProvider,
                refreshToken
        );
        hostProviders.add(hostProviderUser);
        hostProvider.getUsers().add(hostProviderUser);
    }

    public void removeHostProvider(HostProvider hostProvider) {
        hostProviders.removeIf(h -> h.getHostProvider().equals(hostProvider));
        hostProvider.getUsers().removeIf(h -> h.getUser().equals(this));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (null == o || getClass() != o.getClass())
            return false;

        User that = (User) o;
        return Objects.equals(login, that.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login);
    }
}

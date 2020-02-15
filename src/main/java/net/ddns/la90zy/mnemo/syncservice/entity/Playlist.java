package net.ddns.la90zy.mnemo.syncservice.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "playlist")
@NamedQueries({
        @NamedQuery(name = "Playlist.findAll",
                query = "SELECT p FROM Playlist p"),
        @NamedQuery(name = "Playlist.findByHostProviderSpecificId",
                query = "SELECT p FROM Playlist p WHERE p.playlistIdInHostProvider = :id AND p.hostProvider = :hostProvider")
})
public class Playlist {

    public static class Builder {

        private String title;
        private String ownerId;
        private String playlistIdInHostProvider;

        public Builder() {}

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder ownerId(String ownerId) {
            this.ownerId = ownerId;
            return this;
        }

        public Builder playlistIdInHostProvider(String playlistIdInHostProvider) {
            this.playlistIdInHostProvider = playlistIdInHostProvider;
            return this;
        }

        public Playlist build() {
            return new Playlist(this);
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
    @Size(min = 1, max = 255)
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "availability", nullable = false)
    private Boolean availability;

    @NotNull
    @Size(max = 255)
    @Column(name = "playlist_id_in_host_provider", nullable = false)
    private String playlistIdInHostProvider;

    @NotNull
    @Size(max = 255)
    @Column(name = "owner_id", nullable = false)
    private String ownerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_provider_id", nullable = false)
    private HostProvider hostProvider;

    @ManyToMany(cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
    })
    @JoinTable(name = "playlist_track",
            joinColumns = @JoinColumn(name = "playlist_id"),
            inverseJoinColumns = @JoinColumn(name = "track_id")
    )
    private Set<Track> tracks = new HashSet<>();

    @ManyToMany(mappedBy = "playlists")
    private Set<User> users = new HashSet<>();

    public Playlist() {}

    private Playlist(Builder builder) {
        this.title = builder.title;
        this.ownerId = builder.ownerId;
        this.playlistIdInHostProvider = builder.playlistIdInHostProvider;
        this.availability = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getAvailability() {
        return availability;
    }

    public void setAvailability(Boolean availability) {
        this.availability = availability;
    }

    public String getPlaylistIdInHostProvider() {
        return playlistIdInHostProvider;
    }

    public void setPlaylistIdInHostProvider(String playlistIdInHostProvider) {
        this.playlistIdInHostProvider = playlistIdInHostProvider;
    }

    public HostProvider getHostProvider() {
        return hostProvider;
    }

    public void setHostProvider(HostProvider hostProvider) {
        this.hostProvider = hostProvider;
    }

    public Set<Track> getTracks() {
        return tracks;
    }

    public void setTracks(Set<Track> tracks) {
        this.tracks = tracks;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public void addTrack(Track track) {
        tracks.add(track);
        track.getPlaylists().add(this);
    }

    public void removeTrack(Track track) {
        tracks.remove(track);
        track.getPlaylists().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (null == o || getClass() != o.getClass())
            return false;

        Playlist that = (Playlist) o;
        return Objects.equals(hostProvider, that.hostProvider) &&
                Objects.equals(playlistIdInHostProvider, that.playlistIdInHostProvider);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hostProvider, playlistIdInHostProvider);
    }
}

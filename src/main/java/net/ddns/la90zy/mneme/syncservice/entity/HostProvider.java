package net.ddns.la90zy.mneme.syncservice.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
@Table(name = "host_provider")
@NamedQueries({
        @NamedQuery(name = "HostProvider.findAll",
                query = "SELECT h FROM HostProvider h"),
        @NamedQuery(name = "HostProvider.findByTitle",
                query = "SELECT h FROM HostProvider h WHERE h.title = :title")
})
public class HostProvider {

    public static HostProvider create(String title) {
        return new HostProvider(title);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "title")
    private String title;

    @OneToMany(
            mappedBy = "hostProvider",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<HostProviderUser> users = new ArrayList<>();

    @OneToMany(
            mappedBy = "hostProvider",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Playlist> playlists = new ArrayList<>();

    @OneToMany(
            mappedBy = "hostProvider",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Track> tracks = new ArrayList<>();

    public HostProvider() {}

    private HostProvider(String title) {
        this.title = title;
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

    public List<HostProviderUser> getUsers() {
        return users;
    }

    public void setUsers(List<HostProviderUser> users) {
        this.users = users;
    }

    public List<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(List<Playlist> playlists) {
        this.playlists = playlists;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

    public void addTrack(Track track) {
        tracks.add(track);
        track.setHostProvider(this);
    }

    public void removeTrack(Track track) {
        tracks.remove(track);
    }

    public void addPlaylist(Playlist playlist) {
        playlists.add(playlist);
        playlist.setHostProvider(this);
    }

    public void removePlaylist(Playlist playlist) {
        playlists.remove(playlist);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (null == o || getClass() != o.getClass())
            return false;

        HostProvider that = (HostProvider) o;
        return Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() { return Objects.hash(title); }
}

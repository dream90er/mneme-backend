package net.ddns.la90zy.mnemo.syncservice.entity;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Entity
@Table(name = "track")
@NamedQueries({
        @NamedQuery(name = "Track.findAll",
                query = "SELECT t FROM Track t"),
        @NamedQuery(name = "Track.findByHostProviderSpecificId",
                query = "SELECT t FROM Track t WHERE t.trackIdInHostProvider = :id AND t.hostProvider = :hostProvider")
})
public class Track {

    public static class Builder {

        private String title;
        private String trackIdInHostProvider;
        private String description;
        private String thumbnail;

        public Builder() {}

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder trackIdInHostProvider(String trackIdInHostProvider) {
            this.trackIdInHostProvider = trackIdInHostProvider;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder thumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
            return this;
        }

        public Track build() {
            return new Track(this);
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

    @Lob
    @Column(name = "description")
    private String description;

    @Size(max = 255)
    @Column(name = "thumbnail")
    private String thumbnail;

    @NotNull
    @Column(name = "availability", nullable = false)
    private Boolean availability;

    @NotNull
    @Size(max = 255)
    @Column(name = "track_id_in_host_provider", nullable = false)
    private String trackIdInHostProvider;

    @JsonbTransient
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_provider_id", nullable = false)
    private HostProvider hostProvider;

    @JsonbTransient
    @ManyToMany(mappedBy = "tracks")
    private Set<Playlist> playlists = new HashSet<>();

    public Track() {}

    private Track(Builder builder) {
        this.title = builder.title;
        this.trackIdInHostProvider = builder.trackIdInHostProvider;
        this.availability = true;
        this.description = builder.description;
        this.thumbnail = builder.thumbnail;
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

    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Optional<String> getThumbnail() {
        return Optional.ofNullable(thumbnail);
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Boolean getAvailability() {
        return availability;
    }

    public void setAvailability(Boolean availability) {
        this.availability = availability;
    }

    public String getTrackIdInHostProvider() {
        return trackIdInHostProvider;
    }

    public void setTrackIdInHostProvider(String trackIdInHostProvider) {
        this.trackIdInHostProvider = trackIdInHostProvider;
    }

    public HostProvider getHostProvider() {
        return hostProvider;
    }

    public void setHostProvider(HostProvider hostProvider) {
        this.hostProvider = hostProvider;
    }

    public Set<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(Set<Playlist> playlists) {
        this.playlists = playlists;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (null == o || getClass() != o.getClass())
            return false;

        Track that = (Track) o;
        return Objects.equals(hostProvider, that.hostProvider) &&
                Objects.equals(trackIdInHostProvider, that.trackIdInHostProvider);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hostProvider, trackIdInHostProvider);
    }
}

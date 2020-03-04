package net.ddns.la90zy.mneme.syncservice.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
@Table(name = "mneme_user_host_provider")
public class HostProviderUser {

    @EmbeddedId
    private HostProviderUserId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "mneme_user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("hostProviderId")
    @JoinColumn(name = "host_provider_id")
    private HostProvider hostProvider;

    @NotNull
    @Size(max = 255)
    @Column(name = "user_id_in_host_provider", nullable = false)
    private String userIdInHostProvider;

    @NotNull
    @Size(max = 255)
    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

    public HostProviderUser() {}

    public HostProviderUser(User user, HostProvider hostProvider, String userIdInHostProvider, String refreshToken) {
        this.user = user;
        this.hostProvider = hostProvider;
        this.userIdInHostProvider = userIdInHostProvider;
        this.refreshToken = refreshToken;
        this.id = new HostProviderUserId(user.getId(), hostProvider.getId());
    }

    public HostProviderUserId getId() {
        return id;
    }

    public void setId(HostProviderUserId id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public HostProvider getHostProvider() {
        return hostProvider;
    }

    public void setHostProvider(HostProvider hostProvider) {
        this.hostProvider = hostProvider;
    }

    public String getUserIdInHostProvider() {
        return userIdInHostProvider;
    }

    public void setUserIdInHostProvider(String userIdInHostProvider) {
        this.userIdInHostProvider = userIdInHostProvider;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (null == o || getClass() != o.getClass())
            return false;

        HostProviderUser that = (HostProviderUser) o;
        return Objects.equals(user, that.user) &&
                Objects.equals(hostProvider, that.hostProvider);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, hostProvider);
    }
}

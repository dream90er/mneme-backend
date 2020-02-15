package net.ddns.la90zy.mnemo.syncservice.entity;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class HostProviderUserId implements Serializable {

    private Long userId;

    private  Long hostProviderId;

    public HostProviderUserId() {}

    public HostProviderUserId(Long userId, Long hostProviderId) {
        this.userId = userId;
        this.hostProviderId = hostProviderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getHostProviderId() {
        return hostProviderId;
    }

    public void setHostProviderId(Long hostProviderId) {
        this.hostProviderId = hostProviderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (null == o || getClass() != o.getClass())
            return false;

        HostProviderUserId that = (HostProviderUserId) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(hostProviderId, that.hostProviderId);
    }

    @Override
    public int hashCode() { return Objects.hash(userId, hostProviderId); }
}

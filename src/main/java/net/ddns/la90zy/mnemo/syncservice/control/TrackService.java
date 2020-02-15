package net.ddns.la90zy.mnemo.syncservice.control;

import net.ddns.la90zy.mnemo.syncservice.entity.HostProvider;
import net.ddns.la90zy.mnemo.syncservice.entity.Track;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Stateful
public class TrackService implements Serializable {

    private static final long serialVersionUID = 1L;

    @PersistenceContext
    EntityManager em;

    public Track saveTrack(Track track) {
        if (null == track.getId()) {
            em.persist(track);
        } else {
            track = em.merge(track);
        }
        return track;
    }

    public Track getTrackById(Long id) {
        return em.find(Track.class, id);
    }

    public List<Track> getTracks() {
        TypedQuery<Track> query = em.createNamedQuery("Track.findAll", Track.class);
        return query.getResultList();
    }

    public Optional<Track> getTrackByHostProviderSpecificId(String id, HostProvider hostProvider) {
        TypedQuery<Track> query = em.createNamedQuery("Track.findByHostProviderSpecificId", Track.class);
        query.setParameter("id", id)
                .setParameter("hostProvider", hostProvider);
        List<Track> result = query.getResultList();
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    public void deleteTrack(Track track) {
        if (em.contains(track)) {
            em.remove(track);
        } else {
            em.merge(track);
        }
    }
}

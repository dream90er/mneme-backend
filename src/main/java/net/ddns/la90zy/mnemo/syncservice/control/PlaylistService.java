package net.ddns.la90zy.mnemo.syncservice.control;

import net.ddns.la90zy.mnemo.syncservice.entity.HostProvider;
import net.ddns.la90zy.mnemo.syncservice.entity.Playlist;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Stateless
public class PlaylistService implements Serializable {

    private static final long serialVersionUID = 1L;

    @PersistenceContext
    private EntityManager em;

    public Playlist savePlaylist(Playlist playlist) {
        if (null == playlist.getId()) {
            em.persist(playlist);
        } else {
            playlist = em.merge(playlist);
        }
        return playlist;
    }

    public Playlist getPlaylistById(Long id) {
        return em.find(Playlist.class, id);
    }

    public List<Playlist> getPlaylists() {
        TypedQuery<Playlist> query = em.createNamedQuery("Playlist.findAll", Playlist.class);
        return query.getResultList();
    }

    public Optional<Playlist> getPlaylistByHostProviderSpecificId(String id, HostProvider hostProvider) {
        TypedQuery<Playlist> query = em.createNamedQuery("Playlist.findByHostProviderSpecificId", Playlist.class);
        query.setParameter("id", id)
                .setParameter("hostProvider", hostProvider);
        List<Playlist> result = query.getResultList();
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    public void deletePlaylist(Playlist playlist) {
        if (em.contains(playlist)) {
            em.remove(playlist);
        } else {
            em.merge(playlist);
        }
    }
}

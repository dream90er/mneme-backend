package net.ddns.la90zy.mnemo.syncservice.control;

import net.ddns.la90zy.mnemo.syncservice.entity.HostProvider;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Stateful
public class HostProviderService implements Serializable {

    private static final long serialVersionUID = 1L;

    @PersistenceContext
    EntityManager em;

    public HostProvider saveHostProvider(HostProvider hostProvider) {
        if (null == hostProvider.getId()) {
            em.persist(hostProvider);
        } else {
            hostProvider = em.merge(hostProvider);
        }
        return hostProvider;
    }

    public HostProvider getHostProviderById(Long id) {
        return em.find(HostProvider.class, id);
    }

    public List<HostProvider> getHostProviders() {
        TypedQuery<HostProvider> query = em.createNamedQuery("HostProvider.findAll", HostProvider.class);
        return query.getResultList();
    }

    public Optional<HostProvider> getHostProviderByTitle(String title) {
        TypedQuery<HostProvider> query = em.createNamedQuery("HostProvider.findByTitle", HostProvider.class);
        query.setParameter("title", title);
        List<HostProvider> result = query.getResultList();
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    public void deleteHostProvider(HostProvider hostProvider) {
        if (em.contains(hostProvider)) {
            em.remove(hostProvider);
        } else {
            em.merge(hostProvider);
        }
    }
}

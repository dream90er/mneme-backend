package net.ddns.la90zy.mneme.syncservice.control;

import net.ddns.la90zy.mneme.syncservice.entity.User;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Stateful
public class UserService implements Serializable {

    private static final long serialVersionUID = 1L;

    @PersistenceContext
    private EntityManager em;

    public User saveUser(User user) {
        if (null == user.getId()) {
            em.persist(user);
        } else {
            user = em.merge(user);
        }
        return user;
    }

    public User getUserById(Long id) {
        return em.find(User.class, id);
    }

    public List<User> getUsers() {
        TypedQuery<User> query = em.createNamedQuery("User.findAll", User.class);
        return query.getResultList();
    }

    public Optional<User> getUserByLogin(String login) {
        TypedQuery<User> query = em.createNamedQuery("User.findByLogin", User.class);
        query.setParameter("login", login);
        List<User> result = query.getResultList();
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    public void deleteUser(User user) {
        if (em.contains(user)) {
            em.remove(user);
        } else {
            em.merge(user);
        }
    }
}

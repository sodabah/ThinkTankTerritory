package zli.m223.service;

import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import zli.m223.model.ApplicationUser;

@ApplicationScoped
public class ApplicationUserService {
    @Inject
    EntityManager entityManager;

    @Transactional
    public ApplicationUser createUser(ApplicationUser user) {
    if (findAll().isEmpty()) { 
        user.setRole("Admin");
    } else {
        user.setRole("User");
    }
    return entityManager.merge(user);
}
    @Transactional
    public ApplicationUser deleteUser(Long id) {
        var entity = entityManager.find(ApplicationUser.class, id);
        entityManager.remove(entity);
    }

    @Transactional
    public ApplicationUser updateUser(Long id, ApplicationUser user) {
        user.setId(id);
        return entityManager.merge(user);
    }

    public List<ApplicationUser> findAll() {
        var query = entityManager.createQuery("FROM ApplicationUser", ApplicationUser.class);
        return query.getResultList();
    }

    public Optional<ApplicationUser> findByEmail(String email) {
        return entityManager
                .createNamedQuery("ApplicationUser.findByEmail", ApplicationUser.class)
                .setParameter("email", email)
                .getResultStream()
                .findFirst();
    }
}

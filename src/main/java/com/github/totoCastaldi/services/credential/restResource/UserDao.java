package com.github.totoCastaldi.services.credential.restResource;

import com.google.common.base.Optional;
import com.google.common.collect.Iterables;
import com.google.inject.persist.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * Created by toto on 14/11/15.
 */
public class UserDao {

    private final EntityManager entityManager;

    @Inject
    public UserDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Transactional
    public Long create(CreateUserRequest request) {
        ExamplePersonEntity examplePersonEntity = new ExamplePersonEntity();
        examplePersonEntity.setName(request.getName());
        entityManager.persist(examplePersonEntity);
        entityManager.flush();
        return examplePersonEntity.getId();
    }

    public Optional<ExamplePersonEntity> getNameById(Long id) {
        Query query = entityManager.createNamedQuery(ExamplePersonEntity.NQfindById);
        query.setParameter("id", id);
        return Optional.fromNullable((ExamplePersonEntity) Iterables.getFirst(query.getResultList(), null));
    }
}

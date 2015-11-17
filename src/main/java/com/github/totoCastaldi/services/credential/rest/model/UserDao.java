package com.github.totoCastaldi.services.credential.rest.model;

import com.github.totoCastaldi.services.credential.rest.service.UserPassword;
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
    private final UserPassword userPassword;

    @Inject
    public UserDao(
            EntityManager entityManager,
            UserPassword userPassword
    ) {

        this.entityManager = entityManager;
        this.userPassword = userPassword;
    }


    @Transactional
    public Optional<UserModel> create(String email, String password) {
        Optional<UserModel> existant = getByEmail(email);
        if (existant.isPresent()) {
            return Optional.absent();
        } else {
            UserModel userModel = new UserModel();
            userModel.setEmail(email);
            userModel.setPassword(userPassword.getPassword(email, password));
            entityManager.persist(userModel);
            entityManager.flush();
            return Optional.of(userModel);
        }
    }

    public Optional<UserModel> getByEmail(String email) {
        Query query = entityManager.createNamedQuery(UserModel.NQfindByEmail);
        query.setParameter("email", email);
        return Optional.fromNullable((UserModel) Iterables.getFirst(query.getResultList(), null));
    }
}

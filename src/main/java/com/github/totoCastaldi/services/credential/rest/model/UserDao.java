package com.github.totoCastaldi.services.credential.rest.model;

import com.github.totoCastaldi.restServer.TimeProvider;
import com.github.totoCastaldi.services.credential.rest.service.UserPassword;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.inject.persist.Transactional;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * Created by toto on 14/11/15.
 */
public class UserDao {

    private final EntityManager entityManager;
    private final UserPassword userPassword;
    private final TimeProvider timeProvider;

    @Inject
    public UserDao(
            EntityManager entityManager,
            UserPassword userPassword,
            TimeProvider timeProvider
    ) {
        this.entityManager = entityManager;
        this.userPassword = userPassword;
        this.timeProvider = timeProvider;
    }


    @Transactional
    public Optional<UserModel> create(
            String email,
            String password,
            String urlNotifier
    ) {
        Optional<UserModel> existant = getValidUserByEmail(email);
        if (existant.isPresent()) {
            return Optional.absent();
        } else {
            UserModel userModel = new UserModel();
            userModel.setEmail(email);
            userModel.setEncodedPassword(userPassword.encodePassword(email, password));
            userModel.setCreationDate(timeProvider.now());
            userModel.setUserState(UserState.LIGTH);
            userModel.setUrlNotifier(urlNotifier);
            entityManager.persist(userModel);
            entityManager.flush();
            return Optional.of(userModel);
        }
    }

    public Optional<UserModel> getValidUserByEmail(String email) {
        Query query = entityManager.createNamedQuery(UserModel.NQfindByEmail);
        query.setParameter("email", email);
        return Optional.fromNullable((UserModel) Iterables.getFirst(Iterables.filter(query.getResultList(), new Predicate() {
            @Override
            public boolean apply(@Nullable Object input) {
                UserModel userModel = (UserModel) input;
                return userModel.getUserState() != UserState.DELETED;
            }
        }), null));
    }

    @Transactional
    public Optional<UserModel> confirmed(String email) {
        final Optional<UserModel> byEmail = getValidUserByEmail(email);
        if (byEmail.isPresent()) {
            final UserModel userModel = byEmail.get();
            userModel.setUserState(UserState.CONFIRMED);
            entityManager.persist(userModel);
            entityManager.flush();
            return Optional.of(userModel);
        } else {
            return Optional.absent();
        }
    }

    @Transactional
    public Optional<UserModel> updatePassword(String email, String password) {
        Optional<UserModel> existant = getValidUserByEmail(email);
        if (existant.isPresent()) {
            UserModel userModel = existant.get();
            userModel.setEncodedPassword(userPassword.encodePassword(email, password));
            entityManager.persist(userModel);
            entityManager.flush();
            return Optional.of(userModel);
        } else {
            return Optional.absent();
        }

    }
}

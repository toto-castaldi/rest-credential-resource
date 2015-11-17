package com.github.totoCastaldi.services.credential.rest.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by toto on 16/11/15.
 */
@Entity
@RequiredArgsConstructor
@NoArgsConstructor
@Table(name = "user_credential")
@Getter
@NamedQuery(name= UserModel.NQfindByEmail, query="SELECT c FROM UserModel c WHERE c.email = :email")
public class UserModel {

    public final static String NQfindByEmail = "user_1";

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @NonNull
    @NotNull
    @Setter
    private String email;

    @NonNull
    @NotNull
    @Setter
    private String password;

    @Setter
    private String urlNotifier;

    @Setter
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @Setter
    @Enumerated (EnumType.STRING)
    private UserState userState;
}

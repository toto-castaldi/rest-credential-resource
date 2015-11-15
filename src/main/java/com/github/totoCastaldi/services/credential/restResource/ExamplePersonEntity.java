package com.github.totoCastaldi.services.credential.restResource;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by toto on 15/11/15.
 */
@Entity
@RequiredArgsConstructor
@NoArgsConstructor
@Table(name = "person")
@Getter
@NamedQuery(name= ExamplePersonEntity.NQfindById, query="SELECT c FROM ExamplePersonEntity c WHERE c.id = :id")
public class ExamplePersonEntity {

    public final static String NQfindById = "LigthUser_q1";

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @NonNull
    @NotNull
    @Setter
    private String name;


}

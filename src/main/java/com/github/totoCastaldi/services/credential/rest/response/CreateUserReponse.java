package com.github.totoCastaldi.services.credential.rest.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

/**
 * Created by toto on 17/11/15.
 */
@ToString
@Setter
@Getter
@AllArgsConstructor (staticName = "of")
public class CreateUserReponse {
    private String token;
}

package com.github.totoCastaldi.services.credential.rest.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;


/**
 * Created by toto on 14/11/15.
 */
@ToString
@Setter
@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateUserRequest {
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    private String urlNotifier;
    private String urlBaseConfirm;
}

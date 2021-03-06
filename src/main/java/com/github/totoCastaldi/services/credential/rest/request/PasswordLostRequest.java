package com.github.totoCastaldi.services.credential.rest.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by toto on 18/11/15.
 */
@ToString
@Setter
@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PasswordLostRequest {
    @NotBlank
    private String baseUrl;
    @NotBlank
    @Email
    private String email;
    private Boolean skipEmailSend;
}

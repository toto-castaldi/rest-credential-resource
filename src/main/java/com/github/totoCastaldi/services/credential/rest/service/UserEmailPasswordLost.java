package com.github.totoCastaldi.services.credential.rest.service;

import com.github.totoCastaldi.services.credential.rest.Conf;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by toto on 16/11/15.
 */
@Slf4j
public class UserEmailPasswordLost {

    private final String emailAddress;
    private final String emailUsername;
    private final String emailPassword;

    @Inject
    public UserEmailPasswordLost(
            @Named (Conf.MAIL_ADDRESS) String emailAddress,
            @Named (Conf.MAIL_USERNAME) String emailUsername,
            @Named (Conf.MAIL_PASSWORD) String emailPassword
    ) {
        this.emailAddress = emailAddress;
        this.emailUsername = emailUsername;
        this.emailPassword = emailPassword;
    }

    public boolean sendEmail(String emailAddress, String token, String urlBaseConfirm)  {
        return new UserEmail(emailAddress, emailUsername, emailPassword) {

            @Override
            protected String getMessage() {
                return "Click here to reset password here " + urlBaseConfirm + "?e=" + emailAddress + "&t=" + token;
            }

            @Override
            protected String getSubject() {
                return "password recover";
            }
        }.sendEmail(emailAddress);
    }
}

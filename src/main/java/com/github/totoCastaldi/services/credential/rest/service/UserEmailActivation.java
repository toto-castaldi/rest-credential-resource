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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by toto on 16/11/15.
 */
@Slf4j
public class UserEmailActivation {

    private final String emailAddress;
    private final String emailUsername;
    private final String emailPassword;
    private final String smtpServer;
    private final int smtpPort;
    private final boolean authenticate;
    private final boolean sslOnConnect;

    @Inject
    public UserEmailActivation(
        @Named (Conf.MAIL_ADDRESS) String emailAddress,
        @Named (Conf.MAIL_USERNAME) String emailUsername,
        @Named (Conf.MAIL_PASSWORD) String emailPassword,
        @Named (Conf.MAIL_HOST) String smtpServer,
        @Named (Conf.MAIL_PORT) int smtpPort,
        @Named (Conf.MAIL_AUTHENTICATE) boolean authenticate,
        @Named (Conf.MAIL_SSL_ON_CONNECT) boolean sslOnConnect
    ) {
        this.emailAddress = emailAddress;
        this.emailUsername = emailUsername;
        this.emailPassword = emailPassword;
        this.smtpServer = smtpServer;
        this.smtpPort = smtpPort;
        this.authenticate = authenticate;
        this.sslOnConnect = sslOnConnect;
    }

    public boolean sendEmail(String emailAddress, String token, String urlBaseConfirm)  {
        return new UserEmail(emailAddress, emailUsername, emailPassword, smtpServer, smtpPort, authenticate, sslOnConnect) {

            @Override
            protected String getMessage() {
                try {
                    return "Confirm here " + urlBaseConfirm + "?" + URLEncoder.encode("e=" + emailAddress + "&t=" + token, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            protected String getSubject() {
                return "confirm your email";
            }
        }.sendEmail(emailAddress);
    }

}

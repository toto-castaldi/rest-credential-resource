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
 * Created by toto on 18/11/15.
 */
@Slf4j
public abstract class UserEmail {

    private final String emailAddress;
    private final String emailUsername;
    private final String emailPassword;
    private final String smtpServer;
    private final int smtpPort;
    private final boolean authenticate;
    private final boolean sslOnConnect;

    public UserEmail(
           String emailAddress,
           String emailUsername,
           String emailPassword,
           String smtpServer,
           int smtpPort,
           boolean authenticate,
           boolean sslOnConnect
    ) {

        this.emailAddress = emailAddress;
        this.emailUsername = emailUsername;
        this.emailPassword = emailPassword;
        this.smtpServer = smtpServer;
        this.smtpPort = smtpPort;
        this.authenticate = authenticate;
        this.sslOnConnect = sslOnConnect;
    }

    public boolean sendEmail(String emailAddress)  {
        log.info("send email request to {}", emailAddress);
        Email email = new SimpleEmail();
        email.setHostName(smtpServer);
        email.setSmtpPort(smtpPort);
        if (authenticate) {
            email.setAuthenticator(new DefaultAuthenticator(emailUsername, emailPassword));
        }
        if (sslOnConnect) {
            email.setSSLOnConnect(true);
        }
        try {
            email.setFrom(emailAddress);
            email.setSubject(getSubject());
            email.setMsg(getMessage());
            email.addTo(emailAddress);
            final String sendResult = email.send();

            log.info("send email result {}", sendResult);

            return true;
        } catch (EmailException e) {
            log.error(StringUtils.EMPTY, e);
            return false;
        }

    }

    protected abstract String getMessage();

    protected abstract String getSubject();

}

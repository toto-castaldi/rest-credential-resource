package com.github.totoCastaldi;

import com.github.totoCastaldi.integration.db.Postgresql;
import com.github.totoCastaldi.integration.mail.SimpleSmtpServer;
import com.github.totoCastaldi.integration.net.Header;
import com.github.totoCastaldi.integration.net.Rest;

/**
 * Created by toto on 17/02/16.
 */
public class Services {

    public static Rest credentialRest() {
        return  new Rest("localhost", 8080)
                .setContext("/credential")
                .addHeader("x-mashape-proxy-secret", "setit")
                .addHeader(Header.CONTENT_TYPE, "application/json")
                .addHeader(Header.ACCEPT, "application/json")
                ;
    }

    public static Postgresql postgresql() {
        return new Postgresql("localhost", "developer", "developer", "developer", 5432);
    }

    public static SimpleSmtpServer simpleSmtp() {
        return new SimpleSmtpServer("localhost", 7000);
    }
}

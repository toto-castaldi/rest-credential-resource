package com.github.totoCastaldi;


import com.github.totoCastaldi.integration.db.Postgresql;
import com.github.totoCastaldi.integration.mail.SimpleSmtpMessage;
import com.github.totoCastaldi.integration.mail.SimpleSmtpServer;
import com.github.totoCastaldi.integration.net.HttpResource;
import com.github.totoCastaldi.integration.net.Rest;
import com.github.totoCastaldi.integration.templates.JSONTemplate;
import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.internal.matchers.StringContains.containsString;

public class UserTest {

    private static SimpleSmtpServer smtpServer;

    @BeforeClass
    public static void smtpStart() throws SQLException, ClassNotFoundException {
        smtpServer = Services.simpleSmtp();
    }

    @Before
    public void firstUser() throws SQLException, ClassNotFoundException, IOException {
        final Postgresql postgresql = Services.postgresql();
        postgresql.emptyTable("user_credential");

        smtpServer.clearEmails();
    }

    @Test
    public void createWithoutEmail() throws IOException, SQLException, ClassNotFoundException, InterruptedException {
        final Rest rest = Services.credentialRest();

        final HttpResource service = rest.post("/user", new JSONTemplate(new FileInputStream("src/test/resources/createUser.template.json"), new ImmutableMap.Builder<String, String>()
                .put("email", "toto.castaldi+1@gmail.com")
                .put("password", "password")
                .build()).asInputStream());

        final int responseCode = service.getResponseCode();

        assertThat(responseCode, equalTo(201));
    }

    @Test
    public void createWithEmail() throws IOException, SQLException, ClassNotFoundException, InterruptedException {
        final Rest rest = Services.credentialRest();

        final HttpResource service = rest.post("/user", new JSONTemplate(new FileInputStream("src/test/resources/createUser.withEmail.template.json"), new ImmutableMap.Builder<String, String>()
                .put("email", "toto.castaldi+1@gmail.com")
                .put("password", "password")
                .put("urlBaseConfirm", "http://localhost:8080/test")
                .build()).asInputStream());

        final int responseCode = service.getResponseCode();

        assertThat(responseCode, equalTo(201));

        final Collection<SimpleSmtpMessage> receivedEmail = smtpServer.getReceivedEmail();

        assertThat(receivedEmail.size(), equalTo(1));

        final SimpleSmtpMessage smtpMessage = receivedEmail.iterator().next();

        final String body = smtpMessage.getBody();
        System.out.println(body);

        assertThat(body, containsString("toto.castaldi%2B1%40gmail.com"));
    }



}
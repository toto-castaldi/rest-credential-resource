package com.github.totoCastaldi;


import com.github.totoCastaldi.integration.db.Postgresql;
import com.github.totoCastaldi.integration.net.HttpResource;
import com.github.totoCastaldi.integration.net.Rest;
import com.github.totoCastaldi.integration.templates.JSONTemplate;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class UserTest {

    @Test
    public void setup() throws SQLException, ClassNotFoundException {
        final Postgresql postgresql = Services.postgresql();
        postgresql.emptyTable("user_credential");
    }

    @Test
    public void create() throws IOException, SQLException, ClassNotFoundException, InterruptedException {
        final Rest rest = Services.credentialRest();

        final HttpResource service = rest.post("/user", new JSONTemplate(new FileInputStream("src/test/resources/createUser.template.json"), new ImmutableMap.Builder<String, String>()
                .put("email", "toto.castaldi+1@gmail.com")
                .put("password", "password")
                .build()).asInputStream());

        final int responseCode = service.getResponseCode();

        assertThat(responseCode, equalTo(201));
    }



}
package com.github.totoCastaldi;


import com.github.totoCastaldi.integration.net.HttpResource;
import com.github.totoCastaldi.integration.net.Rest;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class NetTest {

    @Test
    public void ping() throws IOException, SQLException, ClassNotFoundException, InterruptedException {
        final Rest rest = Services.credentialRest();

        final HttpResource service = rest.get("/service");
        final int responseCode = service.getResponseCode();

        assertThat(responseCode, equalTo(200));
    }

}
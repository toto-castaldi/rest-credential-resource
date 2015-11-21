package com.github.totoCastaldi.services.credential.rest.resource;

/**
 * Created by toto on 15/11/15.
 */
public class ResourcePath {

    private static final String SLASH = "/";

    public static final String P0 = "p0";
    public static final String P1 = "p1";

    public static final String p_P0 = "{" + P0 + "}";
    public static final String p_P1 = "{" + P1 + "}";

    public static final String USER = "user";
    public static final String CONFIR_TOKEN = "confirmToken";
    public static final String LOST_TOKEN = "lostToken";
    public static final String USER_CREDENTIAL = p_P0 + SLASH + p_P1;

}

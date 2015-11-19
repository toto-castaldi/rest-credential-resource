package com.github.totoCastaldi.services.credential.rest.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by toto on 19/11/15.
 */
@Slf4j
public class UserChange {

    public void notifyExternalService(String urlNotifier) {
        if (StringUtils.isBlank(urlNotifier)) return;
        try {
            URL url = new URL(urlNotifier);
            URLConnection urlConnection = url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuffer stringBuffer = new StringBuffer();
            String inputLine;
            while ((inputLine = bufferedReader.readLine()) != null) {
                stringBuffer.append("\n").append(inputLine);
            }
            bufferedReader.close();
            log.info("url change notified {} {} {}", urlNotifier, stringBuffer.toString());
        } catch (java.io.IOException e) {
            log.error("can't notify change {}", urlNotifier, e);
        }
    }
}

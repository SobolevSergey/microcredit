package ru.simplgroupp.util;

import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class WebUtils {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(WebUtils.class.getName());

    public static final String findMyIpAddress(){
        try {
            URL whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
            String ip = in.readLine();
            logger.debug("my ip address={}",ip);
            return ip;
        } catch (IOException e) {
            logger.error("cannot find my ip address ", e);
        }
        return null;

    }
}

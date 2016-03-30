package com.anashidgames.gerdoo.utils;

/**
 * Created by psycho on 3/17/16.
 */
public class PsychoUtils {

    public static String fixUrl(String url){
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;

        return url;
    }
}

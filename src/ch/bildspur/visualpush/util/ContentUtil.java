package ch.bildspur.visualpush.util;

import java.net.URISyntaxException;

/**
 * Created by cansik on 18/08/16.
 */
public class ContentUtil {
    public static String getContent(String name)
    {
        try {
            return ContentUtil.class.getResource("/content/" + name).toURI().getPath();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }
}

package ch.bildspur.visualpush.util;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by cansik on 18/08/16.
 */
public class ContentUtil {
    public static String getContent(String name)
    {
        return "content/" + name;
    }

    public static String getGStreamerContent(String name)
    {
        Path currentRelativePath = Paths.get("");
        String absolutPath = currentRelativePath.toAbsolutePath().toString();
        return "file://" + absolutPath + "/content/" + name;
    }
}

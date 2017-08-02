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
        Path currentRelativePath = Paths.get("");
        String absolutePath = currentRelativePath.toAbsolutePath().toString();
        return absolutePath + "/content/" + name;
    }
}

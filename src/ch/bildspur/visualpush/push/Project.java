package ch.bildspur.visualpush.push;

import ch.bildspur.visualpush.sketch.controller.ConfigurationController;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by cansik on 23.09.16.
 */
public class Project {
    Path visualPath;
    Path configFile;

    public Project(String configFile)
    {
        this.configFile = Paths.get(configFile);
        this.visualPath = Paths.get(ConfigurationController.DEFAULT_VISUAL_DIR);
    }

    public Project(String configFile, String visualPath)
    {
        this.configFile = Paths.get(configFile);
        this.visualPath = Paths.get(visualPath);
    }

    public Path getVisualPath() {
        return visualPath;
    }

    public void setVisualPath(Path visualPath) {
        this.visualPath = visualPath;
    }

    public Path getConfigFile() {
        return configFile;
    }

    public void setConfigFile(Path configFile) {
        this.configFile = configFile;
    }

    public boolean exists()
    {
        return Files.exists(configFile) && !Files.isDirectory(configFile);
    }
}

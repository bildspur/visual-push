package ch.bildspur.visualpush.event;

import ch.bildspur.visualpush.sketch.controller.ConfigurationController;

/**
 * Created by cansik on 05/09/16.
 */
public interface ConfigLoadedHandler {
    void configLoaded(ConfigurationController configurationController);
}

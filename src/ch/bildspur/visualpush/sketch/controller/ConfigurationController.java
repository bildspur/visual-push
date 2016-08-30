package ch.bildspur.visualpush.sketch.controller;

import ch.bildspur.visualpush.sketch.RenderSketch;
import ch.bildspur.visualpush.video.Clip;
import processing.core.PApplet;
import processing.data.JSONArray;
import processing.data.JSONObject;

/**
 * Created by cansik on 30/08/16.
 */
public class ConfigurationController extends PushController {
    final String CONFIG_DIR = "config/";

    protected RenderSketch sketch;

    public void setup(PApplet sketch) {
        super.setup(sketch);
        this.sketch = (RenderSketch)sketch;
    }

    public void load(String fileName)
    {

    }

    public void save(String fileName)
    {
        JSONObject root = new JSONObject();
        JSONArray clips = new JSONArray();

        // save clip
        Clip[][] grid = sketch.getClips().getClipGrid();

        for(int u = 0; u < grid.length; u++)
            for(int v = 0; v < grid[u].length; v++)
                if(grid[u][v] != null)
                    clips.append(getClipJSON(grid[u][v], u, v));
        root.setJSONArray("clips", clips);

        // write file
        sketch.saveJSONObject(root, CONFIG_DIR + fileName);
    }

    private JSONObject getClipJSON(Clip clip, int row, int column)
    {
        JSONObject json = new JSONObject();

        json.setInt("row", row);
        json.setInt("column", column);

        json.setString("path", clip.filename);
        json.setInt("playMode", clip.getPlayMode().getValue().getIntValue());
        json.setFloat("opacity", clip.getOpacity().getValue());
        json.setFloat("startTime", clip.getStartTime().getValue());
        json.setFloat("endTime", clip.getStartTime().getValue());
        json.setFloat("speed", clip.getSpeed().getValue());
        json.setFloat("zoom", clip.getZoom().getValue());
        json.setInt("blendMode", clip.getBlendMode().getValue().getValue());

        return json;
    }
}

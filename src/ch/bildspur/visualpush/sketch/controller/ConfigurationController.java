package ch.bildspur.visualpush.sketch.controller;

import ch.bildspur.visualpush.sketch.RenderSketch;
import ch.bildspur.visualpush.video.BlendMode;
import ch.bildspur.visualpush.video.Clip;
import ch.bildspur.visualpush.video.playmode.PlayMode;
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
        JSONObject root = sketch.loadJSONObject(CONFIG_DIR + fileName);
        JSONArray clips = root.getJSONArray("clips");

        // load clips from config
        Clip[][] grid = sketch.getClips().getClipGrid();
        for(int i = 0; i < clips.size(); i++)
        {
            JSONObject clipJSON = clips.getJSONObject(i);

            int row = clipJSON.getInt("row");
            int column = clipJSON.getInt("column");

            grid[row][column] = loadClip(clipJSON);
        }

        System.out.println(clips.size() + " clips loaded!");
    }

    public Thread loadAsync(String fileName)
    {
        Thread t = new Thread(() -> {
            load(fileName);
        });
        t.start();
        return t;
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

    private Clip loadClip(JSONObject json)
    {
        Clip c = new Clip(sketch, json.getString("path"));

        c.getPlayMode().setValue(PlayMode.getPlayMode(json.getInt("playMode")));
        c.getOpacity().setValue(json.getFloat("opacity"));
        c.getStartTime().setValue(json.getFloat("startTime"));
        c.getEndTime().setValue(json.getFloat("endTime"));
        c.getSpeed().setValue(json.getFloat("speed"));
        c.getZoom().setValue(json.getFloat("zoom"));
        c.getBlendMode().setValue(BlendMode.fromInteger(json.getInt("blendMode")));

        return c;
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
        json.setInt("blendMode", clip.getBlendMode().getValue().getIntValue());

        return json;
    }
}

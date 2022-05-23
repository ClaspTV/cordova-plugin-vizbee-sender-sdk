package tv.vizbee.cdsender;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.HashMap;

public class VizbeeVideo {

    public static final String LOG_TAG = VizbeeVideo.class.getName();

    private String guid;

    // metadata
    private String title;
    private String subtitle;
    private String imageUrl;
    private boolean isLive;

    // streamInfo
    private String streamUrl;
    // this.tracks = {};
    private double startPositionInSeconds = 0;

    // custom 
    private JSONObject customProperties;

    public java.lang.String getGuid() {
        return guid;
    }

    public java.lang.String getTitle() {
        return title;
    }

    public java.lang.String getSubtitle() {
        return subtitle;
    }

    public java.lang.String getImageUrl() {
        return imageUrl;
    }

    public boolean isLive() {
        return isLive;
    }

    public java.lang.String getStreamUrl() {
        return streamUrl;
    }

    public double getStartPositionInSeconds() {
        return startPositionInSeconds;
    }

    public void setStartPositionInSeconds(double position) {
        this.startPositionInSeconds = position;
    }

    public JSONObject getCustomProperties() {
        return customProperties;
    }

    public VizbeeVideo(JSONObject videoJsonObject) {

        try {
            guid = videoJsonObject.has("guid") ? videoJsonObject.getString("guid") : "";

            title = videoJsonObject.has("title") ? videoJsonObject.getString("title") : "";
            subtitle = videoJsonObject.has("subtitle") ? videoJsonObject.getString("subtitle") : "";
            imageUrl = videoJsonObject.has("imageUrl") ? videoJsonObject.getString("imageUrl") : "";
            isLive = videoJsonObject.has("isLive") && videoJsonObject.getBoolean("isLive");

            streamUrl = videoJsonObject.has("streamUrl") ? videoJsonObject.getString("streamUrl") : "";
            startPositionInSeconds = videoJsonObject.has("startPositionInSeconds") ? videoJsonObject.getDouble("startPositionInSeconds") : 0;

            if (videoJsonObject.has("customProperties")) {
                customProperties = videoJsonObject.getJSONObject("customProperties");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
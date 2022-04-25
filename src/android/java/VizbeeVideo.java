package tv.vizbee.cdsender;

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
    private Map<String, Object> customProperties = new HashMap<String, Object>();

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

    public Map<String, Object> getCustomProperties() {
        return customProperties;
    }

    public VizbeeVideo(VizbeeVideo vizbeeVideo) {

        guid = (null != vizbeeVideo.guid) ? vizbeeVideo.guid : "";
        
        title = (null != vizbeeVideo.title) ? vizbeeVideo.title : "";
        subtitle = (null != vizbeeVideo.subtitle) ? vizbeeVideo.subtitle : "";
        imageUrl = (null != vizbeeVideo.imageUrl) ? vizbeeVideo.imageUrl : "";
        isLive = vizbeeVideo.isLive;

        streamUrl = (null != vizbeeVideo.streamUrl) ? vizbeeVideo.streamUrl : "";
        startPositionInSeconds = vizbeeVideo.startPositionInSeconds;

        customProperties = new HashMap<>();
        if (null != vizbeeVideo.customProperties) {
            customProperties = vizbeeVideo.customProperties;
        }
    }

}
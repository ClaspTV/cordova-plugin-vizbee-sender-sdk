package tv.vizbee.cdsender;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.gms.cast.framework.CastContext;
import com.mgm.apps.roar.R;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tv.vizbee.api.RemoteButton;
import tv.vizbee.api.VizbeeContext;
import tv.vizbee.api.VizbeeOptions;
import tv.vizbee.api.session.SessionState;
import tv.vizbee.api.session.SessionStateListener;
import tv.vizbee.api.session.VideoClient;
import tv.vizbee.api.session.VideoStatus;
import tv.vizbee.api.session.VizbeeSession;
import tv.vizbee.api.session.VizbeeSessionManager;

public class VizbeeNativeManager extends CordovaPlugin {

    private static final String LOG_TAG = VizbeeNativeManager.class.getName();

    //----------------
    // Cordova execute
    //----------------

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext cbContext) throws JSONException {

        Log.i(LOG_TAG, "execute " + action);

        switch (action) {
            case "setChromecastAppStoreId": {
                CastOptionsProvider.receiverAppId = args.get(0).toString();
                break;
            }
            case "initialize": {
                String vizbeeAppId = args.get(0).toString();
                cordova.getActivity().runOnUiThread(() -> initialize(vizbeeAppId));
                break;
            }
            case "addCastIcon": {
                int x = (int) args.get(0);
                int y = (int) args.get(1);
                int width = (int) args.get(2);
                int heigt = (int) args.get(3);
                cordova.getActivity().runOnUiThread(() -> addCastIcon(x, y, width, heigt));
                break;
            }
            case "removeCastIcon": {
                cordova.getActivity().runOnUiThread(this::removeCastIcon);
                break;
            }
            case "smartPrompt": {
                cordova.getActivity().runOnUiThread(this::smartPrompt);
                break;
            }
            case "smartPlay": {
                JSONObject vizbeeVideoMap = (JSONObject) args.get(0);
                cordova.getActivity().runOnUiThread(() -> smartPlay(vizbeeVideoMap, cbContext));
                break;
            }
        }
        return true;
    }

    //----------------
    // Initialize
    //----------------

    public void initialize(String appId) {

        Log.v(LOG_TAG, "initialize");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            cordova.getActivity().onTopResumedActivityChanged(true);
        }

        // init Vizbee after castContext setup for lock/notification controls
        CastContext.getSharedInstance(cordova.getActivity());

        VizbeeOptions options = new VizbeeOptions.Builder().enableProduction(true).build();
        VizbeeContext.getInstance().enableVerboseLogging();
        VizbeeContext.getInstance().init(cordova.getActivity().getApplication(), appId, new VizbeeAppAdapter(), options);
    }

    //----------------
    // UI - CastButton
    //----------------

    public void addCastIcon(int x, int y, int width, int height) {

        Log.v(LOG_TAG, "Adding CastIcon at x " + x + " y "  + y + " width " + width + " height " + height);

        LayoutInflater inflater = (LayoutInflater) cordova.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RemoteButton m_button = (RemoteButton) inflater.inflate(R.layout.cast_button, null);

        DisplayMetrics displayMetrics = cordova.getActivity().getResources().getDisplayMetrics();
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                (width * (int)displayMetrics.density),
                (height * (int)displayMetrics.density));
        params.leftMargin = (int) (x * displayMetrics.density);
        params.topMargin  = (int) (y * displayMetrics.density);

        FrameLayout layout = (FrameLayout) webView.getView().getParent();
        layout.addView(m_button, params);
    }

    public void removeCastIcon() {

        Log.v(LOG_TAG, "Removing CastIcon");

        FrameLayout layout = (FrameLayout) webView.getView().getParent();
        VizbeeCastButtonView vizbeeCastButtonView = null;
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof VizbeeCastButtonView) {
                vizbeeCastButtonView = (VizbeeCastButtonView) child;
                break;
            }
        }
        if (null != vizbeeCastButtonView) {
            Log.v(LOG_TAG, "Found CastIconView and removing");
            layout.removeView(vizbeeCastButtonView);
        }
    }

    //----------------
    // Flow APIs
    //----------------

    public void smartPrompt() {

        Log.v(LOG_TAG, "Invoking smartPrompt");

        Activity activity = cordova.getActivity();
        if (activity == null) {
            Log.e(LOG_TAG, "SmartPrompt - null activity");
            return;
        }

        // Renamed old smartHelp API to new smartPrompt
        VizbeeContext.getInstance().smartHelp(activity);
    }

    public void smartCast() {

        Log.v(LOG_TAG, "Invoking smartCast");

        Activity activity = cordova.getActivity();
        if (activity == null) {
            Log.e(LOG_TAG, "SmartCast - null activity");
            return;
        }

        VizbeeSessionManager sessionManager = VizbeeContext.getInstance().getSessionManager();
        if (null != sessionManager) {
            sessionManager.onCastIconClicked(activity);
        }
    }

    public void smartPlay(JSONObject vizbeeVideoMap, CallbackContext callbackContext) {

        Log.v(LOG_TAG, "Invoking smartPlay");

        Activity activity = cordova.getActivity();
        if (activity == null) {
            Log.e(LOG_TAG, "SmartPlay - null activity");
            return;
        }

        VizbeeVideo vizbeeVideo = new VizbeeVideo(vizbeeVideoMap);

        // IMPORTANT: Android expects position in milliseconds (while iOS expects in seconds!)
        boolean didPlayOnTV = VizbeeContext.getInstance().smartPlay(activity,
                vizbeeVideo, (long)(1000*vizbeeVideo.getStartPositionInSeconds()));
        if (didPlayOnTV) {

          Log.i(LOG_TAG, "SmartPlay success in casting content");
            if (callbackContext != null) {
                callbackContext.success("Playing on TV");
            }

        } else {

          Log.e(LOG_TAG, "SmartPlay failed in casting content");
            if (callbackContext != null) {
                callbackContext.success("Play on Phone");
            }
        }
    }

    //----------------
    // Session APIs
    //----------------

//    public void getSessionState(final Promise promise) {
//
//        cordova.getActivity().runOnUiQueueThread(new Runnable() {
//
//            @Override
//            public void run() {
//
//                VizbeeSessionManager sessionManager = VizbeeContext.getInstance().getSessionManager();
//                if (null == sessionManager) {
//                    promise.resolve(null);
//                    return;
//                }
//
//                promise.resolve(VizbeeNativeManager.this.getSessionStateString(sessionManager.getSessionState()));
//            }
//        });
//    }

//    public void getSessionConnectedDevice(final Promise promise) {
//
//        cordova.getActivity().runOnUiQueueThread(new Runnable() {
//
//            @Override
//            public void run() {
////                promise.resolve(VizbeeNativeManager.this.getSessionConnectedDeviceMap());
//            }
//        });
//    }

    //----------------
    // Video APIs
    //----------------

    public void play() {

        VideoClient videoClient = getSessionVideoClient();
        if (null != videoClient) {
            videoClient.play();
        } else {
            Log.w(LOG_TAG, "Play ignored because videoClient is null");
        }
    }

    public void pause() {
        
        VideoClient videoClient = getSessionVideoClient();
        if (null != videoClient) {
            videoClient.pause();
        } else {
            Log.w(LOG_TAG, "Pause ignored because videoClient is null");
        }
    }

    public void seek(long position) {
        
        VideoClient videoClient = getSessionVideoClient();
        if (null != videoClient) {
            videoClient.seek(position);
        } else {
            Log.w(LOG_TAG, "Seek ignored because videoClient is null");
        }
    }

    public void stop() {
        VideoClient videoClient = getSessionVideoClient();
        if (null != videoClient) {
            videoClient.stop();
        } else {
            Log.w(LOG_TAG, "Stop ignored because videoClient is null");
        }
    }

    //----------------
    // App & session lifecycle
    //----------------
    @Override
    public void onPause(boolean multitasking) {
        super.onPause(multitasking);

        Log.v(LOG_TAG, "onPause");
    }

    @Override
    public void onResume(boolean multitasking) {
        super.onResume(multitasking);

        Log.v(LOG_TAG, "onResume");
        this.addSessionStateListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.v(LOG_TAG, "onDestroy");
        this.removeSessionStateListener();
    }

    private SessionStateListener sessionStateListener;
    private int lastUpdatedState = 0; // UNKNOWN

    private void addSessionStateListener() {

        // sanity
        this.removeSessionStateListener();

        VizbeeSessionManager sessionManager = VizbeeContext.getInstance().getSessionManager();
        if (null != sessionManager) {

            Log.i(LOG_TAG, "Adding session state listener");
            this.sessionStateListener = newState -> {

                // handle videoClient
                if (newState == SessionState.CONNECTED) {
                    VizbeeNativeManager.this.addVideoStatusListener();
                } else {
                    VizbeeNativeManager.this.removeVideoStatusListener();
                }

                VizbeeNativeManager.this.notifySessionStatus(newState);
            };
            sessionManager.addSessionStateListener(this.sessionStateListener);

            // force first update
            this.notifySessionStatus(sessionManager.getSessionState());
        }
    }

    private void removeSessionStateListener() {

        VizbeeSessionManager sessionManager = VizbeeContext.getInstance().getSessionManager();
        if (null != sessionManager) {
            if (null != this.sessionStateListener) {

                Log.i(LOG_TAG, "Removing session state listener");
                sessionManager.removeSessionStateListener(this.sessionStateListener);
            }
        }
        this.sessionStateListener = null;
    }

    private void notifySessionStatus(int newState) {

        if (newState == lastUpdatedState) {
            Log.w(LOG_TAG, "Ignoring duplicate state update");
            return;
        }
        lastUpdatedState = newState;

//        String state = this.getSessionStateString(newState);
//        WritableMap stateMap = Arguments.createMap();
//        stateMap.putString("connectionState", state);
//
//        WritableMap deviceMap = this.getSessionConnectedDeviceMap();
//        if (null != deviceMap) {
//            stateMap.merge(deviceMap);
//        }
//
//        this.sendEvent("VZB_SESSION_STATUS", stateMap);
    }

    private String getSessionStateString(int state) {

        switch (state) {
            case SessionState.NO_DEVICES_AVAILABLE:
                return "NO_DEVICES_AVAILABLE";
            case SessionState.NOT_CONNECTED:
                return "NOT_CONNECTED";
            case SessionState.CONNECTING:
                return "CONNECTING";
            case SessionState.CONNECTED:
                return "CONNECTED";
            default:
                return "UNKNOWN";
        }
    }

//    private WritableMap getSessionConnectedDeviceMap() {
//
//        VizbeeSessionManager sessionManager = VizbeeContext.getInstance().getSessionManager();
//        if (null == sessionManager) {
//            return null;
//        }
//
//        VizbeeSession currentSession = sessionManager.getCurrentSession();
//        if (null == currentSession) {
//            return null;
//        }
//
//        VizbeeScreen screen = currentSession.getVizbeeScreen();
//        if (null == screen) {
//          return null;
//        }
//
//        WritableMap map = Arguments.createMap();
//        map.putString("connectedDeviceType", screen.getScreenType().getTypeName());
//        map.putString("connectedDeviceFriendlyName", screen.getScreenInfo().getFriendlyName());
//        map.putString("connectedDeviceModel", screen.getScreenInfo().getModel());
//        return map;
//    }

    //----------------
    // Video client listener
    //----------------

     private VideoClient.VideoStatusListener videoStatusListener;

    private VideoClient getSessionVideoClient() {

        VizbeeSessionManager sessionManager = VizbeeContext.getInstance().getSessionManager();
        if (null == sessionManager) {
            return null;
        }

        VizbeeSession currentSession = sessionManager.getCurrentSession();
        if (null == currentSession) {
            return null;
        }

        VideoClient videoClient = currentSession.getVideoClient();
        return videoClient;
    }

    private void addVideoStatusListener() {

        // sanity
        this.removeVideoStatusListener();

        Log.v(LOG_TAG, "TRYING to add video status listener");
        VideoClient videoClient = getSessionVideoClient();
        if (null != videoClient) {

            this.videoStatusListener = new VideoClient.VideoStatusListener() {
                
                @Override
                public void onVideoStatusUpdated(VideoStatus status) {
                    VizbeeNativeManager.this.notifyMediaStatus(status);
                }
            };
            videoClient.addVideoStatusListener(this.videoStatusListener);
            Log.i(LOG_TAG, "SUCCESS adding video status listener");

            // force first update
            this.notifyMediaStatus(videoClient.getVideoStatus());
        } else {

            Log.w(LOG_TAG, "FAILED to add video status listener");
        }
    }

    private void removeVideoStatusListener() {

        Log.v(LOG_TAG, "TRYING to remove video status listener");
        VideoClient videoClient = getSessionVideoClient();
        if (null != videoClient) {
            if (null != this.videoStatusListener) {

                Log.i(LOG_TAG, "SUCCESS removing video status listener");
                videoClient.removeVideoStatusListener(this.videoStatusListener);
            }
        }
        this.videoStatusListener = null;
    }

     private void notifyMediaStatus(VideoStatus videoStatus) {

        Log.v(LOG_TAG, "Sending media status ...");
        Log.v(LOG_TAG, videoStatus.toString());
//        WritableMap videoStatusMap = this.getVideoStatusMap(videoStatus);
//        this.sendEvent("VZB_MEDIA_STATUS", videoStatusMap);
    }

//    private WritableMap getVideoStatusMap(VideoStatus videoStatus) {
//
//        WritableMap videoStatusMap = Arguments.createMap();
//        videoStatusMap.putString("guid", videoStatus.getGuid());
//        videoStatusMap.putInt("position", (int) videoStatus.getStreamPosition());
//        videoStatusMap.putInt("duration", (int) videoStatus.getStreamDuration());
//        videoStatusMap.putBoolean("isLive", videoStatus.isStreamLive());
//        videoStatusMap.putString("playerState", this.getPlayerStateString(videoStatus.getPlayerState()));
//        videoStatusMap.putBoolean("isAdPlaying", videoStatus.isAdPlaying());
//
//        return videoStatusMap;
//    }

    private String getPlayerStateString(int state) {

        switch (state) {
            case VideoStatus.PLAYER_STATE_IDLE:
                return "IDLE";
            case VideoStatus.PLAYER_STATE_PLAYING:
                return "PLAYING";
            case VideoStatus.PLAYER_STATE_PAUSED:
                return "PAUSED";
            case VideoStatus.PLAYER_STATE_BUFFERING:
                return "BUFFERING";
            default:
                return "UNKNOWN";
        }
    }

    //----------------
    // Bridge events
    //----------------

//    private void sendEvent(String eventName, @Nullable WritableMap params) {
//        getReactApplicationContext()
//                  .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
//                  .emit(eventName, params);
//    }
}
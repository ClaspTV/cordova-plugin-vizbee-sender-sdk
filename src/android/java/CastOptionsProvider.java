package tv.vizbee.cdsender;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.cast.framework.CastOptions;
import com.google.android.gms.cast.framework.OptionsProvider;
import com.google.android.gms.cast.framework.SessionProvider;
import com.google.android.gms.cast.framework.media.CastMediaOptions;
import com.google.android.gms.cast.framework.media.MediaIntentReceiver;
import com.google.android.gms.cast.framework.media.NotificationOptions;

import java.util.ArrayList;
import java.util.List;

import tv.vizbee.api.RemoteActivity;

public class CastOptionsProvider implements OptionsProvider {

    private static final String LOG_TAG = CastOptionsProvider.class.getName();

    public static String receiverAppId;

    @Override
    public CastOptions getCastOptions(Context context) {

        // Showing 4 buttons: "rewind", "play/pause", "forward" and "stop casting".
        List<String> buttonActions = new ArrayList<>();
        buttonActions.add(MediaIntentReceiver.ACTION_REWIND);
        buttonActions.add(MediaIntentReceiver.ACTION_TOGGLE_PLAYBACK);
        buttonActions.add(MediaIntentReceiver.ACTION_FORWARD);
        buttonActions.add(MediaIntentReceiver.ACTION_STOP_CASTING);

        // Showing "play/pause" and "stop casting" in the compat view of the notification.
        int[] compatButtonActionsIndicies = new int[]{ 1, 3 };

        // Builds a notification with the above actions. Each tap on the "rewind" and
        // "forward" buttons skips 30 seconds.
        NotificationOptions notificationOptions = new NotificationOptions.Builder()
                .setActions(buttonActions, compatButtonActionsIndicies)
                .setSkipStepMs(30000l)
                .setTargetActivityClassName(RemoteActivity.class.getName())
                .build();

        final CastMediaOptions mediaOptions = new CastMediaOptions.Builder()
                .setNotificationOptions(notificationOptions)
                .build();

        Log.i(LOG_TAG, "Building CastOptions with receiver applicationId " + receiverAppId);
        return new CastOptions.Builder()
                .setReceiverApplicationId(receiverAppId.isEmpty() ? "" : receiverAppId)
                .setCastMediaOptions(mediaOptions)
                .build();
    }

    @Override
    public List<SessionProvider> getAdditionalSessionProviders(Context appContext) {
        return null;
    }
}


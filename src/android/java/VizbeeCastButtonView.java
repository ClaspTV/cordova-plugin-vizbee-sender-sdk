package tv.vizbee.cdsender;

import android.content.Context;
import android.util.Log;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import tv.vizbee.api.RemoteButton;

public class VizbeeCastButtonView extends LinearLayout {

    public static final String TAG = VizbeeCastButtonView.class.getName();
    private RemoteButton m_button;

    public VizbeeCastButtonView(Context context) {
        super(context);

        // button
        m_button = new RemoteButton(context);

        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        int lr_padding = (int) (4 * displayMetrics.density);
        int bt_padding = (int) (3 * displayMetrics.density);
        m_button.setPadding(lr_padding, bt_padding, lr_padding, bt_padding);

        this.addView(m_button);
    }

     @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }
}
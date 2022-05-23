package tv.vizbee.cdsender;

import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.mgm.apps.roar.R;

import tv.vizbee.api.RemoteButton;

public class VizbeeCastButtonView extends FrameLayout {

    public static final String TAG = VizbeeCastButtonView.class.getName();
    private RemoteButton m_button;

    public VizbeeCastButtonView(Context context, int x, int y) {
        super(context);

        // button
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        m_button = (RemoteButton) inflater.inflate(R.layout.cast_button, null);
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
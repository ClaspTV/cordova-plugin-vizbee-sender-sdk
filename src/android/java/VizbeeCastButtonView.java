package tv.vizbee.cdsender;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.FrameLayout;

import tv.vizbee.api.RemoteButton;

public class VizbeeCastButtonView extends FrameLayout {

    public static final String TAG = VizbeeCastButtonView.class.getName();
    private RemoteButton m_button;

    public VizbeeCastButtonView(Context context, int x, int y) {
        super(context);

        // button
        m_button = new RemoteButton(context);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(72, 72);
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        params.leftMargin = (int) (x * displayMetrics.density);
        params.topMargin  = (int) (y * displayMetrics.density);

        this.addView(m_button, params);
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
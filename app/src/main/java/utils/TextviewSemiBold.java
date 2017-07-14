package utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 *
 * Created by Suraj shakya on 26/7/16.
 * shakyasuraj08@gmail.com
 *
 */

public class TextviewSemiBold extends TextView {
    public TextviewSemiBold(Context context) {
        super(context);
        init();
    }

    public TextviewSemiBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextviewSemiBold(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public TextviewSemiBold(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "font/Roboto-Medium.ttf");
        setTypeface(tf);
    }
}

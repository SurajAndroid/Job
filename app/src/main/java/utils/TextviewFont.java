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

public class TextviewFont extends TextView {
    public TextviewFont(Context context) {
        super(context);
        init();
    }

    public TextviewFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextviewFont(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public TextviewFont(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "font/Roboto-Regular.ttf");
        setTypeface(tf);
    }
}

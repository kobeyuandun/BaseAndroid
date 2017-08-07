package com.baseandroid.widget.tintimage;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.baseandroid.R;

public class TintImageView extends ImageView {

    private ColorStateList mTintColorStateList;

    public TintImageView(Context context) {
        this(context, null);
    }

    public TintImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TintImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTint();
    }

    private void initTint() {
        mTintColorStateList = getResources().getColorStateList(R.color.tint_overlay_color);

        Drawable drawable = getDrawable();
        if (drawable != null) {
            setImageDrawable(drawable);
        }
    }

    @Override
    public void setImageResource(int resId) {
        Drawable drawable = getResources().getDrawable(resId);
        setImageDrawable(drawable);
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        if (drawable != null) {
            drawable = tintDrawable(drawable);
        }

        super.setImageDrawable(drawable);
    }

    private Drawable tintDrawable(Drawable drawable) {
        return DrawableTint.tint(drawable, mTintColorStateList, PorterDuff.Mode.MULTIPLY);
    }

}

package com.baseandroid.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.baseandroid.R;
import com.jayfeng.lesscode.core.DisplayLess;
import com.rengwuxian.materialedittext.MaterialEditText;

public class EditTextWithDelete extends MaterialEditText {

    private Rect mBounds;

    private Drawable mDrawableEnd;
    private Drawable mDrawableDelete;

    public EditTextWithDelete(Context context) {
        super(context);
        init(context, null, 0);
    }

    public EditTextWithDelete(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public EditTextWithDelete(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.EditTextWithDelete, defStyle, 0);
        mDrawableDelete = a.getDrawable(R.styleable.EditTextWithDelete_del_icon);
        a.recycle();

        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                showDeleteIcon();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void setCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom) {

        // keep a reference to the right drawable so later on touch we can check if touch is on the drawable
        mDrawableEnd = right;

        super.setCompoundDrawables(left, top, right, bottom);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_UP && mDrawableEnd != null) {
            mBounds = mDrawableEnd.getBounds();
            final int x = (int) event.getX();
            //check if the touch is within bound of drawable End icon
            if (x >= (this.getRight() - DisplayLess.$dp2px(30)) && x <= this.getRight()) {
                setText("");
                showDeleteIcon();
                event.setAction(MotionEvent.ACTION_CANCEL);
            }
        }
        return super.onTouchEvent(event);
    }

    private void showDeleteIcon() {
        if (getText().length() > 0) {
            setCompoundDrawablesWithIntrinsicBounds(getCompoundDrawables()[0], null, mDrawableDelete, null);
        } else {
            setCompoundDrawables(getCompoundDrawables()[0], null, null, null);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        mDrawableEnd = null;
        mBounds = null;
        super.finalize();
    }
}
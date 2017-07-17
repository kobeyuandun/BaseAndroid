package com.android.photocameralib.media;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.android.photocameralib.R;


public class SelectImageCustomView extends SelectImageActivity {

    @Override
    public void SetToolBarView(RelativeLayout relativeLayout) {
        ViewGroup viewGroup = (ViewGroup) relativeLayout.getParent();
        viewGroup.setBackgroundColor(Color.RED);
        Button button = (Button) relativeLayout.findViewById(R.id.btn_title_select);
        button.setTextColor(Color.BLUE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void SetButtonView(FrameLayout frameLayout) {

    }
}

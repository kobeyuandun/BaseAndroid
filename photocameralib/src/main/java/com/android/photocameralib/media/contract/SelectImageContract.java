package com.android.photocameralib.media.contract;

import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public interface SelectImageContract {

    interface Operator {
        void requestCamera();

        void requestExternalStorage();

        void setDataView(View view);

        /*--toolbar--*/
        void SetToolBarView(RelativeLayout relativeLayout);

        /*--preview done--*/
        void SetButtonView(FrameLayout frameLayout);

    }

    interface View {
        void onOpenCameraSuccess();

        void onCameraPermissionDenied();
    }
}

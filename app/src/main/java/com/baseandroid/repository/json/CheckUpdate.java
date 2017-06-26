package com.baseandroid.repository.json;

import com.google.gson.annotations.SerializedName;

public class CheckUpdate {

    @SerializedName("ANDROID_CRM_VERSION.size")
    private String _$ANDROID_CRM_VERSIONSize76;
    private String current_server_time;
    @SerializedName("ANDROID_CRM_VERSION.force_update_version")
    private String _$ANDROID_CRM_VERSIONForce_update_version284;
    @SerializedName("ANDROID_CRM_VERSION.latest_version")
    private String _$ANDROID_CRM_VERSIONLatest_version249;

    public String get_$ANDROID_CRM_VERSIONSize76() {
        return _$ANDROID_CRM_VERSIONSize76;
    }

    public void set_$ANDROID_CRM_VERSIONSize76(String _$ANDROID_CRM_VERSIONSize76) {
        this._$ANDROID_CRM_VERSIONSize76 = _$ANDROID_CRM_VERSIONSize76;
    }

    public String getCurrent_server_time() {
        return current_server_time;
    }

    public void setCurrent_server_time(String current_server_time) {
        this.current_server_time = current_server_time;
    }

    public String get_$ANDROID_CRM_VERSIONForce_update_version284() {
        return _$ANDROID_CRM_VERSIONForce_update_version284;
    }

    public void set_$ANDROID_CRM_VERSIONForce_update_version284(String _$ANDROID_CRM_VERSIONForce_update_version284) {
        this._$ANDROID_CRM_VERSIONForce_update_version284 = _$ANDROID_CRM_VERSIONForce_update_version284;
    }

    public String get_$ANDROID_CRM_VERSIONLatest_version249() {
        return _$ANDROID_CRM_VERSIONLatest_version249;
    }

    public void set_$ANDROID_CRM_VERSIONLatest_version249(String _$ANDROID_CRM_VERSIONLatest_version249) {
        this._$ANDROID_CRM_VERSIONLatest_version249 = _$ANDROID_CRM_VERSIONLatest_version249;
    }

    @Override
    public String toString() {
        return "CheckUpdate{" + "_$ANDROID_CRM_VERSIONSize76='" + _$ANDROID_CRM_VERSIONSize76 + '\'' + ", current_server_time='" + current_server_time + '\'' + ", _$ANDROID_CRM_VERSIONForce_update_version284='" + _$ANDROID_CRM_VERSIONForce_update_version284 + '\'' + ", _$ANDROID_CRM_VERSIONLatest_version249='" + _$ANDROID_CRM_VERSIONLatest_version249 + '\'' + '}';
    }
}

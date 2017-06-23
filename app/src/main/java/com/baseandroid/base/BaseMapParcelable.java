package com.baseandroid.base;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/*The Map keys must be String objects.The Map values are written using
{@link #writeValue} and must follow
 the specification there.
 Flatten a generic object in to a parcel.  The given Object value may
     * currently be one of the following types:
     * <li> String
     * <li> Byte
     * <li> Short
     * <li> Integer
     * <li> Long
     * <li> Float
     * <li> Double
     * <li> Boolean
     * <li> String[]
     * <li> boolean[]
     * <li> byte[]
     * <li> int[]
     * <li> long[]
     * <li> Map (as supported by {@link #writeMap}).
     * <li> Any object that implements the {@link Parcelable} protocol.
     * <li> Parcelable[]
     * <li> List (as supported by {@link #writeList}).*/
public class BaseMapParcelable implements Parcelable {

    public static String INTENTTAG = "intentMap";

    private Map<String, Object> map = new HashMap<String, Object>();

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeMap(map);
    }

    public static final Creator<BaseMapParcelable> CREATOR = new
            Creator<BaseMapParcelable>() {

        @Override
        public BaseMapParcelable[] newArray(int size) {
            return null;
        }

        @SuppressWarnings("unchecked")
        @Override
        public BaseMapParcelable createFromParcel(Parcel source) {
            BaseMapParcelable hmp = new BaseMapParcelable();
            hmp.map = source.readHashMap(Map.class.getClassLoader());
            return hmp;
        }
    };

    public void setParcelMap(Map<String, Object> map) {
        this.map = map;
    }

    public Map<String, Object> getParcelMap() {
        return map;
    }
}

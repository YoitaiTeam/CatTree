package com.yoitai.cattree;

import android.content.Context;
import android.content.SharedPreferences;

public class CatTreeData {

    private static String TABLE = "CatTree";

    static SharedPreferences mData;

    public static String POINT = "CatTreePoint";
    public static String POURED_TIME = "CatTreePouredTime";
    static String INSTALL_DATE;
    static String PLAY_TIME;
    static String LAST_VISIT;

    static void init(Context _context) {
        mData = _context.getSharedPreferences(TABLE, Context.MODE_PRIVATE);
    }

    static String get(String _key, String _default) {
        return mData.getString(_key, _default);
    }

    static void set(String _key, String _value) {
        mData.edit()
                .putString(_key, _value)
                .apply();
    }

    public static int getInt(String _key, int _default) {
        return mData.getInt(_key, _default);
    }

    public static void setInt(String _key, int _value) {
        mData.edit()
                .putInt(_key, _value)
                .apply();
    }

    public static long getLong(String _key, long _default) {
        return mData.getLong(_key, _default);
    }

    public static void setLong(String _key, long _value) {
        mData.edit()
                .putLong(_key, _value)
                .apply();
    }
}

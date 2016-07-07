package com.thesis.sms;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Utils {

	private Context context;
	private SharedPreferences sharedPref;

	private static final String KEY_SHARED_PREF = "ANDROID_WEB_CHAT";
	private static final int KEY_MODE_PRIVATE = 0;

	public Utils(Context context) {
		this.context = context;
		sharedPref = this.context.getSharedPreferences(KEY_SHARED_PREF,
				KEY_MODE_PRIVATE);
	}
}

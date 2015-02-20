package com.android.volley.toolbox;

import com.android.volley.*;

import java.io.*;

import android.util.*;

import org.apache.http.protocol.*;

public class StringUTF8Request extends StringRequest {
    private static final String LOG_TAG = StringUTF8Request.class.getSimpleName();

    protected static final String TYPE_UTF8_CHARSET = "charset=UTF-8";

    public StringUTF8Request(String url,
                             Response.Listener<String> listener,
                             Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
    }

    @Override
    protected Response<String> parseNetworkResponse(
            NetworkResponse response) {
        try {
            String type = response.headers.get(HTTP.CONTENT_TYPE);
            if (type == null) {
                Log.d(LOG_TAG, "content type was null");
                type = TYPE_UTF8_CHARSET;
                response.headers.put(HTTP.CONTENT_TYPE, type);
            } else if (!type.contains("UTF-8")) {
                Log.d(LOG_TAG, "content type had UTF-8 missing");
                type += ";" + TYPE_UTF8_CHARSET;
                response.headers.put(HTTP.CONTENT_TYPE, type);
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getLocalizedMessage());
        }
        return super.parseNetworkResponse(response);
    }

}

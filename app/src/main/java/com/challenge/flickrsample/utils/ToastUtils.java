package com.challenge.flickrsample.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Handles toast messages
 */

public class ToastUtils {

    /**
     * Displays short toast
     *
     * @param context context
     * @param message toast message
     */
    public static void showShortToast(Context context, String message) {
        if (context != null) {
            Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
        }
    }

}

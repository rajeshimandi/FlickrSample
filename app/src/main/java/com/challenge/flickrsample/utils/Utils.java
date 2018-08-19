package com.challenge.flickrsample.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.SimpleCursorTreeAdapter;

/**
 * Class to handle URL creations
 */

public class Utils {

    /**
     * Hides soft keypad
     *
     * @param context Activity context
     * @param view    view
     */
    public static void hideKeyboard(Context context, View view) {
        if (context != null && null != view) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * Returns Flickr API URL to get list of images based on keyword and page number
     *
     * @param pageCount current page
     * @param keyword   keyword entered
     * @return URL string
     */
    public static String getImageListURL(int pageCount, String keyword) {
        StringBuilder url = new StringBuilder(Constants.URL);
        url.append(Constants.PAGE)
                .append(Constants.EQUALS)
                .append(pageCount)
                .append(Constants.AMP)
                .append(Constants.TEXT)
                .append(Constants.EQUALS)
                .append(keyword);

        return url.toString();
    }

    /**
     * Returns image URL
     *
     * @param farm   farm
     * @param server server
     * @param id     id
     * @param secret secret
     * @return image url string
     */
    public static String getImageURL(int farm, String server, String id, String secret) {
        StringBuilder imageUrl = new StringBuilder(Constants.IMAGE_URL_ROOT);
        imageUrl.append(farm)
                .append(Constants.IMAGE_URL_DOMAIN)
                .append(server)
                .append(Constants.SLASH)
                .append(id)
                .append(Constants.UNDERSCORE)
                .append(secret)
                .append(Constants.JPG_EXT);
        return imageUrl.toString();
    }

}

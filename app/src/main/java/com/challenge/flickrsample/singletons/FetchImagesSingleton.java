package com.challenge.flickrsample.singletons;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.challenge.flickrsample.netwok.FetchResponseAsync;
import com.challenge.flickrsample.netwok.ResponseListener;
import com.challenge.flickrsample.pojos.FlickrResponsePojo;
import com.challenge.flickrsample.utils.Constants;
import com.challenge.flickrsample.utils.NetworkUtils;
import com.challenge.flickrsample.utils.ToastUtils;
import com.challenge.flickrsample.utils.Utils;

/**
 * Singleton to invoke Async and handles success/failure
 */

public class FetchImagesSingleton implements ResponseListener {

    public interface APICallbackListener {

        void showOrHideLoader(boolean show);

        void onSuccess(Object object);

        void onFailure(Object object);
    }

    @Nullable
    private APICallbackListener mCallBackListener;

    private static final FetchImagesSingleton fetchImagesInstance = new FetchImagesSingleton();

    public static FetchImagesSingleton getInstance() {
        return fetchImagesInstance;
    }

    private FetchImagesSingleton() {
    }

    public void setCallBackListener(APICallbackListener callBackListener) {
        this.mCallBackListener = callBackListener;
    }

    /**
     * Initiates API call to fetch imaged details
     *
     * @param context   context
     * @param keyword   keyword
     * @param pageCount current page no
     */
    public void loadImages(Context context, String keyword, int pageCount) {
        if (NetworkUtils.isNetworkAvailable(context)) {

            FetchResponseAsync imagesAsync = new FetchResponseAsync(this);
            imagesAsync.execute(Utils.getImageListURL(pageCount, keyword));

        } else {
            ToastUtils.showShortToast(context, Constants.INTERNET_ERROR);
        }
    }

    @Override
    public void onRequestStart() {
        mCallBackListener.showOrHideLoader(true);
    }

    @Override
    public void onResponse(Object response) {
        if (null != response && response instanceof FlickrResponsePojo) {

            FlickrResponsePojo responsePojo = (FlickrResponsePojo) response;
            if (responsePojo.getStat().equals("ok")) {
                mCallBackListener.onSuccess(responsePojo);
            } else {
                if (!TextUtils.isEmpty(responsePojo.getMessage())) {
                    mCallBackListener.onFailure(responsePojo.getMessage());
                } else {
                    mCallBackListener.onFailure(Constants.ERROR);
                }
            }

        } else {
            mCallBackListener.onFailure(Constants.ERROR);
        }

        mCallBackListener.showOrHideLoader(false);
    }

    @Override
    public void onError(Object error) {
        mCallBackListener.showOrHideLoader(false);
        mCallBackListener.onFailure(error);
    }

}

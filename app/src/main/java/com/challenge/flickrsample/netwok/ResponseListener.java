package com.challenge.flickrsample.netwok;


/**
 * Interface for handling web service callback requests.
 */
public interface ResponseListener {

    /**
     * Callback fired soon after the web service call
     */
    public void onRequestStart();

    /**
     * Callback fired after getting the response from web service call
     *
     * @param response - Json response from server
     */
    public void onResponse(Object response);

    /**
     * Callback fired if there is any error in web service call
     *
     * @param error - An error message
     */
    public void onError(Object error);

}

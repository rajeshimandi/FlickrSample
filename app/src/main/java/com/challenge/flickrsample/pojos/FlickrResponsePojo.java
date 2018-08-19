package com.challenge.flickrsample.pojos;


/**
 * Flickr response pojo
 */

public class FlickrResponsePojo {

    private Photos photos;

    private String stat;

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Photos getPhotos() {
        return photos;
    }

    public void setPhotos(Photos photos) {
        this.photos = photos;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }
}

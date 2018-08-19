package com.challenge.flickrsample.utils;

import com.challenge.flickrsample.pojos.FlickrResponsePojo;
import com.challenge.flickrsample.pojos.PhotoItem;
import com.challenge.flickrsample.pojos.Photos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Parses the response
 */

public class JsonParser {

    public static Object parseJson(String response) throws JSONException {

        FlickrResponsePojo flickrResponsePojo = new FlickrResponsePojo();
        Photos photos = new Photos();

        JSONObject rootObject = new JSONObject(response);

        if (null != rootObject) {
            flickrResponsePojo.setStat(rootObject.optString("stat"));
            flickrResponsePojo.setMessage(rootObject.optString("message"));
        }

        JSONObject photosObject = rootObject.optJSONObject("photos");
        JSONArray photosArray = null;

        if (null != photosObject) {
            photos.setPage(photosObject.optInt("page", 1));
            photos.setPages(photosObject.optInt("pages"));
            photos.setPerpage(photosObject.optInt("perpage"));
            photos.setTotal(photosObject.optString("total"));
            photosArray = photosObject.optJSONArray("photo");
        }

        if (null != photosArray) {
            int arrayLength = photosArray.length();

            ArrayList<PhotoItem> photosList = new ArrayList<>();

            for (int i = 0; i < arrayLength; i++) {
                JSONObject photoObject = photosArray.optJSONObject(i);
                PhotoItem photoItem = new PhotoItem();
                photoItem.setFarm(photoObject.optInt("farm"));
                photoItem.setId(photoObject.optString("id"));
                photoItem.setIsfamily(photoObject.optInt("isfamily"));
                photoItem.setIsfriend(photoObject.optInt("isfriend"));
                photoItem.setIspublic(photoObject.optInt("ispublic"));
                photoItem.setTitle(photoObject.optString("title"));
                photoItem.setOwner(photoObject.optString("owner"));
                photoItem.setServer(photoObject.optString("server"));
                photoItem.setSecret(photoObject.optString("secret"));
                photosList.add(photoItem);
            }

            photos.setPhoto(photosList);
        }

        flickrResponsePojo.setPhotos(photos);

        return flickrResponsePojo;
    }

}

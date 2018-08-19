package com.challenge.flickrsample.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.challenge.flickrsample.R;
import com.challenge.flickrsample.pojos.PhotoItem;
import com.challenge.flickrsample.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Adapter class to load images
 */

public class FlickrAdapter extends RecyclerView.Adapter<FlickrAdapter.ImageViewHolder> {

    private ArrayList<PhotoItem> mPhotosList;

    public FlickrAdapter(ArrayList<PhotoItem> photosList) {
        this.mPhotosList = photosList;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_image_view, parent, false);

        return new ImageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {

        PhotoItem photoItem = mPhotosList.get(position);

        String imageUrl = Utils.getImageURL(photoItem.getFarm(), photoItem.getServer(), photoItem.getId(), photoItem.getSecret());

        //new FetchImageAsync(holder.imageView).execute(imageUrl.toString());

        Picasso.get().load(imageUrl)
                .placeholder(R.drawable.loader)
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return mPhotosList != null ? mPhotosList.size() : 0;
    }

    /**
     * View Holder class
     */
    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

}

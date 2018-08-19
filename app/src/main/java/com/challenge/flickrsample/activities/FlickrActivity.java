package com.challenge.flickrsample.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.challenge.flickrsample.R;
import com.challenge.flickrsample.adapters.FlickrAdapter;
import com.challenge.flickrsample.netwok.FetchResponseAsync;
import com.challenge.flickrsample.netwok.ResponseListener;
import com.challenge.flickrsample.pojos.FlickrResponsePojo;
import com.challenge.flickrsample.pojos.PhotoItem;
import com.challenge.flickrsample.utils.Constants;
import com.challenge.flickrsample.utils.NetworkUtils;
import com.challenge.flickrsample.widgets.CustomRecyclerView;

import java.util.ArrayList;

public class FlickrActivity extends AppCompatActivity implements ResponseListener {

    private int pageCount = 1;
    private ProgressBar mProgressBar;
    private CustomRecyclerView mRecyclerView;
    private ArrayList<PhotoItem> mPhotosList;
    private FlickrAdapter mFlickrAdapter;
    private int mTotalPages;
    private long mBackPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flickr);

        mRecyclerView = findViewById(R.id.recyclerView);
        TextView emptyView = findViewById(R.id.emptyView);
        final EditText search = findViewById(R.id.search);
        mProgressBar = findViewById(R.id.progressbar);

        final GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setEmptyView(emptyView);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                    if (layoutManager.findLastVisibleItemPosition() >= layoutManager.getItemCount() - 1) {
                        pageCount++;
                        if (pageCount < mTotalPages) {
                            loadImages(search.getText().toString().trim());
                        }
                    }
                }
            }

        });


        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String keyword = search.getText().toString().trim();
                    if (!TextUtils.isEmpty(keyword)) {
                        if (null != mPhotosList) {
                            mPhotosList.clear();
                        }
                        loadImages(keyword);
                    } else {
                        Toast.makeText(FlickrActivity.this, Constants.VALID_KEYWORD, Toast.LENGTH_SHORT).show();
                    }
                    hideKeyboard(FlickrActivity.this, search);
                    return true;
                }
                return false;
            }
        });


    }

    /**
     * Fetches images and sets to recycler view
     *
     * @param keyword - keyword entered
     */
    private void loadImages(String keyword) {

        if (NetworkUtils.isNetworkAvailable(FlickrActivity.this)) {

            StringBuilder url = new StringBuilder(Constants.URL);
            url.append(Constants.PAGE)
                    .append(Constants.EQUALS)
                    .append(pageCount)
                    .append(Constants.AMP)
                    .append(Constants.TEXT)
                    .append(Constants.EQUALS)
                    .append(keyword);

            FetchResponseAsync imagesAsync = new FetchResponseAsync(this, FlickrResponsePojo.class);
            imagesAsync.execute(url.toString());

        } else {
            Toast.makeText(FlickrActivity.this, Constants.INTERNET_ERROR, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRequestStart() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResponse(Object response) {

        if (null != response && response instanceof FlickrResponsePojo) {

            FlickrResponsePojo responsePojo = (FlickrResponsePojo) response;
            if (responsePojo.getStat().equals("ok")) {
                mTotalPages = responsePojo.getPhotos().getPages();
                populateAdapter(responsePojo);
            } else {
                if (!TextUtils.isEmpty(responsePojo.getMessage())) {
                    Toast.makeText(FlickrActivity.this, responsePojo.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(FlickrActivity.this, Constants.ERROR, Toast.LENGTH_SHORT).show();
                }
            }

        } else {
            Toast.makeText(FlickrActivity.this, Constants.ERROR, Toast.LENGTH_SHORT).show();
        }

        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onError(Object error) {
        mProgressBar.setVisibility(View.GONE);
        Toast.makeText(FlickrActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
    }

    /**
     * Sets adapter for recycler view
     *
     * @param responsePojo - response object
     */
    private void populateAdapter(FlickrResponsePojo responsePojo) {

        if (mPhotosList == null) {
            mPhotosList = new ArrayList<>();
        }
        mPhotosList.addAll(responsePojo.getPhotos().getPhoto());

        if (mFlickrAdapter == null) {
            mFlickrAdapter = new FlickrAdapter(FlickrActivity.this, mPhotosList);
            mRecyclerView.setAdapter(mFlickrAdapter);
        } else {
            mFlickrAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Hides soft keypad
     *
     * @param context Activity context
     * @param view    view
     */
    private void hideKeyboard(Context context, View view) {
        if (null != view) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * Prevents accidental back press
     */
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() < mBackPressedTime + 2000) {
            finish();
        } else {
            Toast.makeText(FlickrActivity.this, Constants.BACK_MSG, Toast.LENGTH_SHORT).show();
            mBackPressedTime = System.currentTimeMillis();
        }
    }


}

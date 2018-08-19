package com.challenge.flickrsample.activities;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.challenge.flickrsample.R;
import com.challenge.flickrsample.adapters.FlickrAdapter;
import com.challenge.flickrsample.netwok.FetchResponseAsync;
import com.challenge.flickrsample.netwok.ResponseListener;
import com.challenge.flickrsample.pojos.FlickrResponsePojo;
import com.challenge.flickrsample.pojos.PhotoItem;
import com.challenge.flickrsample.utils.Constants;
import com.challenge.flickrsample.utils.NetworkUtils;
import com.challenge.flickrsample.utils.ToastUtils;
import com.challenge.flickrsample.utils.Utils;
import com.challenge.flickrsample.widgets.CustomRecyclerView;

import java.util.ArrayList;

/**
 * Activity that displays images based on user search. Handles endless scroll
 */
public class FlickrActivity extends AppCompatActivity implements ResponseListener {

    private int pageCount = 1; // maintains current page number
    private ProgressBar mProgressBar;
    private CustomRecyclerView mRecyclerView;
    private EditText mSearch;
    private TextView mEmptyView;

    @Nullable
    private ArrayList<PhotoItem> mPhotosList; // maintains photo details retrieved from API
    @Nullable
    private FlickrAdapter mFlickrAdapter;
    private int mTotalPages; // maintains max page count returned from API
    private long mBackPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flickr);

        initViews();

        final GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setEmptyView(mEmptyView);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                    if (layoutManager.findLastVisibleItemPosition() >= layoutManager.getItemCount() - 1) {
                        pageCount++;
                        if (pageCount < mTotalPages) {
                            loadImages(mSearch.getText().toString().trim());
                        }
                    }
                }
            }

        });


        mSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String keyword = mSearch.getText().toString().trim();
                    if (!TextUtils.isEmpty(keyword)) {
                        if (null != mPhotosList) {
                            mPhotosList.clear();
                        }
                        loadImages(keyword);
                    } else {
                        ToastUtils.showShortToast(FlickrActivity.this, Constants.VALID_KEYWORD);
                    }
                    Utils.hideKeyboard(FlickrActivity.this, mSearch);
                    return true;
                }
                return false;
            }
        });


    }

    /**
     * Initializes views
     */
    private void initViews() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mEmptyView = findViewById(R.id.emptyView);
        mSearch = findViewById(R.id.search);
        mProgressBar = findViewById(R.id.progressbar);
    }

    /**
     * Fetches images and sets to recycler view
     *
     * @param keyword - keyword entered
     */
    private void loadImages(String keyword) {

        if (NetworkUtils.isNetworkAvailable(FlickrActivity.this)) {

            FetchResponseAsync imagesAsync = new FetchResponseAsync(this, FlickrResponsePojo.class);
            imagesAsync.execute(Utils.getImageListURL(pageCount, keyword));

        } else {
            ToastUtils.showShortToast(FlickrActivity.this, Constants.INTERNET_ERROR);
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
                    ToastUtils.showShortToast(FlickrActivity.this, responsePojo.getMessage());
                } else {
                    ToastUtils.showShortToast(FlickrActivity.this, Constants.ERROR);
                }
            }

        } else {
            ToastUtils.showShortToast(FlickrActivity.this, Constants.ERROR);
        }

        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onError(Object error) {
        mProgressBar.setVisibility(View.GONE);
        ToastUtils.showShortToast(FlickrActivity.this, error.toString());
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
            mFlickrAdapter = new FlickrAdapter(mPhotosList);
            mRecyclerView.setAdapter(mFlickrAdapter);
        } else {
            mFlickrAdapter.notifyDataSetChanged();
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
            ToastUtils.showShortToast(FlickrActivity.this, Constants.BACK_MSG);
            mBackPressedTime = System.currentTimeMillis();
        }
    }


}

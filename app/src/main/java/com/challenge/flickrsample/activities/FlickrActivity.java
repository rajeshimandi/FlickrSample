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
import com.challenge.flickrsample.pojos.FlickrResponsePojo;
import com.challenge.flickrsample.pojos.PhotoItem;
import com.challenge.flickrsample.singletons.FetchImagesSingleton;
import com.challenge.flickrsample.utils.Constants;
import com.challenge.flickrsample.utils.ToastUtils;
import com.challenge.flickrsample.utils.Utils;

import java.util.ArrayList;

/**
 * Activity that displays images based on user search. Handles endless scroll
 */
public class FlickrActivity extends AppCompatActivity implements /*ResponseListener,*/ FetchImagesSingleton.APICallbackListener {

    private int pageCount = 1; // maintains current page number
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
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

        init();

        final GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                    if (layoutManager.findLastVisibleItemPosition() >= layoutManager.getItemCount() - 1) {
                        pageCount++;
                        if (pageCount < mTotalPages) {
                            FetchImagesSingleton.getInstance().loadImages(FlickrActivity.this, mSearch.getText().toString().trim(), pageCount);
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
                        FetchImagesSingleton.getInstance().loadImages(FlickrActivity.this, mSearch.getText().toString().trim(), pageCount);
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
    private void init() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mEmptyView = findViewById(R.id.emptyView);
        mSearch = findViewById(R.id.search);
        mProgressBar = findViewById(R.id.progressbar);
        FetchImagesSingleton.getInstance().setCallBackListener(this);
    }

    /**
     * Handles loader visibility
     *
     * @param show - true - show, false - hide
     */
    @Override
    public void showOrHideLoader(boolean show) {
        mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    /**
     * Populates the list post retrieving the results
     *
     * @param object - response object
     */
    @Override
    public void onSuccess(Object object) {
        FlickrResponsePojo responsePojo = (FlickrResponsePojo) object;
        mTotalPages = responsePojo.getPhotos().getPages();
        populateAdapter(responsePojo);
        if (mRecyclerView.getAdapter().getItemCount() > 0) {
            mEmptyView.setVisibility(View.GONE);
        }
    }

    /**
     * Handles failure cases
     *
     * @param object message object
     */
    @Override
    public void onFailure(Object object) {
        if (mRecyclerView.getAdapter().getItemCount() == 0) {
            mEmptyView.setVisibility(View.VISIBLE);
        }
        ToastUtils.showShortToast(FlickrActivity.this, object.toString());
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

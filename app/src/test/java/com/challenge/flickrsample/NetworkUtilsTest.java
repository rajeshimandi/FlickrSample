package com.challenge.flickrsample;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.challenge.flickrsample.utils.NetworkUtils;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Class to test Network connectivity
 */

public class NetworkUtilsTest {
    @Mock
    Context context;
    @Mock
    ConnectivityManager connectivityManager;
    @Mock
    NetworkInfo networkInfo;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void networkIsNull() {
        when(connectivityManager.getActiveNetworkInfo()).thenReturn(null);
        when(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager);

        assertFalse(NetworkUtils.isNetworkAvailable(context));
    }

    @Test
    public void networkIsFalse() {
        when(networkInfo.isConnectedOrConnecting()).thenReturn(false);
        when(connectivityManager.getActiveNetworkInfo()).thenReturn(networkInfo);
        when(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager);

        assertFalse(NetworkUtils.isNetworkAvailable(context));
    }

    @Test
    public void networkIsTrue() {
        when(networkInfo.isConnectedOrConnecting()).thenReturn(true);
        when(connectivityManager.getActiveNetworkInfo()).thenReturn(networkInfo);
        when(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager);

        assertTrue(NetworkUtils.isNetworkAvailable(context));
    }
}

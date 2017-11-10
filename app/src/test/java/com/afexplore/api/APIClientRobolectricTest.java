package com.afexplore.api;

import com.afexplore.BuildConfig;
import com.afexplore.constants.Constants;
import com.afexplore.home.HomeActivity;
import com.afexplore.home.api.APIClient;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import retrofit2.Retrofit;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class APIClientRobolectricTest {

    // The Activity which holds the view hierarchy
    private HomeActivity homeActivity;

    // Class APIClient will be tested
    private APIClient apiClient;


    @Before
    public void setUp() throws Exception {

        // Initialize HomeActivity
        homeActivity = Robolectric.buildActivity(HomeActivity.class).create().get();
    }

    /**
     * Tests if APIClient is able to create a valid Retrofit instance
     * */
    @Test
    public void APIClient_shouldReturnRetrofitObjectTest() throws Exception {

        Retrofit retrofit = apiClient.getClient(homeActivity, Constants.BASE_URL);

        // Assert that retrofit is not null
        assertNotNull(retrofit);

        // Assert that the retrofit.baseUrl() is equal to Constants.BASE_URL
        assertEquals(Constants.BASE_URL, retrofit.baseUrl().toString());
    }

    /**
     * Tests if APIClient returns null when any base URL is given
     * */
    @Test
    public void APIClient_shouldReturnNullRetrofitObjectTest1() throws Exception {

        Retrofit retrofit = apiClient.getClient(homeActivity, "whatever");

        // Assert that retrofit is null
        assertNull(retrofit);
    }

    /**
     * Tests if APIClient returns null when a null base URL is given
     * */
    @Test
    public void APIClient_shouldReturnNullRetrofitObjectTest2() throws Exception {

        Retrofit retrofit = apiClient.getClient(homeActivity, null);

        // Assert that retrofit is null
        assertNull(retrofit);
    }
}

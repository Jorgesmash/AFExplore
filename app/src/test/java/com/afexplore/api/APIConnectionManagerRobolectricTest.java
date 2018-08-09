package com.afexplore.api;

import com.afexplore.BuildConfig;
import com.afexplore.R;
import com.afexplore.constants.Constants;
import com.afexplore.home.HomeActivity;
import com.afexplore.home.api.APIConnectionManager;
import com.afexplore.home.datamodels.ExploreItemDataModel;
import com.afexplore.util.json.JSONReader;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLooper;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

/**
 * Performs an non instrumented test of JSONReader to validate that it is able to return valid JSON
 * objects and that is well code-protected.
 * */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class APIConnectionManagerRobolectricTest {

    // The Activity which holds the view hierarchy
    private HomeActivity homeActivity;

    // Class APIConnectionManager will be tested
    private APIConnectionManager apiConnectionManager;

    @Before
    public void setUp() {

        // Initialize HomeActivity
        homeActivity = Robolectric.buildActivity(HomeActivity.class).create().get();

        // Initialize APIConnectionManager
        apiConnectionManager = new APIConnectionManager(homeActivity);
        apiConnectionManager.setOnConnectionResultListener(new ExploreAPIManagerOnConnectionResultListener());
    }

    /**
     * Tests a real API connection to the Explore endpoint.
     * NOTE: This might be returning a timeout since there is some issues on the API server.
     * */
    @Test
    public void APIConnectionManager_shouldRetrieveExploreInformationTest1() throws Exception {

        final AtomicBoolean callbackCalled = new AtomicBoolean(false);

        // Connect to API endpoint to retrieve the information
        apiConnectionManager.connectToExploreEndpoint(callbackCalled, Constants.BASE_URL);

        // This loop will keep the UIThread alive while the response callback returns
        while (!callbackCalled.get()) {
            Thread.sleep(1000);
            ShadowLooper.runUiThreadTasks();
        }
    }

    /**
     * Tests a mock API connection to the Explore endpoint.
     *
     * This will return a successful response
     * */
    @Test
    public void APIConnectionManager_shouldRetrieveExploreInformationTest2() throws Exception {

        final AtomicBoolean callbackCalled = new AtomicBoolean(false);

        String jsonString = JSONReader.loadJSONStringFromAsset(homeActivity, Constants.EXPLORE_JSON_FILENAME);

        // Initialize MockWebServer
        MockWebServer mockWebServer = new MockWebServer();
        mockWebServer.start();
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(jsonString));

        // Ask the server for its URL to make mock HTTP requests.
        String urlString = mockWebServer.url("/").toString();

        // Connect to API endpoint to retrieve the information
        apiConnectionManager.connectToExploreEndpoint(callbackCalled, urlString);

        // This loop will keep the UIThread alive while the response callback returns
        while (!callbackCalled.get()) {
            Thread.sleep(1000);
            ShadowLooper.runUiThreadTasks();
        }
    }

    /**
     * Called when the API has finished retrieving the Explore JSON response.
     *
     * IMPORTANT NOTE: It is possible to receive a timeout, since the API server is having issues
     */
    private class ExploreAPIManagerOnConnectionResultListener implements APIConnectionManager.OnConnectionResultListener {

        @Override
        public void onConnectionResult(String statusCode, List<ExploreItemDataModel> exploreItemDataModelList) {

            System.out.println("API Call Test Successful with result:");

            if (statusCode.equals(APIConnectionManager.NETWORK_ERROR)) {
                System.out.println(homeActivity.getString(R.string.no_network_dialog_fragment_title));
                System.out.println(homeActivity.getString(R.string.no_network_dialog_fragment_message));

                // Assert that exploreItemDataModelList is null
                assertNull(exploreItemDataModelList);

            } else if (statusCode.equals(APIConnectionManager.RESULT_TIMEOUT)) {
                System.out.println(homeActivity.getString(R.string.result_timeout_dialog_fragment_title));
                System.out.println(homeActivity.getString(R.string.result_timeout_dialog_fragment_message));

                // Assert that exploreItemDataModelList is null
                assertNull(exploreItemDataModelList);

            } else if (statusCode.equals(APIConnectionManager.RESULT_ACCESSDENIED)) {
                System.out.println(homeActivity.getString(R.string.result_accessdenied_dialog_fragment_title));
                System.out.println(homeActivity.getString(R.string.result_accessdenied_dialog_fragment_message));

                // Assert that exploreItemDataModelList is null
                assertNull(exploreItemDataModelList);

            } else if (statusCode.equals(APIConnectionManager.RESULT_UNKNOWN)) {
                System.out.println(homeActivity.getString(R.string.result_error_unknown_fragment_title));
                System.out.println(homeActivity.getString(R.string.result_unknown_dialog_fragment_message));

                // Assert that exploreItemDataModelList is null
                assertNull(exploreItemDataModelList);

            } else if (statusCode.equals(APIConnectionManager.RESULT_NOTFOUND)) {
                System.out.println(homeActivity.getString(R.string.result_notfound_dialog_fragment_title));
                System.out.println(homeActivity.getString(R.string.result_notfound_dialog_fragment_message));

                // Assert that exploreItemDataModelList is null
                assertNull(exploreItemDataModelList);

            } else if (statusCode.equals(APIConnectionManager.RESULT_OK)) {
                System.out.println(homeActivity.getString(R.string.result_successful_dialog_fragment_title));
                System.out.println(homeActivity.getString(R.string.result_successful_dialog_fragment_message));

                // converts ExploreItemDataModel to JSON string
                Gson gson = new Gson();
                String jsonString = gson.toJson(exploreItemDataModelList);
                System.out.println("JSON string:");
                System.out.println(jsonString);

                // Assert that exploreItemDataModelList is not null
                assertNotNull(exploreItemDataModelList);

                // Assert that list items are instances of ExploreItemDataModel
                assertThat(exploreItemDataModelList.get(0), instanceOf(ExploreItemDataModel.class));
            }
        }
    }
}

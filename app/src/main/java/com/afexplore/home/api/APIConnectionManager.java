package com.afexplore.home.api;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.afexplore.constants.Constants;
import com.afexplore.home.datamodels.ExploreItemDataModel;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.net.ssl.SSLHandshakeException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Manages the connection to the forecast API.
 *
 * It makes some necessary tasks previous connecting to the API, like verify that the mobile has
 * network connection. Also receives the API call response and process it by taking the necessary
 * information to fill the data model.
 * */
public class APIConnectionManager {

    private Context context;

    // Network status
    public static final String NETWORK_ERROR = "NETWORK_ERROR";

    // Response status
    public static final String RESULT_OK = "RESULT_OK";
    public static final String RESULT_ACCESSDENIED = "RESULT_ACCESSDENIED";
    public static final String RESULT_NOTFOUND = "RESULT_NOTFOUND";
    public static final String RESULT_TIMEOUT = "RESULT_TIMEOUT";
    public static final String RESULT_UNKNOWN = "RESULT_UNKNOWN";

    /**  Listener to inform that a result from the API connection has been gotten */
    private OnConnectionResultListener onConnectionResultListener;
    public interface OnConnectionResultListener {
        void onConnectionResult(String statusCode, List<ExploreItemDataModel> exploreItemDataModelList);
    }
    public void setOnConnectionResultListener(OnConnectionResultListener onConnectionResultListener) {
        this.onConnectionResultListener = onConnectionResultListener;
    }

    /** Constructor */
    public APIConnectionManager(Context context) {
        this.context = context;
    }

    /**
     * Explore connection endpoint:
     *
     * Calls the 'explore' endpoint which returns the current featured information for the
     * Explore section.
     *
     * Example: https://www.abercrombie.com/anf/nativeapp/qa/codetest/codeTest_exploreData.json
     */
    public void connectToExploreEndpoint(AtomicBoolean callbackCalled, String baseURL) {

        if (isNetworkConnected()) {
            executeExploreAPICall(callbackCalled, baseURL);

        } else {
            onConnectionResultListener.onConnectionResult(NETWORK_ERROR, null);
        }
    }

    /**
     * Creates a new instance of the async task and executes it.
     * It is called for each connection to the RESTful API endpoint.
     **/
    private void executeExploreAPICall(AtomicBoolean callbackCalled, String baseURL) {

        APIInterface apiInterface = APIClient.getClient(context, baseURL).create(APIInterface.class);
        Call call = apiInterface.getExploreInformation();
        call.enqueue(new ExploreAPIConnectionCallback(callbackCalled));
    }

    /**
     * Validates if the device is connected to network.
     * */
    private boolean isNetworkConnected() {

        // Check if a network connection is available
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }

        return false;
    }

    /**
     *  Receives the result of the asynchronous task to the API.
     *
     *  Connect to an endpoint which returns a response with the featured information for the
     *  Explore section.
     *  */
    private class ExploreAPIConnectionCallback implements Callback {

        private AtomicBoolean callbackCalled;

        public ExploreAPIConnectionCallback(AtomicBoolean callbackCalled) {
            this.callbackCalled = callbackCalled;
        }

        @Override
        public void onResponse(Call call, Response response) {

            callbackCalled.set(true);

            List<ExploreItemDataModel> exploreItemDataModelList = (ArrayList<ExploreItemDataModel>) response.body();

            if (response.raw().code() == 200) {
                onConnectionResultListener.onConnectionResult(RESULT_OK, exploreItemDataModelList);

            } else if (response.raw().code() == 403) {
                onConnectionResultListener.onConnectionResult(RESULT_ACCESSDENIED, null);

            } else {
                onConnectionResultListener.onConnectionResult(RESULT_NOTFOUND, null);
            }
        }

        @Override
        public void onFailure(Call call, Throwable t) {

            callbackCalled.set(true);

            if (t instanceof SocketTimeoutException) {
                onConnectionResultListener.onConnectionResult(RESULT_TIMEOUT, null);

            } else if (t instanceof SSLHandshakeException) {
                onConnectionResultListener.onConnectionResult(RESULT_UNKNOWN, null);

            } else {
                onConnectionResultListener.onConnectionResult(RESULT_UNKNOWN, null);
            }
        }
    }
}

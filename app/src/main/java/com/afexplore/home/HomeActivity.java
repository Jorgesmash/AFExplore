package com.afexplore.home;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.afexplore.R;
import com.afexplore.constants.Constants;
import com.afexplore.dialog.ApplicationDialogFragment;
import com.afexplore.home.api.APIConnectionManager;
import com.afexplore.home.datamodels.ExploreItemDataModel;
import com.afexplore.util.json.JSONReader;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Entry activity of the Explore application.
 *
 * Presents a RecyclerView by showing different CardViews with information to explore about promos
 * */
public class HomeActivity extends AppCompatActivity {

    /* Widgets */
    private Toolbar toolbar;

    private RecyclerView exploreRecyclerView;

    // Flag to know if the Explore information has been retrieved so it won't be try to retrieve it again
    private boolean hasRetrievedExploreInfo;

    // Flags to know if a Chrome custom tab is showing
    private boolean willShowChromeCustomTabs;
    private boolean showingChromeCustomTabs;
    private boolean hasShownChromeCustomTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        /* Get the components of the content layout */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        exploreRecyclerView = (RecyclerView) findViewById(R.id.exploreRecyclerView);
        exploreRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        // The Explore information will be retrieved only once
        if (!hasRetrievedExploreInfo) {

            // Call the API to retrieve the current featured information to explore
            retrieveExploreInformation();
        }

        // Set flags to know if a Chrome custom tab has shown
        if (showingChromeCustomTabs) {
            hasShownChromeCustomTabs = true;
            willShowChromeCustomTabs = false;
            showingChromeCustomTabs = false;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Set flags to know if a Chrome custom tab is showing
        if (willShowChromeCustomTabs) {
            showingChromeCustomTabs = true;
        }
    }

    /* Getters and Setters */
    public void setWillShowChromeCustomTabs(boolean willShowChromeCustomTabs) {
        this.willShowChromeCustomTabs = willShowChromeCustomTabs;
    }

    public void setHasShownChromeCustomTabs(boolean hasShownChromeCustomTabs) {
        this.hasShownChromeCustomTabs = hasShownChromeCustomTabs;
    }

    public boolean isHasShownChromeCustomTabs() {
        return hasShownChromeCustomTabs;
    }

    /**
     * This method creates a new API connection to retrieve the information for the Explore section.
     * Also sets OnConnectionResultListener which will be called when the result of the API
     * has been retrieved.
     * */
    private void retrieveExploreInformation() {

        // The information for the Explore section may be taken from the assets folder o from
        // the API endpoint, since this one is having functionality issues
        if (Constants.SIMULATE_EXPLORE_API_CONNECTION) {

            // Read JSON from file in assets folder and populate the Explore section
            List<ExploreItemDataModel> exploreItemDataModelList = JSONReader.newInstance(this).loadJSONObjectFromAsset(Constants.EXPLORE_JSON_FILENAME, new TypeToken<List<ExploreItemDataModel>>(){}.getType());
            populateExploreSection(exploreItemDataModelList);

        } else {

            final AtomicBoolean callbackCalled = new AtomicBoolean(false);

            // Connect to API endpoint to retrieve the information
            APIConnectionManager apiConnectionManager = new APIConnectionManager(this);
            apiConnectionManager.setOnConnectionResultListener(new ExploreAPIManagerOnConnectionResultListener());
            apiConnectionManager.connectToExploreEndpoint(callbackCalled, Constants.BASE_URL);
        }
    }

    /**
     * Listens if the API has finished retrieving the featured information.
     * If there is any error, this will be reported to user in a Dialog.
     * If a successful result is received, the Explore section will be populated.
     */
    private class ExploreAPIManagerOnConnectionResultListener implements APIConnectionManager.OnConnectionResultListener {

        @Override
        public void onConnectionResult(String statusCode, List<ExploreItemDataModel> exploreItemDataModelList) {

            if (statusCode.equals(APIConnectionManager.NETWORK_ERROR)) {
                showDialogFragment(getString(R.string.no_network_dialog_fragment_title), getString(R.string.no_network_dialog_fragment_message), new ApplicationDialogFragmentPositiveButtonOnClickListener());

            } else if (statusCode.equals(APIConnectionManager.RESULT_TIMEOUT)) {
                showDialogFragment(getString(R.string.result_timeout_dialog_fragment_title), getString(R.string.result_timeout_dialog_fragment_message), new ApplicationDialogFragmentPositiveButtonOnClickListener());

            } else if (statusCode.equals(APIConnectionManager.RESULT_ACCESSDENIED)) {
                showDialogFragment(getString(R.string.result_accessdenied_dialog_fragment_title), getString(R.string.result_accessdenied_dialog_fragment_message), new ApplicationDialogFragmentPositiveButtonOnClickListener());

            } else if (statusCode.equals(APIConnectionManager.RESULT_UNKNOWN)) {
                showDialogFragment(getString(R.string.result_error_unknown_fragment_title), getString(R.string.result_unknown_dialog_fragment_message), new ApplicationDialogFragmentPositiveButtonOnClickListener());

            } else if (statusCode.equals(APIConnectionManager.RESULT_NOTFOUND)) {
                showDialogFragment(getString(R.string.result_notfound_dialog_fragment_title), getString(R.string.result_notfound_dialog_fragment_message), new ApplicationDialogFragmentPositiveButtonOnClickListener());

            }else if (statusCode.equals(APIConnectionManager.RESULT_OK)) {
                populateExploreSection(exploreItemDataModelList);
            }
        }
    }

    /**
     * Set a RecyclerViewAdapter which will parse the Explore current featured information
     * and populates each CardView.
     * */
    private void populateExploreSection(List<ExploreItemDataModel> exploreItemDataModelList) {

        // The Explore information has been already retrieved. It won't try to retrieved again
        hasRetrievedExploreInfo = true;

        // Specify an Adapter
        ExploreRecyclerViewAdapter exploreRecyclerViewAdapter = new ExploreRecyclerViewAdapter(this, exploreItemDataModelList);
        exploreRecyclerView.setAdapter(exploreRecyclerViewAdapter);
    }

    /**
     * Shows a multi purpose DialogFragment.
     */
    private void showDialogFragment(String title, String message, DialogInterface.OnClickListener onClickListener) {

        ApplicationDialogFragment applicationDialogFragment = ApplicationDialogFragment.newInstance();
        applicationDialogFragment.setTitle(title);
        applicationDialogFragment.setMessage(message);
        applicationDialogFragment.setCancelable(false);
        applicationDialogFragment.setPositiveButtonOnClickListener(onClickListener);
        applicationDialogFragment.show(getFragmentManager(), "applicationDialogFragment");
    }

    /**
     * Called when user presses the OK button in the ApplicationDialogFragment.
     * It will try to connectToExploreEndpoint again.
     * */
    private class ApplicationDialogFragmentPositiveButtonOnClickListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            retrieveExploreInformation();
        }
    }
}

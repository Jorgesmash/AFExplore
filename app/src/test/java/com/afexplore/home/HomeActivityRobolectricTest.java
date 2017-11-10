package com.afexplore.home;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.widget.RecyclerView;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.afexplore.BuildConfig;
import com.afexplore.R;
import com.afexplore.home.datamodels.ExploreItemDataModel;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

//import okhttp3.mockwebserver.MockResponse;
//import okhttp3.mockwebserver.MockWebServer;
//import okhttp3.mockwebserver.RecordedRequest;

import java.util.Random;

import retrofit2.http.Url;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Performs an non instrumented test of the MainFragmentActivity flow to scroll over the
 * RecyclerView clicking the buttons, hyperlinks and performing the corresponding API calls.
 *
 * Five tests are performed, choosing randomly five ViewCards to interact with.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class HomeActivityRobolectricTest {

    // The Activity which holds the view hierarchy
    private HomeActivity homeActivity;

    // The RecyclerView which contains the Explore information
    private RecyclerView exploreRecyclerView;

    // The RecyclerView.Adapter
    private ExploreRecyclerViewAdapter exploreRecyclerViewAdapter;

    @Before
    public void setUp() throws Exception {

        // Instantiate homeActivity
        homeActivity = Robolectric.setupActivity(HomeActivity.class);

        // Get exploreRecyclerView from homeActivity
        exploreRecyclerView = homeActivity.findViewById(R.id.exploreRecyclerView);

        // Get exploreRecyclerViewAdapter from exploreRecyclerView
        exploreRecyclerViewAdapter = (ExploreRecyclerViewAdapter) exploreRecyclerView.getAdapter();
    }

    /**
     * Performs a Walkthrough over the application choosing randomly a CardView to interact with.
     */
    @Test
    public void performWalkthroughTest1() throws Exception {

        // Pick any CardView randomly
        int position = new Random().nextInt(exploreRecyclerViewAdapter.getItemCount());

        // Perform walkthrough interacting with the CardView of the picked position
        performWalkthroughTest(position);
    }

    /**
     * Performs a Walkthrough over the application choosing randomly a CardView to interact with.
     */
    @Test
    public void performWalkthroughTest2() throws Exception {

        // Pick any CardView randomly
        int position = new Random().nextInt(exploreRecyclerViewAdapter.getItemCount());

        // Perform walkthrough interacting with the CardView of the picked position
        performWalkthroughTest(position);
    }

    /**
     * Performs a Walkthrough over the application choosing randomly a CardView to interact with.
     */
    @Test
    public void performWalkthroughTest3() throws Exception {

        // Pick any CardView randomly
        int position = new Random().nextInt(exploreRecyclerViewAdapter.getItemCount());

        // Perform walkthrough interacting with the CardView of the picked position
        performWalkthroughTest(position);
    }

    /**
     * Performs a Walkthrough over the application choosing randomly a CardView to interact with.
     */
    @Test
    public void performWalkthroughTest4() throws Exception {

        // Pick any CardView randomly
        int position = new Random().nextInt(exploreRecyclerViewAdapter.getItemCount());

        // Perform walkthrough interacting with the CardView of the picked position
        performWalkthroughTest(position);
    }

    /**
     * Performs a Walkthrough over the application choosing randomly a CardView to interact with.
     */
    @Test
    public void performWalkthroughTest5() throws Exception {

        // Pick any CardView randomly
        int position = new Random().nextInt(exploreRecyclerViewAdapter.getItemCount());

        // Perform walkthrough interacting with the CardView of the picked position
        performWalkthroughTest(position);
    }

    /**
     * Performs a Walkthrough over the application with a given position.
     */
    private void performWalkthroughTest(int position) {

        System.out.println();
        System.out.println("Testing CardView number: " + position);

        // Before trying to get the viewHolder for the given position, it is necessary to scroll the RecyclerView to position
        RecyclerView.LayoutManager layoutManager = exploreRecyclerView.getLayoutManager();
        layoutManager.scrollToPosition(position);

        ExploreRecyclerViewAdapter.ViewHolder viewHolder = (ExploreRecyclerViewAdapter.ViewHolder) exploreRecyclerView.findViewHolderForAdapterPosition(position);
        System.out.println("Visible data:");

        // Check if the viewHolder is showing the always visible widgets. These widgets are: exploreItemImageView and titleTextView
        assertThat(viewHolder.exploreItemImageView.getVisibility(), equalTo(View.VISIBLE));
        System.out.println("- exploreItemImageView: an image.");

        assertThat(viewHolder.titleTextView.getVisibility(), equalTo(View.VISIBLE));
        System.out.println("- titleTextView: " + viewHolder.titleTextView.getText());

        // If the viewHolder has a hyperlink, click it
        if (viewHolder.bottomDescriptionTextView.getVisibility() == View.VISIBLE) {
            System.out.println("- bottomDescriptionTextView (hyperlink): " + viewHolder.bottomDescriptionTextView.getText());
            System.out.println("The CardView has a hyperlink... performing click.");

            // Find the Span into the bottomDescriptionTextView's text and call its onClick method
            Spanned spanned = (Spanned) viewHolder.bottomDescriptionTextView.getText();
            ClickableSpan[] clickableSpans = spanned.getSpans(0, spanned.length(), ClickableSpan.class);
            clickableSpans[0].onClick(viewHolder.bottomDescriptionTextView);

            // Assert that the actual Intent has started Chrome custom tab
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            CustomTabsIntent customTabsIntent = builder.build();
            Intent expectedIntent = customTabsIntent.intent;
            Intent actualIntent = ShadowApplication.getInstance().getNextStartedActivity();
            assertEquals(expectedIntent.getComponent(), actualIntent.getComponent());
            System.out.println("The hyperlink has launched the URL.");

            // Assert that the correct URL has been launched
            URLSpan urlSpan = (URLSpan) clickableSpans[0];
            String expectedURLString = urlSpan.getURL().toString();
            String actualURLString = actualIntent.getDataString();
            assertEquals(expectedURLString, actualURLString);
            System.out.println("Hyperlink URL: " + actualURLString);
        }

        // If the viewHolder has buttons, choose one randomly and click it
        if (viewHolder.contentLinearLayout.getChildCount() > 0) {

            for (int i = 0; i < viewHolder.contentLinearLayout.getChildCount(); i++) {
                Button button = (Button) viewHolder.contentLinearLayout.getChildAt(i);
                System.out.println("- shopButton #" + i + ": " + button.getText());
            }

            int childIndex = new Random().nextInt(viewHolder.contentLinearLayout.getChildCount());

            System.out.println("Performing click on Button #" + childIndex + "...");

            // Find the Button into contentLinearLayout and perform a click
            Button button = (Button) viewHolder.contentLinearLayout.getChildAt(childIndex);
            button.performClick();

            // Check if the actual Intent has started Chrome custom tab
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            CustomTabsIntent customTabsIntent = builder.build();
            Intent expectedIntent = customTabsIntent.intent;
            Intent actualIntent = ShadowApplication.getInstance().getNextStartedActivity();
            assertEquals(expectedIntent.getComponent(), actualIntent.getComponent());
            System.out.println("Button #" + childIndex + " has launched the URL");

            // Assert that the correct URL has been launched
            String expectedURLString = (String) button.getTag();
            String actualURLString = actualIntent.getDataString();
            assertEquals(expectedURLString, actualURLString);
            System.out.println("Button #" + childIndex + " URL: " + actualURLString);
        }
    }
}

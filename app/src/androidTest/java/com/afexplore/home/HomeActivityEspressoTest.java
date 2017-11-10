package com.afexplore.home;

import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.afexplore.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import static android.support.customtabs.CustomTabsIntent.EXTRA_ENABLE_URLBAR_HIDING;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.intent.matcher.UriMatchers.hasHost;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.afexplore.testutils.TestUtilsActionsEspresso.clickOnButtonInContentLayout;
import static com.afexplore.testutils.TestUtilsActionsEspresso.clickOnClickableSpanIntoTextViewWithId;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Performs an non instrumented test of the MainFragmentActivity flow to scroll over the
 * RecyclerView clicking the buttons, hyperlinks and performing the corresponding API calls.
 * <p>
 * Five tests are performed, choosing randomly five ViewCards to interact with.
 *
 * IMPORTANT: Chrome Custom Tabs is a third party resource which does not enable unit testing.
 * So the tester must click on the "Close" and "Back" buttons manually during the testing cycle
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class HomeActivityEspressoTest {

    @Rule
    public IntentsTestRule<HomeActivity> activityTestRule = new IntentsTestRule<HomeActivity>(HomeActivity.class);

    /* Widgets */
    private RecyclerView exploreRecyclerView;
    private ExploreRecyclerViewAdapter exploreRecyclerViewAdapter;

    @Before
    public void setUp() throws Exception {

        // Get the RecyclerView
        exploreRecyclerView = activityTestRule.getActivity().findViewById(R.id.exploreRecyclerView);

        // Get the ExploreRecyclerViewAdapter
        exploreRecyclerViewAdapter = (ExploreRecyclerViewAdapter) exploreRecyclerView.getAdapter();
    }

    @Test
    public void performWalkthroughInstrumentedTest1() throws Exception {

        // Assert that RecyclerView is not empty
        assertFalse(exploreRecyclerViewAdapter.getItemCount() == 0);

        // Pick any CardView randomly
        int position = new Random().nextInt(exploreRecyclerViewAdapter.getItemCount());

        // Perform walkthrough interacting with the CardView of the picked position
        performWalkthroughInstrumentedTest(position);
    }

    @Test
    public void performWalkthroughInstrumentedTest2() throws Exception {

        // Assert that RecyclerView is not empty
        assertFalse(exploreRecyclerViewAdapter.getItemCount() == 0);

        // Pick any CardView randomly
        int position = new Random().nextInt(exploreRecyclerViewAdapter.getItemCount());

        // Perform walkthrough interacting with the CardView of the picked position
        performWalkthroughInstrumentedTest(position);
    }

    @Test
    public void performWalkthroughInstrumentedTest3() throws Exception {

        // Assert that RecyclerView is not empty
        assertFalse(exploreRecyclerViewAdapter.getItemCount() == 0);

        // Pick any CardView randomly
        int position = new Random().nextInt(exploreRecyclerViewAdapter.getItemCount());

        // Perform walkthrough interacting with the CardView of the picked position
        performWalkthroughInstrumentedTest(position);
    }

    @Test
    public void performWalkthroughInstrumentedTest4() throws Exception {

        // Assert that RecyclerView is not empty
        assertFalse(exploreRecyclerViewAdapter.getItemCount() == 0);

        // Pick any CardView randomly
        int position = new Random().nextInt(exploreRecyclerViewAdapter.getItemCount());

        // Perform walkthrough interacting with the CardView of the picked position
        performWalkthroughInstrumentedTest(position);
    }

    @Test
    public void performWalkthroughInstrumentedTest5() throws Exception {

        // Assert that RecyclerView is not empty
        assertFalse(exploreRecyclerViewAdapter.getItemCount() == 0);

        // Pick any CardView randomly
        int position = new Random().nextInt(exploreRecyclerViewAdapter.getItemCount());

        // Perform walkthrough interacting with the CardView of the picked position
        performWalkthroughInstrumentedTest(position);
    }

    /**
     * Performs the instrumented test for the CardView in the given position.
     * */
    private void performWalkthroughInstrumentedTest(int position) throws InterruptedException {

        Log.e("HomeActivityEspressoTest", "\n");
        Log.e("HomeActivityEspressoTest", "Testing CardView number: " + position);

        // Assert that RecyclerView is showing
        onView(withId(R.id.exploreRecyclerView)).check(matches(isDisplayed()));

        // Scroll exploreRecyclerView to the given position
        onView(withId(R.id.exploreRecyclerView)).perform(scrollToPosition(position));

        // Get the viewHolder for the given position
        ExploreRecyclerViewAdapter.ViewHolder viewHolder = (ExploreRecyclerViewAdapter.ViewHolder) exploreRecyclerView.findViewHolderForAdapterPosition(position);

        // Check if the viewHolder is showing the always visible widgets. These widgets are: exploreItemImageView and titleTextView
        assertNotNull(viewHolder.exploreItemImageView);
        assertThat(viewHolder.exploreItemImageView.getVisibility(), equalTo(View.VISIBLE));

        assertNotNull(viewHolder.titleTextView);
        assertThat(viewHolder.titleTextView.getVisibility(), equalTo(View.VISIBLE));

        // If the viewHolder has a hyperlink, click it
        if (viewHolder.bottomDescriptionTextView.getVisibility() == View.VISIBLE) {

            Log.e("HomeActivityEspressoTest", "Showing Hyperlink URL. Please click on Close button to continue...");

            // Click on the hyperlink
            onView(withId(R.id.exploreRecyclerView)).perform(actionOnItemAtPosition(position, clickOnClickableSpanIntoTextViewWithId(R.id.bottomDescriptionTextView)));

            // This loop will keep the Espresso thread waiting while the response callback returns
            // In order to return, the user must close the Chrome custom tab
            pauseEspressoThreadWhileChromeCustomTabIsShown();

            // Assert that a Chrome custom tab has been started by identifying the Chrome custom tab Intent
            // as the last one sent by the application
            intended(allOf(hasAction(equalTo(Intent.ACTION_VIEW)), hasData(hasHost(equalTo("www.abercrombie.com"))), hasExtra(EXTRA_ENABLE_URLBAR_HIDING, false)));

        } else {
            Log.e("HomeActivityEspressoTest", "This CardView does not have Hyperlink URL.");
        }

        // If the viewHolder has buttons, choose one randomly and click it
        if (viewHolder.contentLinearLayout.getChildCount() > 0) {

            Log.e("HomeActivityEspressoTest", "Showing Button URL. Please click on Back button to continue...");

            int childIndex = new Random().nextInt(viewHolder.contentLinearLayout.getChildCount());

            // Find the Button into contentLinearLayout and perform a click
            Button button = (Button) viewHolder.contentLinearLayout.getChildAt(childIndex);
            onView(withId(R.id.exploreRecyclerView)).perform(actionOnItemAtPosition(position, clickOnButtonInContentLayout(button)));

            // This loop will keep the Espresso thread waiting while the response callback returns
            // In order to return, the user must close the Chrome custom tab
            pauseEspressoThreadWhileChromeCustomTabIsShown();

            // Assert that a Chrome custom tab has been started by identifying the Chrome custom tab Intent
            // as the last one sent by the application
            intended(allOf(hasAction(equalTo(Intent.ACTION_VIEW)), hasData(hasHost(equalTo("www.abercrombie.com"))), hasExtra(EXTRA_ENABLE_URLBAR_HIDING, true)));

        } else {
            Log.e("HomeActivityEspressoTest", "This CardView does not have Button URL.");
        }

    }

    /**
     * This loop will keep the Espresso thread waiting while the response callback returns.
     * In order to return, the user must close the Chrome custom tab manually.
     */
    private void pauseEspressoThreadWhileChromeCustomTabIsShown() throws InterruptedException {

        while (!activityTestRule.getActivity().isHasShownChromeCustomTabs()) {
            Thread.sleep(1000);
        }

        // Reset hasShownChromeCustomTabs to false
        activityTestRule.getActivity().setHasShownChromeCustomTabs(false);
    }
}

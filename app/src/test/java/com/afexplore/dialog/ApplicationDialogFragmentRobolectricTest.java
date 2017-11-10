package com.afexplore.dialog;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Button;

import com.afexplore.BuildConfig;
import com.afexplore.home.HomeActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLooper;

import java.util.concurrent.atomic.AtomicBoolean;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class ApplicationDialogFragmentRobolectricTest {

    // The Activity which holds the view hierarchy
    private HomeActivity homeActivity;

    // Class ApplicationDialogFragment will be tested
    private ApplicationDialogFragment applicationDialogFragment;

    @Before
    public void setUp() throws Exception {

        // Initialize HomeActivity
        homeActivity = Robolectric.buildActivity(HomeActivity.class).create().get();

        applicationDialogFragment = ApplicationDialogFragment.newInstance();
    }

    /**
     * Tests that the dialog is successfully showing on screen.
     *
     * After pressing the positive button, the dialog should be dismissed and the OnClickListener
     * called.
     * */
    @Test
    public void ApplicationDialogFragment_shouldAppearOnScreenTest1() throws Exception {

        final AtomicBoolean callbackCalled = new AtomicBoolean(false);

        // Show the ApplicationDialogFragment
        showDialogFragment("Hello", "Hello World!", new ApplicationDialogFragmentPositiveButtonOnClickListener());

        // Check that ApplicationDialogFragment has been created
        final ApplicationDialogFragment applicationDialogFragment = (ApplicationDialogFragment) homeActivity.getFragmentManager().findFragmentByTag("applicationDialogFragment");
        assertNotNull(applicationDialogFragment.getDialog());

        // Check that applicationDialogFragment is showing
        assertTrue(applicationDialogFragment.getDialog().isShowing());

        // Click the OK button of applicationDialogFragment and check that applicationDialogFragment has been dismissed
        AlertDialog alertDialog = (AlertDialog) applicationDialogFragment.getDialog();
        Button okButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        okButton.performClick();

        // Check that the dialog is null and therefore dismissed
        assertNull(applicationDialogFragment.getDialog());
    }

    /**
     * Tests that the dialog is successfully showing on screen.
     *
     * After pressing the positive button, the dialog should be dismissed, but without
     * OnCLickListener called.
     * */
    @Test
    public void ApplicationDialogFragment_shouldAppearOnScreenTest2() throws Exception {

        final AtomicBoolean callbackCalled = new AtomicBoolean(false);

        // Show the ApplicationDialogFragment
        showDialogFragment("Hello", "Hello World!", null);

        // Check that ApplicationDialogFragment has been created
        final ApplicationDialogFragment applicationDialogFragment = (ApplicationDialogFragment) homeActivity.getFragmentManager().findFragmentByTag("applicationDialogFragment");
        assertNotNull(applicationDialogFragment.getDialog());

        // Check that applicationDialogFragment is showing
        assertTrue(applicationDialogFragment.getDialog().isShowing());

        // Click the OK button of applicationDialogFragment and check that applicationDialogFragment has been dismissed
        AlertDialog alertDialog = (AlertDialog) applicationDialogFragment.getDialog();
        Button okButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        okButton.performClick();

        // Check that the dialog is null and therefore dismissed
        assertNull(applicationDialogFragment.getDialog());
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
        applicationDialogFragment.show(homeActivity.getFragmentManager(), "applicationDialogFragment");
    }

    /**
     * Called when user presses the OK button in the NoNetworkDialogFragment.
     * It will try to connectToExploreEndpoint again
     */
    private class ApplicationDialogFragmentPositiveButtonOnClickListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {

            System.out.println("Positive Button Pressed. Dialog has been dismissed.");
        }
    }
}

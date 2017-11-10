package com.afexplore.testutils;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.afexplore.R;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

/**
 * Utility set of convenient custom actions to modified the state of the views during instrumented
 * tests.
 */
public class TestUtilsActionsEspresso {

    /**
     * ViewAction to perform click on ClickableSpan.
     * */
    public static ViewAction clickOnClickableSpanIntoTextViewWithId(final int id) {

        return new ViewAction() {

            @Override
            public Matcher<View> getConstraints() {
                return Matchers.instanceOf(TextView.class);
            }

            @Override
            public String getDescription() {
                return "clicking on a ClickableSpan";
            }

            @Override
            public void perform(UiController uiController, View view) {
                TextView textView = view.findViewById(id);
                SpannableString spannableString = (SpannableString) textView.getText();

                // If TextView is empty, nothing to do
                if (spannableString.length() == 0) {
                    throw new NoMatchingViewException.Builder()
                            .includeViewHierarchy(true)
                            .withRootView(textView)
                            .build();
                }

                // Get the links inside the TextView and check if we find textToClick
                ClickableSpan[] clickableSpans = spannableString.getSpans(0, spannableString.length(), ClickableSpan.class);
                if (clickableSpans.length > 0) {
                    clickableSpans[0].onClick(textView);

                    return;
                }

                // textToClick not found in TextView
                throw new NoMatchingViewException.Builder()
                        .includeViewHierarchy(true)
                        .withRootView(textView)
                        .build();
            }
        };
    }

    /**
     * ViewAction click on a child View.
     * */
    public static ViewAction clickOnButtonInContentLayout(final Button button) {
        return new ViewAction() {

            @Override
            public Matcher<View> getConstraints() {
                return Matchers.instanceOf(View.class);
            }

            @Override
            public String getDescription() {
                return "Click on a child view with specified id.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                button.performClick();
            }
        };
    }
}

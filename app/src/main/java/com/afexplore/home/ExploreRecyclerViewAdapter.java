package com.afexplore.home;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afexplore.R;
import com.afexplore.home.datamodels.ContentDataModel;
import com.afexplore.home.datamodels.ExploreItemDataModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.support.customtabs.CustomTabsIntent.EXTRA_ENABLE_URLBAR_HIDING;

class ExploreRecyclerViewAdapter extends RecyclerView.Adapter<ExploreRecyclerViewAdapter.ViewHolder> {

    private static final String EXTRA_CUSTOM_TABS_KEEP_ALIVE = "android.support.customtabs.extra.KEEP_ALIVE";

    // The Activity context
    private Context context;

    // The List with the data to be populated
    private List exploreList;

    // The OnClickListener for the buttons
    private ContentButtonOnClickListener contentButtonOnClickListener;

    /* Constructor */
    ExploreRecyclerViewAdapter(Context context, List ExploreList) {
        this.context = context;
        this.exploreList = ExploreList;
        this.contentButtonOnClickListener = new ContentButtonOnClickListener();
    }

    /**
     * ViewHolder pattern: Inner class needed to keep the references between widgets and data to improve the performance
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView exploreItemImageView;
        TextView topDescriptionTextView;
        TextView titleTextView;
        TextView promoMessageTextView;
        TextView bottomDescriptionTextView;
        LinearLayout contentLinearLayout;

        ViewHolder(View itemView) {
            super(itemView);

            exploreItemImageView = itemView.findViewById(R.id.exploreItemImageView);
            topDescriptionTextView = itemView.findViewById(R.id.topDescriptionTextView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            promoMessageTextView = itemView.findViewById(R.id.promoMessageTextView);
            bottomDescriptionTextView = itemView.findViewById(R.id.bottomDescriptionTextView);
            contentLinearLayout = itemView.findViewById(R.id.contentLinearLayout);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.explore_item_cardview, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        ExploreItemDataModel exploreItemDataModel = (ExploreItemDataModel) exploreList.get(position);

        // Populate RecyclerView
        populateRecyclerView(viewHolder, exploreItemDataModel);
    }

    /**
     * Populate the RecyclerView with the information for each CardView
     * Each CardView might be different, so this view has the ability to be dynamically resizeable,
     * depending of the information which is shown or not.
     */
    private void populateRecyclerView(ViewHolder viewHolder, ExploreItemDataModel exploreItemDataModel) {

        /* Background Image */
        viewHolder.exploreItemImageView.setImageDrawable(null);
        Picasso.with(context).load(exploreItemDataModel.getBackgroundImage()).into(viewHolder.exploreItemImageView);

        /* Top Description */
        String topDescriptionString = exploreItemDataModel.getTopDescription();
        if (topDescriptionString != null) {
            viewHolder.topDescriptionTextView.setVisibility(View.VISIBLE);
            viewHolder.topDescriptionTextView.setText(topDescriptionString);
        } else {
            viewHolder.topDescriptionTextView.setVisibility(View.GONE);
        }

        /* Title */
        viewHolder.titleTextView.setText(exploreItemDataModel.getTitle());

        /* Promo Message */
        String promoMessageString = exploreItemDataModel.getPromoMessage();
        if (promoMessageString != null) {
            viewHolder.promoMessageTextView.setVisibility(View.VISIBLE);
            viewHolder.promoMessageTextView.setText(promoMessageString);
        } else {
            viewHolder.promoMessageTextView.setVisibility(View.GONE);
        }

        /* Bottom Description */
        String bottomDescriptionString = exploreItemDataModel.getBottomDescription();
        if (bottomDescriptionString != null) {
            viewHolder.bottomDescriptionTextView.setVisibility(View.VISIBLE);
            viewHolder.bottomDescriptionTextView.setText(makeStringSpanned(bottomDescriptionString), TextView.BufferType.SPANNABLE);
            viewHolder.bottomDescriptionTextView.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            viewHolder.bottomDescriptionTextView.setVisibility(View.GONE);
        }

        /* Buttons - They might be zero or more */
        viewHolder.contentLinearLayout.removeAllViews();
        List<ContentDataModel> contentDataModelList = exploreItemDataModel.getContentDataModel();
        if (contentDataModelList != null) {
            for (ContentDataModel contentDataModel : contentDataModelList) {

                // For each item in contentDataModelList create a button
                Button button = new Button(context);
                button.setBackgroundResource(R.drawable.button_background_selector);
                button.setStateListAnimator(null);
                button.setText(contentDataModel.getTitle());
                button.setTextColor(context.getResources().getColorStateList(R.color.button_text_color_selector, null));
                button.setTag(contentDataModel.getTarget()); // Set a which will contain the URL to be shown if the button is clicked
                button.setOnClickListener(contentButtonOnClickListener);

                RelativeLayout.LayoutParams buttonLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                buttonLayoutParams.setMargins(0, (int) context.getResources().getDimension(R.dimen.content_button_top_margin), 0, 0);
                button.setLayoutParams(buttonLayoutParams);

                // Add the button to contentLinearLayout
                viewHolder.contentLinearLayout.addView(button);
            }
        }
    }

    /**
     * Identifies the part of the text which is a URL and add a clickable span along the URL text.
     */
    private Spanned makeStringSpanned(String textString) {

        // Fixes a bug in the API response where one of the URLs comes with an extra '\\'
        textString = textString.replace("\\", "");

        // Makes the textString HTML styled
        Spannable textSpannable = (Spannable) Html.fromHtml(textString, Html.FROM_HTML_MODE_LEGACY);

        // Get the parts of the text strings which are spanned as URL (it is only one)
        URLSpan[] urlSpans = textSpannable.getSpans(0, textSpannable.length(), URLSpan.class);

        // Get the start and end indexes of the spanned text
        URLSpan urlSpan = urlSpans[0];
        int spanStart = textSpannable.getSpanStart(urlSpan);
        int spanEnd = textSpannable.getSpanEnd(urlSpan);

        // Create a TextClickableSpan instance to get the click in the spanned text and add this instance as the new span of the URL
        TextClickableSpan textClickableSpan = new TextClickableSpan(urlSpan.getURL());
        textSpannable.setSpan(textClickableSpan, spanStart, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Remove the previous span
        textSpannable.removeSpan(urlSpan);

        return textSpannable;
    }

    /**
     * Called when a spanned text (hyperlink) in a TextView is clicked.
     * <p>
     * It creates a CustomTabsIntent to launch the URL in a Chrome custom tab.
     */
    private class TextClickableSpan extends URLSpan {

        public TextClickableSpan(String url) {
            super(url);
        }

        @Override
        public void onClick(View view) {

            // Get the URL string
            String urlString = getURL();

            // Launch a Chrome custom tab to open the URL
            launchChromeCustomTab(urlString, true);
        }
    }

    /**
     * Called when a spanned text (hyperlink) in a TextView is clicked.
     * <p>
     * It creates a CustomTabsIntent to launch the URL in a Chrome custom tab.
     */
    private class ContentButtonOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            // Get the URL string from the Button's tag
            String urlString = (String) view.getTag();

            // Launch a Chrome custom tab to open the URL
            launchChromeCustomTab(urlString, false);
        }
    }

    /**
     * Launches a Chrome custom tab.
     */
    private void launchChromeCustomTab(String urlString, boolean modal) {

        HomeActivity homeActivity = (HomeActivity) context;
        homeActivity.setWillShowChromeCustomTabs(true);

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary));
        builder.setShowTitle(true);

        if (modal) { // If the Chrome custom tab is modal, it will be shown with a close button and the animation transition to show it is from bottom to top
            builder.setStartAnimations(context, R.anim.slide_in_bottom, R.anim.fixed);
            builder.setExitAnimations(context, R.anim.fixed, R.anim.slide_out_bottom);
            builder.setCloseButtonIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_action_close));

        } else { // If not modal, it will be shown with a back button and the animation transition to show it is from right to left
            builder.setStartAnimations(context, R.anim.slide_in_right, R.anim.slide_out_left);
            builder.setExitAnimations(context, R.anim.slide_in_left, R.anim.slide_out_right);
            builder.setCloseButtonIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_action_back));
        }

        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.intent.putExtra(EXTRA_ENABLE_URLBAR_HIDING, modal ? false : true);
        customTabsIntent.launchUrl(context, Uri.parse(urlString));
    }

    /**
     * Returns the data list.
     */
    public List<ExploreItemDataModel> getList() {
        return exploreList;
    }

    /**
     * Returns the item from the data list localized in the given position.
     */
    public ExploreItemDataModel getItem(int position) {
        return (ExploreItemDataModel) exploreList.get(position);
    }

    @Override
    public int getItemCount() {
        return exploreList.size();
    }
}

package com.afexplore.home;

import android.support.v7.widget.RecyclerView;

import com.afexplore.BuildConfig;
import com.afexplore.R;
import com.afexplore.constants.Constants;
import com.afexplore.home.datamodels.ExploreItemDataModel;
import com.afexplore.util.json.JSONReader;
import com.google.gson.reflect.TypeToken;

import org.hamcrest.collection.IsEmptyCollection;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class ExploreRecyclerViewAdapterRobolectricTest {

    private RecyclerView exploreRecyclerView;

    @Before
    public void setUp() throws Exception {

        HomeActivity homeActivity = Robolectric.buildActivity(HomeActivity.class).create().get();

        // Get exploreRecyclerView from homeActivity
        exploreRecyclerView = homeActivity.findViewById(R.id.exploreRecyclerView);

        // Get the JSON response
        List<ExploreItemDataModel> exploreItemDataModelList = JSONReader.loadJSONObjectFromAsset(homeActivity, Constants.EXPLORE_JSON_FILENAME, new TypeToken<List<ExploreItemDataModel>>() {}.getType());

        // Specify an Adapter
        ExploreRecyclerViewAdapter exploreRecyclerViewAdapter = new ExploreRecyclerViewAdapter(homeActivity, exploreItemDataModelList);
        exploreRecyclerView.setAdapter(exploreRecyclerViewAdapter);
    }

    @Test
    public void ExploreRecyclerViewAdapter_shouldContainItemsTest() throws Exception {

        ExploreRecyclerViewAdapter adapter = (ExploreRecyclerViewAdapter) exploreRecyclerView.getAdapter();

        // Assert that the adapter's list is not empty
        assertThat(adapter.getList(), not(IsEmptyCollection.<ExploreItemDataModel>empty()));

        // Assert that the adapter contains 10 items
        assertEquals(10, adapter.getItemCount());

        // Assert that list items are instances of ExploreItemDataModel
        assertThat(adapter.getItem(0), instanceOf(ExploreItemDataModel.class));
    }
}

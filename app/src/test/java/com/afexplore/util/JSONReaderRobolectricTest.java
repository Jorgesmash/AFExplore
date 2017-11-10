package com.afexplore.util;

import com.afexplore.BuildConfig;
import com.afexplore.constants.Constants;
import com.afexplore.home.HomeActivity;
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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

/**
 * Performs an non instrumented test of JSONReader to validate that it is able to return valid JSON
 * objects and that is well code-protected.
 * */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class JSONReaderRobolectricTest {

    // The Activity which holds the view hierarchy
    private HomeActivity homeActivity;

    // Class JSONReader will be tested
    private JSONReader jsonReader;

    @Before
    public void setUp() throws Exception {

        homeActivity = Robolectric.buildActivity(HomeActivity.class).create().get();

        jsonReader = JSONReader.newInstance(homeActivity);

        assertNotNull(jsonReader);
    }

    /**
     * Test to validate that JSONReader is able to return a valid JSON object.
     * */
    @Test
    public void loadJSONFromAsset_shouldReturnNonEmptyExploreItemDataModelListTest() throws Exception {

        // Get the JSON array from file and place it in a List
        List<ExploreItemDataModel> list = jsonReader.loadJSONObjectFromAsset(Constants.EXPLORE_JSON_FILENAME, new TypeToken<List<ExploreItemDataModel>>(){}.getType());

        // Assert that the list is not empty
        assertThat(list, not(IsEmptyCollection.<ExploreItemDataModel>empty()));

        // Assert that list items are instances of ExploreItemDataModel
        assertThat(list.get(0), instanceOf(ExploreItemDataModel.class));
    }

    /**
     * Test to check that JSONReader returns null when the generic type is not defined.
     * */
    @Test
    public void loadJSONFromAsset_shouldReturnNullTest1() throws Exception {

        // Get the JSON array from file and place it in a List
        List<ExploreItemDataModel> list = jsonReader.loadJSONObjectFromAsset(Constants.EXPLORE_JSON_FILENAME, null);

        // Assert that the list is null
        assertNull(list);
    }

    /**
     * Test to check that JSONReader returns null when both the filename and the generic type are
     * not defined.
     * */
    @Test
    public void loadJSONFromAsset_shouldReturnNullTest2() throws Exception {

        // Get the JSON array from file and place it in a List
        List<ExploreItemDataModel> list = jsonReader.loadJSONObjectFromAsset(null, null);

        // Assert that the list is null
        assertNull(list);
    }

    /**
     * Test to check that JSONReader returns null when the file is not found
     * */
    @Test
    public void loadJSONFromAsset_shouldReturnNullTest3() throws Exception {

        // Get the JSON array from file and place it in a List
        List<ExploreItemDataModel> list = jsonReader.loadJSONObjectFromAsset("whatever", new TypeToken<List<ExploreItemDataModel>>(){}.getType());

        // Assert that the list is null
        assertNull(list);
    }

    /**
     * Test to check that JSONReader returns a valid JSON string
     * */
    @Test
    public void loadJSONFromAsset_shouldReturnNonEmptyJSONStringTest() throws Exception {

        // Get the JSON array from file and place it in a List
        String jsonString = jsonReader.loadJSONStringFromAsset(Constants.EXPLORE_JSON_FILENAME);

        // Assert that the list is null
        assertNotNull(jsonString);
    }

    /**
     * Test to check that JSONReader returns null JSON string when the file is not found
     * */
    @Test
    public void loadJSONFromAsset_shouldReturnNullJSONStringTest() throws Exception {

        // Get the JSON array from file and place it in a List
        String jsonString = jsonReader.loadJSONStringFromAsset("whatever");

        // Assert that the list is null
        assertNull(jsonString);
    }
}

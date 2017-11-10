package com.afexplore.home.api;

import com.afexplore.home.datamodels.ExploreItemDataModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Interface where the API call to the Explore endpoint is defined.
 * */
public interface APIInterface {

    // Explore API endpoint with method GET
    @GET("codeTest_exploreData.json")
    Call<List<ExploreItemDataModel>> getExploreInformation();
}

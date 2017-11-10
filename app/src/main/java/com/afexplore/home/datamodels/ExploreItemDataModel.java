
package com.afexplore.home.datamodels;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExploreItemDataModel {

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("backgroundImage")
    @Expose
    private String backgroundImage;

    @SerializedName("content")
    @Expose
    private List<ContentDataModel> contentDataModel = null;

    @SerializedName("promoMessage")
    @Expose
    private String promoMessage;

    @SerializedName("topDescription")
    @Expose
    private String topDescription;

    @SerializedName("bottomDescription")
    @Expose
    private String bottomDescription;

    /* Getters and Setters */
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public List<ContentDataModel> getContentDataModel() {
        return contentDataModel;
    }

    public void setContentDataModel(List<ContentDataModel> contentDataModel) {
        this.contentDataModel = contentDataModel;
    }

    public String getPromoMessage() {
        return promoMessage;
    }

    public void setPromoMessage(String promoMessage) {
        this.promoMessage = promoMessage;
    }

    public String getTopDescription() {
        return topDescription;
    }

    public void setTopDescription(String topDescription) {
        this.topDescription = topDescription;
    }

    public String getBottomDescription() {
        return bottomDescription;
    }

    public void setBottomDescription(String bottomDescription) {
        this.bottomDescription = bottomDescription;
    }
}

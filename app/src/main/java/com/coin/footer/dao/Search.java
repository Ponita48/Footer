package com.coin.footer.dao;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Dwiki on 3/26/2017.
 */
public class Search {
    @SerializedName("food_name")
    @Expose
    private String foodName;
    @SerializedName("restaurant_name")
    @Expose
    private String restaurantName;
    @SerializedName("ID_restaurant")
    @Expose
    private String iDRestaurant;

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getiDRestaurant() {
        return iDRestaurant;
    }

    public void setiDRestaurant(String iDRestaurant) {
        this.iDRestaurant = iDRestaurant;
    }
}

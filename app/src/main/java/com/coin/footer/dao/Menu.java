
package com.coin.footer.dao;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Menu {

    @SerializedName("ID_food")
    @Expose
    private String iDFood;
    @SerializedName("food_name")
    @Expose
    private String foodName;
    @SerializedName("descriptions")
    @Expose
    private String descriptions;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("ID_restaurant")
    @Expose
    private String iDRestaurant;

    public String getIDFood() {
        return iDFood;
    }

    public void setIDFood(String iDFood) {
        this.iDFood = iDFood;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getIDRestaurant() {
        return iDRestaurant;
    }

    public void setIDRestaurant(String iDRestaurant) {
        this.iDRestaurant = iDRestaurant;
    }

}

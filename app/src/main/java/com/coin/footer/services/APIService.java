package com.coin.footer.services;

import com.coin.footer.dao.LoginData;
import com.coin.footer.dao.Restaurant;
import com.coin.footer.dao.RestaurantDisplay;
import com.coin.footer.dao.SearchDisplay;
import com.coin.footer.dao.UserDisplay;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.coin.footer.dao.DefaultMessage;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Digunakan untuk menghubungkan android dengan server di internet.
 */
public interface APIService {
    @GET("user/all_restaurant/{token}")
    Call<RestaurantDisplay> getAll(@Path("token") String token);
    @GET("user/restaurant/{resto}/{token}")
    Call<RestaurantDisplay> getResto(@Path("resto") String resto, @Path("token") String token);
    @GET("user/like/{resto}/{token}")
    Call<DefaultMessage> getLike(@Path("resto") String restoId, @Path("token") String token);
    @GET("user/fav/{resto}/{token}")
    Call<DefaultMessage> getFav(@Path("resto") String restoId, @Path("token") String token);
    @GET("user/select_favorite/{token}")
    Call<RestaurantDisplay> getAllFav(@Path("token") String token);
    @GET("user/select_user/{token}")
    Call<UserDisplay> getUser(@Path("token") String token);
    @FormUrlEncoded
    @POST("user/search/{cat}")
    Call<SearchDisplay> postSearch(@Path("cat") String cat, @Field("keySearch") String key);
    @FormUrlEncoded
    @POST("user/login")
    Call<LoginData> postLogin(@Field("username") String username,
                              @Field("password") String password);
    @FormUrlEncoded
    @POST("user/register")
    Call<DefaultMessage> postRegister(@Field("username") String username,
                                      @Field("fullname") String fullname,
                                      @Field("nickname") String nickname,
                                      @Field("gender") String gender,
                                      @Field("email") String email,
                                      @Field("password") String password);

    String baseUrl = "http://hda2.jux.in/footer_backend/index.php/api/";
    Gson gson = new GsonBuilder().create();
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    APIService services = retrofit.create(APIService.class);
}

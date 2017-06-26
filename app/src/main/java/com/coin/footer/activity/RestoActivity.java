package com.coin.footer.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.coin.footer.R;
import com.coin.footer.adapter.MenuListAdapter;
import com.coin.footer.dao.DefaultMessage;
import com.coin.footer.dao.Menu;
import com.coin.footer.dao.Restaurant;
import com.coin.footer.dao.RestaurantDisplay;
import com.coin.footer.services.APIService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestoActivity extends AppCompatActivity {
    
    View toolbarLayout;
    View layoutBawah;
    ProgressBar loading;
    AQuery aq;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resto);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        preferences = getSharedPreferences("USER", MODE_PRIVATE);

        toolbarLayout = findViewById(R.id.toolbar_layout);
        layoutBawah = findViewById(R.id.layoutBawah);
        loading = (ProgressBar) findViewById(R.id.loadingResto);
        loading.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.loading),
                PorterDuff.Mode.MULTIPLY);
        aq = new AQuery(this);
        
        String idResto = getIntent().getStringExtra("ID_RESTO");
        Toast.makeText(RestoActivity.this, preferences.getString("TOKEN", "0"), Toast.LENGTH_SHORT).show();
        getResto(idResto);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            layoutBawah.setVisibility(show ? View.GONE : View.VISIBLE);
            layoutBawah.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    layoutBawah.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });
            toolbarLayout.setVisibility(show ? View.GONE : View.VISIBLE);
            toolbarLayout.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    toolbarLayout.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            loading.setVisibility(show ? View.VISIBLE : View.GONE);
            loading.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    loading.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            loading.setVisibility(show ? View.VISIBLE : View.GONE);
            layoutBawah.setVisibility(show ? View.GONE : View.VISIBLE);
            toolbarLayout.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public void getResto(final String restoId) {
        showProgress(true);
        String token = preferences.getString("TOKEN", "0");
        Call<RestaurantDisplay> call = APIService.services.getResto(restoId, token);
        call.enqueue(new Callback<RestaurantDisplay>() {
            @Override
            public void onResponse(Call<RestaurantDisplay> call, Response<RestaurantDisplay> response) {
                if (response.isSuccessful()) {
                    final Restaurant body = response.body().getRestaurant().get(0);
                    if (body.getPhoto() != null) {
                        aq.id(R.id.fotoResto).image(body.getPhoto());
                    }
                    aq.id(R.id.lokasiRestoInfo).text(body.getLocation());
                    aq.id(R.id.numLike).text(String.valueOf(body.getPopularity()));
                    aq.id(R.id.likeButton).clicked(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addLike(restoId);
                        }
                    });
                    aq.id(R.id.favButton).clicked(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addFav(restoId);
                        }
                    });
                    List<Menu> makanan = body.getMenu();
                    LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                    LinearLayout list = (LinearLayout) findViewById(R.id.listMakanan);
                    for (Menu makan : makanan) {
                        LinearLayout convertView = (LinearLayout) inflater.inflate(R.layout.list_menu, null, false);
                        TextView nama = (TextView) convertView.findViewById(R.id.namaMakanan);
                        TextView harga = (TextView) convertView.findViewById(R.id.hargaMakanan);
                        nama.setText(makan.getFoodName());
                        harga.setText("Rp." + makan.getPrice());
                        list.addView(convertView);
                    }
                    aq.id(R.id.linkMap).clicked(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent in = new Intent(RestoActivity.this, MapsActivity.class);
                            in.putExtra("lat", body.getLocationLatitude());
                            in.putExtra("lon", body.getLocationLongitude());
                            in.putExtra("nama", body.getRestaurantName());
                            startActivity(in);
                        }
                    });
                    setTitle(body.getRestaurantName());
                    if (body.getLove() == 1) {
                        Button fav = (Button) findViewById(R.id.favButton);
                        Drawable img = getResources().getDrawable(R.drawable.ic_favorite_red_24dp);
                        fav.setCompoundDrawables(img, null, null, null);
                    }
                }
                showProgress(false);
            }

            @Override
            public void onFailure(Call<RestaurantDisplay> call, Throwable t) {

            }
        });
    }
    
    public void addLike(String restoID) {
        Call<DefaultMessage> call = APIService.services.getLike(restoID, preferences.getString("TOKEN", "0"));
        call.enqueue(new Callback<DefaultMessage>() {
            @Override
            public void onResponse(Call<DefaultMessage> call, Response<DefaultMessage> response) {
                if (response.isSuccessful()) {
                    Button fav = (Button) findViewById(R.id.likeButton);
                    Drawable img = getResources().getDrawable(R.drawable.ic_thumb_up_blue_24dp);
                    int numLike = Integer.parseInt(aq.id(R.id.numLike).getText().toString());
                    if (fav.getCompoundDrawables()[0] == img) {
                        img = getResources().getDrawable(R.drawable.ic_thumb_up_black_24dp);
                        fav.setCompoundDrawables(img, null, null, null);
                        numLike--;
                    } else {
                        fav.setCompoundDrawables(img, null, null, null);
                        numLike++;
                    }
                    aq.id(numLike).text(numLike);
                } else {
                    if (preferences.getString("TOKEN", "0").equals("0")) {
                        Toast.makeText(RestoActivity.this, "Please login to do this", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RestoActivity.this, "Connection timed out", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<DefaultMessage> call, Throwable t) {
                Toast.makeText(RestoActivity.this, "Please check your connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addFav(String restoID) {
        Call<DefaultMessage> call = APIService.services.getLike(restoID, preferences.getString("TOKEN", "0"));
        call.enqueue(new Callback<DefaultMessage>() {
            @Override
            public void onResponse(Call<DefaultMessage> call, Response<DefaultMessage> response) {
                if (response.isSuccessful()) {
                    Button fav = (Button) findViewById(R.id.likeButton);
                    Drawable img = getResources().getDrawable(R.drawable.ic_favorite_red_24dp);
                    if (fav.getCompoundDrawables()[0] == img) {
                        img = getResources().getDrawable(R.drawable.ic_favorite_black_24dp);
                        fav.setCompoundDrawables(img, null, null, null);
                    } else {
                        fav.setCompoundDrawables(img, null, null, null);
                    }
                } else {
                    Toast.makeText(RestoActivity.this, "Connection timed out", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DefaultMessage> call, Throwable t) {
                Toast.makeText(RestoActivity.this, "Please check your connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

}

package com.coin.footer.activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.androidquery.AQuery;
import com.coin.footer.R;
import com.coin.footer.dao.UserDisplay;
import com.coin.footer.services.APIService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;

public class UserActivity extends AppCompatActivity {

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        preferences = getSharedPreferences("USER", MODE_PRIVATE);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getUser() {
        String token = preferences.getString("TOKEN", "0");
        final AQuery aq = new AQuery(this);
        Call<UserDisplay> call = APIService.services.getUser(token);
        call.enqueue(new Callback<UserDisplay>() {
            @Override
            public void onResponse(Call<UserDisplay> call, Response<UserDisplay> response) {
                if (response.isSuccessful()) {
                    UserDisplay body = response.body();
                    aq.id(R.id.unameUser).text(body.getData().get(0).getUsername());
                    aq.id(R.id.emailUser).text(body.getData().get(0).getEmail());
                    aq.id(R.id.namaUser).text(body.getData().get(0).getFullname());
                    aq.id(R.id.genderUser).text(body.getData().get(0).getGender());
                    aq.id(R.id.nickUser).text(body.getData().get(0).getNickname());

                    if (body.getData().get(0).getPhoto() != null) {
                        aq.id(R.id.fotoUser).image(body.getData().get(0).getPhoto());
                    }
                }
            }

            @Override
            public void onFailure(Call<UserDisplay> call, Throwable t) {

            }
        });
    }
}

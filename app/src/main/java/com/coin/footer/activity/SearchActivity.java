package com.coin.footer.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.coin.footer.R;
import com.coin.footer.adapter.MenuListAdapter;
import com.coin.footer.dao.SearchDisplay;
import com.coin.footer.services.APIService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    EditText edit;
    Button btn;
    RadioGroup rg;
    String category;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        edit = (EditText) findViewById(R.id.searchBar);
        btn = (Button) findViewById(R.id.buttonSend);
        rg = (RadioGroup) findViewById(R.id.category);

        if (rg.getCheckedRadioButtonId() == R.id.food) {
            category = "foods";
        } else {
            category = "restaurants";
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send(category);
            }
        });

    }

    public void send(String category) {
        String search = edit.getText().toString();
        Call<SearchDisplay> call = APIService.services.postSearch(category, search);
        call.enqueue(new Callback<SearchDisplay>() {
            @Override
            public void onResponse(Call<SearchDisplay> call, Response<SearchDisplay> response) {
                if (response.isSuccessful()) {
                    final SearchDisplay body = response.body();
                    MenuListAdapter adapter = new MenuListAdapter(getApplicationContext(), body.getData());
                    list.setAdapter(adapter);
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent in = new Intent(SearchActivity.this, RestoActivity.class);
                            in.putExtra("ID_RESTO", body.getData().get(position).getiDRestaurant());
                            startActivity(in);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<SearchDisplay> call, Throwable t) {

            }
        });
    }
}

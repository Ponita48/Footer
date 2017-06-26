package com.coin.footer.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.androidquery.util.Constants;
import com.coin.footer.R;
import com.coin.footer.dao.Restaurant;
import com.coin.footer.dao.Util;

import java.util.List;

public class RestoListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<Restaurant> restaurants;

    public RestoListAdapter(Context context, List<Restaurant> restaurants) {
        this.context = context;
        this.restaurants = restaurants;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return restaurants.size();
    }

    @Override
    public Restaurant getItem(int position) {
        return restaurants.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_resto, parent, false);
        }

        AQuery aq = new AQuery(convertView);
        if (restaurants.get(position).getPhoto() != null) {
            aq.id(R.id.fotoResto).image(restaurants.get(position).getPhoto(), true, true, 200,
                    R.color.white, null, Constants.FADE_IN_NETWORK);
        }
        aq.id(R.id.namaResto).text(restaurants.get(position).getRestaurantName());
        aq.id(R.id.lokasiResto).text(restaurants.get(position).getLocation());
        aq.id(R.id.numLike).text(String.valueOf(restaurants.get(position).getPopularity()));


        return convertView;
    }
}

package com.coin.footer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.coin.footer.R;
import com.coin.footer.dao.Menu;
import com.coin.footer.dao.Search;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dwiki on 3/23/2017.
 */
public class MenuListAdapter extends BaseAdapter {

    LayoutInflater inflater;
    Context context;
    List<Search> makanan;

    public MenuListAdapter(Context context, List<Search> makanan) {
        this.context = context;
        this.makanan = makanan;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return makanan.size();
    }

    @Override
    public Search getItem(int position) {
        return makanan.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_menu, parent, false);
            TextView nama = (TextView) convertView.findViewById(R.id.namaMakanan);
            TextView harga = (TextView) convertView.findViewById(R.id.hargaMakanan);
            nama.setText(getItem(position).getFoodName());
            harga.setText(getItem(position).getRestaurantName());
        }



        return convertView;
    }
}

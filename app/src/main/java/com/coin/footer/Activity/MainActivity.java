package com.coin.footer.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.coin.footer.fragment.FeedbackFragment;
import com.coin.footer.fragment.MainFragment;
import com.coin.footer.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Fragment fragment;
    private CircleImageView fotoNav;
    private TextView namaNav;
    private TextView linkNav;
    private LinearLayout navHead;
    private View headerView;
    private AQuery aq;
    private SharedPreferences preferences;
    private Menu navMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navMenu = navigationView.getMenu();
        headerView = navigationView.getHeaderView(0);
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (preferences.contains("TOKEN")) {
                    Intent in = new Intent(MainActivity.this, UserActivity.class);
                    startActivity(in);
                } else {
                    Intent in = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(in);
                }
            }
        });

        preferences = getSharedPreferences("USER", MODE_PRIVATE);
        String nama = preferences.getString("FULLNAME", "Guest");
        aq = new AQuery(headerView);
        aq.id(R.id.namaNav).text(nama);
        if (!nama.equals("Gueast")) {
            aq.id(R.id.userLink).text(getString(R.string.nav_user));
        } else {
            aq.id(R.id.userLink).text(getString(R.string.nav_login));
        }

        fragment = new MainFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flContent, fragment);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        //creating fragment object
        fragment = null;
        
        if (id == R.id.nav_home) {
            fragment = new MainFragment();
        } else if (id == R.id.nav_about) {
            //TODO: pindah ke about
        } else if (id == R.id.nav_feedback) {
            fragment = new FeedbackFragment();
        } else if (id == R.id.nav_login) {
            Intent in = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(in);
        } else if (id == R.id.nav_logout) {
            preferences.edit().clear().commit();
            navMenu.findItem(R.id.nav_login).setVisible(false);
            navMenu.findItem(R.id.nav_logout).setVisible(true);
            recreate();
        }
        
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContent, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!preferences.getString("TOKEN", "0").equals("0")) {
            navMenu.findItem(R.id.nav_login).setVisible(false);
            navMenu.findItem(R.id.nav_logout).setVisible(true);
        }

        preferences = getSharedPreferences("USER", MODE_PRIVATE);
        String nama = preferences.getString("FULLNAME", "Guest");
        aq = new AQuery(headerView);
        aq.id(R.id.namaNav).text(nama);
        if (!nama.equals("Gueast")) {
            aq.id(R.id.userLink).text(getString(R.string.nav_user));
        } else {
            aq.id(R.id.userLink).text(getString(R.string.nav_login));
        }
        fragment = new MainFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flContent, fragment);
        ft.commit();
    }

}

package com.coin.footer.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.coin.footer.R;
import com.coin.footer.activity.RestoActivity;
import com.coin.footer.adapter.RestoListAdapter;
import com.coin.footer.dao.Restaurant;
import com.coin.footer.dao.RestaurantDisplay;
import com.coin.footer.services.APIService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PopularityFragment extends Fragment {
    
    private OnFragmentInteractionListener mListener;
    private ProgressBar loading;
    private ListView list;

    public PopularityFragment() {
        // Required empty public constructor
    }

    public static PopularityFragment newInstance(String param1, String param2) {
        PopularityFragment fragment = new PopularityFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_popularity, container, false);
        loading = (ProgressBar) view.findViewById(R.id.loadPopularity);
        list = (ListView) view.findViewById(R.id.listPopular);

        loading.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.loading),
                PorterDuff.Mode.MULTIPLY);
        getResto();
        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    /**
     * Fungsi buat tampilin loading
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getActivity().getResources().getInteger(android.R.integer.config_shortAnimTime);

            list.setVisibility(show ? View.GONE : View.VISIBLE);
            list.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    list.setVisibility(show ? View.GONE : View.VISIBLE);
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
            list.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void getResto() {
        showProgress(true);
        SharedPreferences preferences = this.getActivity().getSharedPreferences("USER",
                Context.MODE_PRIVATE);
        String token = preferences.getString("TOKEN", "0");
        Call<RestaurantDisplay> call = APIService.services.getAll(token);
        call.enqueue(new Callback<RestaurantDisplay>() {
            @Override
            public void onResponse(Call<RestaurantDisplay> call, Response<RestaurantDisplay> response) {
                if (response.isSuccessful()) {
                    final List<Restaurant> resto = response.body().getRestaurant();
                    try {
                        RestoListAdapter adapter = new RestoListAdapter(getContext(), resto);
                        list.setAdapter(adapter);
                    } catch (Exception e) {

                    }
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent in = new Intent(getContext(), RestoActivity.class);
                            in.putExtra("ID_RESTO", resto.get(position).getIDRestaurant());
                            startActivity(in);
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Connection Timed Out", Toast.LENGTH_SHORT).show();
                }
                try {

                    showProgress(false);
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<RestaurantDisplay> call, Throwable t) {
                Toast.makeText(getContext(), "Please check your connection",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}

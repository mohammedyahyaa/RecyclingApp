package com.example.android.ad;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.ad.DataBase.Executors;
import com.example.android.ad.DataBase.Holder;
import com.example.android.ad.model.adv_data;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AdDetailsFragment extends Fragment {
    private static final String DETAILS = "details";
    SharedPreferences sharedPreferences;
    String myUrl;

    adv_data data;
    Boolean isFav = false;
    Holder mDb;

    @BindView(R.id.adv_category_tv)
    TextView adv_category_tv;
    @BindView(R.id.adv_description_tv)
    TextView adv_description_tv;
    @BindView(R.id.adv_location_tv)
    TextView adv_location_tv;
    @BindView(R.id.adv_owner_name_tv)
    TextView adv_owner_name_tv;
    @BindView(R.id.adv_email_tv)
    TextView adv_email_tv;
    @BindView(R.id.btn_email)
    Button email;
    @BindView(R.id.btn_phone)
    Button phone;
    @BindView(R.id.adv_salary_tv_frag)
    TextView adv_salary_tv;
    @BindView(R.id.adv_date_tv_frag)
    TextView adv_date_tv;
    @BindView(R.id.imageView_adFrag)
    ImageView imageViewFrag;

    @BindView(R.id.fav)
    ImageButton imageFav;

    public AdDetailsFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ad_details, container, false);
        ButterKnife.bind(this, rootView);
        Context c = getActivity().getApplicationContext();


        Intent i = getActivity().getIntent();
        data = i.getParcelableExtra(DETAILS);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
       // myUrl = sharedPreferences.getString("pop", data.getPhotoUrl());


        //Picasso.with(getContext()).load(myUrl).into(imageViewFrag);
        imageViewFrag.setVisibility(View.VISIBLE);
        Glide.with(getContext()).load(data.getPhotoUrl()).into(imageViewFrag);

        adv_category_tv.setText(data.getAdv_category());
        adv_description_tv.setText(data.getAdv_description());
        adv_location_tv.setText(data.getLocation());
        adv_owner_name_tv.setText(data.getOwner_name());
        adv_email_tv.setText(data.getEmail());
        adv_salary_tv.setText(String.valueOf(data.getPrice()) + "");
        adv_date_tv.setText(data.getDate());


        mDb = Holder.getInstance(getContext());

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                adv_data adInFav = mDb.movieDao().selectAdByKey(data.getKey());
                if (adInFav != null) {
                    isFav = true;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imageFav.setBackgroundColor(getResources().getColor(R.color.amber));
                            Log.d("gida", "fav");
                        }
                    });
                }

            }
        });


        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:" + data.getEmail()));
                startActivity(Intent.createChooser(emailIntent, "Send feedback"));
            }
        });
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + data.getPhone()));
                startActivity(intent);
            }
        });
        imageFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favMovies(v);
            }
        });


        return rootView;


    }

    public void favMovies(View view) {

        isFav = !isFav;


        if (isFav) {

            Executors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDb.movieDao().insertAd(data);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imageFav.setBackgroundColor(getResources().getColor(R.color.amber));

                            Log.d("gida", "fav");

                        }
                    });

                }
            });
            Toast.makeText(getContext(), "added to favourites", Toast.LENGTH_SHORT).show();
        } else {
            Executors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDb.movieDao().deleteAd(data);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imageFav.setBackgroundColor(Color.WHITE);
                            Log.d("gida", "deletedFromFav");

                        }
                    });
                }
            });
            Toast.makeText(getContext(), "removed from favourites", Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


}

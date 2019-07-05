package com.example.android.ad;

import android.content.Intent;
import android.graphics.Movie;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.android.ad.model.adv_data;
import com.squareup.picasso.Picasso;

public class AdDetails extends AppCompatActivity {
    private static final String DETAILS = "details";
    adv_data data;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_details);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //imageView = findViewById(R.id.image_view_Ad_Activity);

        Intent i = getIntent();
        data = i.getParcelableExtra(DETAILS);

       // Picasso.with(getApplicationContext()).load(data.getPhotoUrl()).into(imageView);


        AdDetailsFragment detailsFragment = new AdDetailsFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().add(R.id.containerFrag, detailsFragment).commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

}

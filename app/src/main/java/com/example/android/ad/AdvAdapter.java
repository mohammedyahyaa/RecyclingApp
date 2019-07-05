package com.example.android.ad;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.ad.model.adv_data;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdvAdapter extends ArrayAdapter<adv_data> {
    private adv_data advData;


    public AdvAdapter(Context context, int resource, List<adv_data> objects) {
        super(context, resource, objects);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_ads, parent, false);
        }

        ImageView photoImageView = convertView.findViewById(R.id.photoImageViewAD);
        TextView adtextView = convertView.findViewById(R.id.adNameTextViewAD);
        TextView ownertextView = convertView.findViewById(R.id.ownerNameTextViewAD);
        TextView locationOfowner = convertView.findViewById(R.id.location_ad_textview_AD);
        TextView salary = convertView.findViewById(R.id.textViewAdSalary);

        advData = getItem(position);


     //   photoImageView.setVisibility(View.VISIBLE);
        Glide.with(photoImageView.getContext())
                .load(advData.getPhotoUrl())
                .into(photoImageView);


        adtextView.setText(advData.getAdv_name());
        locationOfowner.setText(advData.getLocation());
        ownertextView.setText(advData.getOwner_name());
        salary.setText(String.valueOf(advData.getPrice()) + "ج");
        return convertView;
    }

}

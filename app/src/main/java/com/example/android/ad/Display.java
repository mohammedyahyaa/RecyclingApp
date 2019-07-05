package com.example.android.ad;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Query;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.ad.DataBase.MainViewModel;
import com.example.android.ad.model.adv_data;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Display extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private static final String DETAILS = "details";
    private final int PICK_IMAGE_REQUEST = 71;
    public static SharedPreferences sharedPreferences;



    public boolean isConnected;

    Toast toast;
    private ImageButton imageButton;
    private ImageView imageView;
    private Uri imagePath;

    private ListView mMessageListView;
    private AdvAdapter mMessageAdapter;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference, connectedRef;



    public static String cityFilter_choice;
    private Spinner cityFilter_spinner;

    private ImageButton filterImageButton;

    private Spinner order_spinner;
    public static String orderFilter_choice;

    //private Spinner filter_spinner;
    public static String filter_choice;



    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private ValueEventListener mValueEventListener;

    private String Downer_name;
    public static final String ANONYMOUS = "anonymous";
    private static final int RC_SIGN_IN = 1;

    private FloatingActionButton fab;
    com.google.firebase.database.Query query;


    //    //Dialog Content
//    @BindView(R.id.buttonUpdate)
    Button buttonUpdate;
    //    @BindView(R.id.editText_ad_ownerName_update)
    EditText editText_ownerNameNew;
    //    @BindView(R.id.editText_ad_des_update)
    EditText editText_desNew;
    //    @BindView(R.id.editText_ad_ownerMail_update)
    EditText editText_mailNew;
    //    @BindView(R.id.editText_ad_phone_update)
    EditText editText_phoneNew;
    //    //    @BindView(R.id.imageButton_update)
////    ImageButton imageButton_new;
////    @BindView(R.id.imageView_update)
////    ImageView imageView_new;
//    @BindView(R.id.editText_price_id_update)
    EditText editText_priceNew;
    //    @BindView(R.id.editText_adName_update)
    EditText editText_adNameNew;
    //    @BindView(R.id.spinner_city_update)
    Spinner spinner_city_update;
    private String city_choice_update;

    //    @BindView(R.id.category_spinner_id_update)
    Spinner spinner_category_update;
    private String category_choice_update;

    String strDate;
    String keyOfCurrentNode;
    private static Spinner filter_spinnerr;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);


       // filter_spinner = findViewById(R.id.filter_spinner);
        filterImageButton = findViewById(R.id.filter_image);
        filterImageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                showFilterDialog();

            }
        });




        order_spinner = findViewById(R.id.order_spinner);

                ArrayAdapter<CharSequence> order_adapter = ArrayAdapter.createFromResource(this,
                R.array.order, android.R.layout.simple_spinner_item);
        order_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        order_spinner.setAdapter(order_adapter);


        order_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                orderFilter_choice = parentView.getItemAtPosition(position).toString();
                if (mMessageAdapter.isEmpty()) {

                    order();

                } else {

                    mMessageAdapter.clear();
                    order();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                //Toast.makeText(Display.this, "???? ?? ?????? ????? ?????? ", Toast.LENGTH_SHORT).show();
            }

        });



        mMessageListView = findViewById(R.id.messageListView);




        mFirebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        Downer_name = ANONYMOUS;


        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("Advertisments");


        connectedRef = mFirebaseDatabase.getReference(".info/connected");


        keyOfCurrentNode = mMessagesDatabaseReference.push().getKey();
        Log.e("gida", keyOfCurrentNode);


        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Date date = new Date();
        strDate = dateFormat.format(date).toString();


        // Initialize message ListView and its adapter
        List<adv_data> friendlyMessages = new ArrayList<>();
        mMessageAdapter = new AdvAdapter(this, R.layout.item_ads, friendlyMessages);
        mMessageListView.setAdapter(mMessageAdapter);

        mMessageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adv_data selected_ad = mMessageAdapter.getItem(position);
                Intent startActivityAdDetails = new Intent(Display.this, AdDetails.class);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("pop", selected_ad.getPhotoUrl());
                editor.commit();
                startActivityAdDetails.putExtra(DETAILS, selected_ad);
                startActivity(startActivityAdDetails);

            }
        });
        EnableUpdateAndDelete();


        //navigation
        DrawerLayout drawer = findViewById(R.id.drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //Fab
        fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Display.this, MainActivity.class);
                startActivity(intent);
            }
        });


        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    onSignInInitialize(user.getDisplayName());
                    //AppState();

                } else {
                    onSignedOutCleanUp();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(Arrays.asList(new AuthUI.IdpConfig.GoogleBuilder().build(),
                                            new AuthUI.IdpConfig.EmailBuilder().build()
                                    ))
                                    .build(),
                            RC_SIGN_IN);
                }

            }
        };


    }

    private void EnableUpdateAndDelete() {
        mMessageListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                final adv_data selected_ad = mMessageAdapter.getItem(position);
                if (selected_ad.getUserId().equals(firebaseAuth.getCurrentUser().getUid())) {
                    showUpdateDialog(selected_ad.getAdv_name(), selected_ad.getAdv_description(), selected_ad.getEmail(), selected_ad.getPhone(), selected_ad.getPrice(), selected_ad.getOwner_name(), selected_ad.getPhotoUrl(), selected_ad.getUserId(), selected_ad.getKey());

                }
                return true;
            }
        });
    }

    private void AppState() {
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (!connected) {
                    Toast.makeText(Display.this, R.string.offline, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    private void OnAttachDatabaseReadListener() {


        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mMessageAdapter.clear();

                for (DataSnapshot dSnapshot : dataSnapshot.getChildren()) {

                    adv_data adv_data = dSnapshot.getValue(adv_data.class);

                    mMessageAdapter.add(adv_data);

                    mMessageAdapter.notifyDataSetChanged();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mMessagesDatabaseReference.addValueEventListener(mValueEventListener);
    }


    private void OnAttachDatabaseReadListenerFilter() {


        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mMessageAdapter.clear();


                for (DataSnapshot dSnapshot : dataSnapshot.getChildren()) {

                    if (filter_choice.equals("الكل") && dSnapshot.child("location").getValue().toString().equals(cityFilter_choice)) {

                        adv_data adv_data = dSnapshot.getValue(adv_data.class);

                        mMessageAdapter.add(adv_data);

                    }

                    if (dSnapshot.child("adv_category").getValue().toString().equals(filter_choice) && dSnapshot.child("location").getValue().toString().equals(cityFilter_choice)) {

                        adv_data adv_data = dSnapshot.getValue(adv_data.class);

                        mMessageAdapter.add(adv_data);

                    }


                    mMessageAdapter.notifyDataSetChanged();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mMessagesDatabaseReference.addValueEventListener(mValueEventListener);

    }

    private void OnDetachDatabaseReadListener() {

        if (mValueEventListener != null) {
            mMessagesDatabaseReference.removeEventListener(mValueEventListener);
            mValueEventListener = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth.addAuthStateListener(authStateListener);
        OnAttachDatabaseReadListener();


    }

    @Override
    protected void onPause() {
        super.onPause();
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
        OnDetachDatabaseReadListener();
        mMessageAdapter.clear();

    }

    private void onSignInInitialize(String owner_name) {

        Downer_name = owner_name;

    }

    private void onSignedOutCleanUp() {
        Downer_name = ANONYMOUS;

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.sign_out_menu:
                AuthUI.getInstance().signOut(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

            mMessageAdapter.clear();
            OnAttachDatabaseReadListener();


        } else if (id == R.id.nav_person) {
            mMessagesDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mMessageAdapter.clear();

                    for (DataSnapshot dSnapshot : dataSnapshot.getChildren()) {


                        if (dSnapshot.child("userId").getValue().toString().equals(firebaseAuth.getCurrentUser().getUid())) {

                            adv_data adv_data = dSnapshot.getValue(adv_data.class);

                            mMessageAdapter.add(adv_data);

                        }


                        mMessageAdapter.notifyDataSetChanged();
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        } else if (id == R.id.nav_fav) {


            final MainViewModel model = ViewModelProviders.of(this).get(MainViewModel.class);

            model.getAds().observe(this, new Observer<adv_data[]>() {
                @Override
                public void onChanged(@Nullable adv_data[] ads) {
                    Log.d("gida", "Updating list of tasks from LiveData in ViewModel");
                    mMessageAdapter.clear();
                    mMessageAdapter.addAll(ads);


                }
            });


        } else if (id == R.id.nav_add) {
            Intent startMainActivity = new Intent(Display.this, MainActivity.class);
            startActivity(startMainActivity);


        }

        DrawerLayout drawer = findViewById(R.id.drawer);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void showUpdateDialog(String advName, String advDes, String advEmail, String advPhone, double advPrice, String advOwner, final String path, final String ID, final String keyOfNode) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);


        View view = getLayoutInflater().inflate(R.layout.update_dialog_content, null);


        //Dialog Content
        Button buttonUpdate = view.findViewById(R.id.buttonUpdate);
        Button buttonDelete = view.findViewById(R.id.buttonDelete);
        //CHoosing IMAGE
        imageButton = view.findViewById(R.id.imageButton_update);
        imageView = view.findViewById(R.id.imageView_update);

        editText_ownerNameNew = view.findViewById(R.id.editText_ad_ownerName_update);
        editText_desNew = view.findViewById(R.id.editText_ad_des_update);
        editText_mailNew = view.findViewById(R.id.editText_ad_ownerMail_update);
        editText_phoneNew = view.findViewById(R.id.editText_ad_phone_update);
        editText_priceNew = view.findViewById(R.id.editText_price_id_update);
        editText_adNameNew = view.findViewById(R.id.editText_adName_update);
        spinner_city_update = view.findViewById(R.id.spinner_city_update);
        spinner_category_update = view.findViewById(R.id.category_spinner_id_update);



        editText_ownerNameNew.setText(advOwner);
        editText_desNew.setText(advDes);
        editText_mailNew.setText(advEmail);
        editText_phoneNew.setText(advPhone);
        editText_priceNew.setText(String.valueOf(advPrice));
        editText_adNameNew.setText(advName);
        Glide.with(this).load(path).into(imageView);


        builder.setView(view);
        builder.setTitle(getString(R.string.updating_adv) + " " + advName);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });



        //   ButterKnife.bind(view);
        spinnersComeToPlay();

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();


        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String adNameNew = editText_adNameNew.getText().toString();
                String desNew = editText_desNew.getText().toString();
                String mailNew = editText_mailNew.getText().toString();
                String ownerNameNew = editText_ownerNameNew.getText().toString();
                String phoneNew = editText_phoneNew.getText().toString();
                double priceNew = Double.parseDouble(editText_priceNew.getText().toString());

                updateContent(adNameNew, category_choice_update, desNew, city_choice_update, mailNew, ownerNameNew, phoneNew, imagePath.toString(), ID, strDate, priceNew, keyOfNode);
                alertDialog.dismiss();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteItem(keyOfNode);
            }
        });


    }

    private boolean updateContent(String adNameNew, String catogery_item, String desNew, String city_choice, String mailNew, String ownerNameNew, String phoneNew, String path, String ID, String strDate, Double priceNew, String key) {

        adv_data data = new adv_data(adNameNew, catogery_item, desNew, city_choice, mailNew, ownerNameNew, phoneNew, path, ID, strDate, priceNew, key);
        mMessagesDatabaseReference.child(key).setValue(data);
        Toast.makeText(getApplicationContext(), "updated successfully", Toast.LENGTH_SHORT).show();
        return true;
    }


    private void deleteItem(String keyOfNode) {

        mMessagesDatabaseReference.child(keyOfNode).removeValue();
        Toast.makeText(getApplicationContext(), "deleted successfully", Toast.LENGTH_SHORT).show();
        return;

    }

    private void spinnersComeToPlay() {

        ArrayAdapter<CharSequence> des_adapter = ArrayAdapter.createFromResource(this,
                R.array.Recycling_category, android.R.layout.simple_spinner_item);
        des_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_category_update.setAdapter(des_adapter);


        spinner_category_update.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                category_choice_update = parentView.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                Toast.makeText(getApplicationContext(), "لابد من اختيار تصنيف للمنتج ", Toast.LENGTH_SHORT).show();
            }

        });
        spinner_category_update.setPrompt(getString(R.string.spinner_titleDes));


        ArrayAdapter<CharSequence> city_adapter = ArrayAdapter.createFromResource(this,
                R.array.city, android.R.layout.simple_spinner_item);
        city_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_city_update.setAdapter(city_adapter);


        spinner_city_update.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                city_choice_update = parentView.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                Toast.makeText(getApplicationContext(), "لابد من اختيار مدينتك ", Toast.LENGTH_SHORT).show();
            }

        });
        spinner_city_update.setPrompt(getString(R.string.spinner_titleLocation));


    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_photo)), PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imagePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showFilterDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);


        View view = getLayoutInflater().inflate(R.layout.filter_layout, null);


        filter_spinnerr = view.findViewById(R.id.filter_spinner);
        cityFilter_spinner = view.findViewById(R.id.city_filter_spinner);


        ArrayAdapter<CharSequence> filter_adapter = ArrayAdapter.createFromResource(this,
                R.array.filter, android.R.layout.simple_spinner_item);
        filter_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filter_spinnerr.setAdapter(filter_adapter);


        ArrayAdapter<CharSequence> cityFilter_adapter = ArrayAdapter.createFromResource(this,
                R.array.city, android.R.layout.simple_spinner_item);
        filter_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cityFilter_spinner.setAdapter(cityFilter_adapter);


        filter_spinnerr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                filter_choice = parentView.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                //Toast.makeText(Display.this, "???? ?? ?????? ????? ?????? ", Toast.LENGTH_SHORT).show();
            }

        });


        cityFilter_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                cityFilter_choice = parentView.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                //Toast.makeText(Display.this, "???? ?? ?????? ????? ?????? ", Toast.LENGTH_SHORT).show();
            }

        });


        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.filter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        if (mMessageAdapter.isEmpty()) {

                            OnAttachDatabaseReadListenerFilter();

                        } else {

                            mMessageAdapter.clear();
                            OnAttachDatabaseReadListenerFilter();
                        }


                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                    }
                });


        builder.setView(view);
        builder.setTitle(R.string.filter_builder_title);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();


    }


    private void order() {


        if (orderFilter_choice.equals("الأقل سعر")) {
            query = mMessagesDatabaseReference.orderByChild("price");
        } else if (orderFilter_choice.equals("الأعلي سعرا")) {

            query = mMessagesDatabaseReference.orderByChild("phone");

            mMessageListView.setAdapter(mMessageAdapter);

        } else if (orderFilter_choice.equals("رتب")) {

            query = mMessagesDatabaseReference.orderByPriority();
        } else if (orderFilter_choice.equals("الاحدث")) {
            query = mMessagesDatabaseReference.orderByChild("date");
        }


        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mMessageAdapter.clear();


                for (DataSnapshot dSnapshot : dataSnapshot.getChildren()) {


                    adv_data adv_data = dSnapshot.getValue(adv_data.class);

                    mMessageAdapter.add(adv_data);


                    mMessageAdapter.notifyDataSetChanged();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        query.addValueEventListener(mValueEventListener);


    }


}

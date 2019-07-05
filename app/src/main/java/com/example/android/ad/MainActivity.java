package com.example.android.ad;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.ad.model.adv_data;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    public static final String ANONYMOUS = "anonymous";
    private Button add_button;
    private Spinner category_spinner;
    private Spinner city_spinner;
    private EditText adv_name_EditTxt;
    private EditText adv_description_EditTxt;
    private EditText owner_mail_EditTxt;
    private EditText phone_EditTxt;
    private ProgressBar mProgressBar;
    private EditText adv_ownerName;
    private EditText price_EditText;
    public String key;
    private final int PICK_IMAGE_REQUEST = 71;
    private String catogery_item;
    private String city_choice;
    private ImageButton imageButton;
    private ImageView imageView;
    private Uri filePath;
    private String strDate;
    private String mKeyGenerated;


    private String Downer_name;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private FirebaseStorage storage;
    private StorageReference storageReference;

    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;

    String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();

        databaseReference = firebaseDatabase.getReference().child("Advertisments");
        storageReference = storage.getReference();

        currentUserId = firebaseAuth.getCurrentUser().getUid();

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Date date = new Date();
        strDate = dateFormat.format(date);

        key = databaseReference.getKey();


        Downer_name = ANONYMOUS;
        add_button = findViewById(R.id.add_button_id);
        adv_name_EditTxt = findViewById(R.id.adv_name_id);
        adv_description_EditTxt = findViewById(R.id.adv_description_id);
        owner_mail_EditTxt = findViewById(R.id.owner_mail_id);
        phone_EditTxt = findViewById(R.id.phone_id);
        adv_ownerName = findViewById(R.id.adv_ownerName_id);
        mProgressBar = findViewById(R.id.progressBar);
        imageButton = findViewById(R.id.photoPickerButton);
        imageView = findViewById(R.id.imgView);
        price_EditText = findViewById(R.id.price_EditText_id);


        if (adv_name_EditTxt.getText().toString().length() == 0)
            adv_name_EditTxt.setError(null);
        else {
            adv_name_EditTxt.setText(null);
        }
        if (adv_description_EditTxt.getText().toString().length() == 0)
            adv_description_EditTxt.setError(getString(R.string.validate_des));
        else {
            adv_description_EditTxt.setText(null);
        }
        if (owner_mail_EditTxt.getText().toString().length() == 0)
            owner_mail_EditTxt.setError(getString(R.string.validate_mail));
        else {

            owner_mail_EditTxt.setText(null);
        }


        if (phone_EditTxt.getText().toString().length() == 0)
            phone_EditTxt.setError(getString(R.string.validate_phone));
        else {
            phone_EditTxt.setText(null);
        }

        if (adv_ownerName.getText().toString().length() == 0)
            adv_ownerName.setError(getString(R.string.validate_owner));
        else {
            adv_ownerName.setText(null);
        }


        mProgressBar.setVisibility(ProgressBar.INVISIBLE);


        category_spinner = (Spinner) findViewById(R.id.category_spinner_id);


        adv_name_EditTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    add_button.setEnabled(true);
                } else {
                    add_button.setEnabled(false);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        adv_name_EditTxt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});


        adv_description_EditTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    add_button.setEnabled(true);
                } else {
                    add_button.setEnabled(false);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        adv_description_EditTxt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});


        owner_mail_EditTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    add_button.setEnabled(true);
                } else {
                    add_button.setEnabled(false);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        owner_mail_EditTxt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});


        phone_EditTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    add_button.setEnabled(true);

                } else {
                    add_button.setEnabled(false);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        phone_EditTxt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});


        adv_ownerName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    add_button.setEnabled(true);
                } else {
                    add_button.setEnabled(false);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        adv_ownerName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});


        ArrayAdapter<CharSequence> des_adapter = ArrayAdapter.createFromResource(this,
                R.array.Recycling_category, android.R.layout.simple_spinner_item);
        des_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category_spinner.setAdapter(des_adapter);


        category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                catogery_item = parentView.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                Toast.makeText(MainActivity.this, "لابد من اختيار تصنيف للمنتج ", Toast.LENGTH_SHORT).show();
            }

        });
        category_spinner.setPrompt(getString(R.string.spinner_titleDes));

        city_spinner = findViewById(R.id.city_spinner_id);

        ArrayAdapter<CharSequence> city_adapter = ArrayAdapter.createFromResource(this,
                R.array.city, android.R.layout.simple_spinner_item);
        city_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        city_spinner.setAdapter(city_adapter);


        city_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                city_choice = parentView.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                Toast.makeText(MainActivity.this, "لابد من اختيار مدينتك ", Toast.LENGTH_SHORT).show();
            }

        });
        city_spinner.setPrompt(getString(R.string.spinner_titleLocation));

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });


        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (imageView.getDrawable() == null || adv_name_EditTxt.getText().toString().equals("") || adv_description_EditTxt.getText().toString().equals("") || adv_ownerName.getText().toString().equals("") || owner_mail_EditTxt.getText().toString().equals("") || phone_EditTxt.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, R.string.validate_all_data, Toast.LENGTH_LONG).show();
                    add_button.setEnabled(false);

                } else {

                    if (isEmailValid(owner_mail_EditTxt.getText().toString())) {

                        uploadImage();
                        mKeyGenerated = databaseReference.push().getKey();
                        adv_data data = new adv_data(adv_name_EditTxt.getText().toString(), catogery_item, adv_description_EditTxt.getText().toString(), city_choice, owner_mail_EditTxt.getText().toString(), adv_ownerName.getText().toString(), phone_EditTxt.getText().toString(), filePath.toString(), currentUserId, strDate, Double.parseDouble(price_EditText.getText().toString()), mKeyGenerated);
                        databaseReference.child(mKeyGenerated).setValue(data);

                        adv_name_EditTxt.setText("");
                        category_spinner.clearDisappearingChildren();
                        adv_description_EditTxt.setText("");
                        city_spinner.clearDisappearingChildren();
                        owner_mail_EditTxt.setText("");
                        adv_ownerName.setText("");
                        phone_EditTxt.setText("");
                    } else {
                        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                        alertDialog.setTitle(getString(R.string.warning));
                        alertDialog.setMessage(getString(R.string.isMailRight));
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                        add_button.setEnabled(false);
                    }
                }
            }
        });


    }

    public boolean isEmailValid(String email) {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches())
            return true;
        else
            return false;
    }


    private boolean isValidPhone() {
        if (android.util.Patterns.PHONE.matcher(phone_EditTxt.getText().toString()).matches()) {
            Toast.makeText(this, R.string.valid, Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(getApplicationContext(), R.string.isPhoneValid, Toast.LENGTH_SHORT).show();
            return false;
        }

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }


    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_photo)), PICK_IMAGE_REQUEST);
    }

    private void uploadImage() {

        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle(getString(R.string.uploading));
            progressDialog.show();

            StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, R.string.adCompletion, Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, getString(R.string.failed) + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage(getString(R.string.adInProgress) + (int) progress + "%");
                        }
                    });
        }
    }


}

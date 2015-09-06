package com.designdemo.uaha;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.support.android.designlibdemo.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserActivity extends AppCompatActivity {
    private static final String TAG = "UserActivity";
    private Activity mActivity;

    private DrawerLayout drawerLayout;
    private EditText nameEnterField;
    private EditText phoneEnterField;
    private FloatingActionButton fab;
    private Button picButton;

    private CircleImageView profileImageView;

    private AlertDialog photoDialog;

    public static final int TAKE_PICTURE_CODE = 23423;
    public static final int CHOOSE_PICTURE_CODE = 453;
    private String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        mActivity = this;

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        setupViews();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "Request Code = " + requestCode);

        if(resultCode == RESULT_OK){
            if(requestCode == TAKE_PICTURE_CODE){
                photoDialog.dismiss();

                // We've created this photo path when we launched the camera intent
                Uri selectedImage = Uri.parse(mCurrentPhotoPath);
                Glide.with(this).load(selectedImage).into(profileImageView);
                PrefsUtil.saveProfilePic(this, selectedImage);

            }else if (requestCode == CHOOSE_PICTURE_CODE){
                photoDialog.dismiss();

                // Uri comes back from the Gallery
                Uri selectedImage = data.getData();
                Glide.with(this).load(selectedImage).into(profileImageView);
                PrefsUtil.saveProfilePic(this, selectedImage);
            }
        }
    }

    private void setupViews() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        nameEnterField = (EditText) findViewById(R.id.name_edit);
        phoneEnterField = (EditText) findViewById(R.id.phone_edit);

        profileImageView = (CircleImageView)findViewById(R.id.profile_image_view);

        Uri imageUri = PrefsUtil.getProfilePic(this);
        if(imageUri != null){
            Glide.with(this).load(imageUri).centerCrop().into(profileImageView);
        }else {
            Glide.with(this).load(R.drawable.ic_account_lg).centerCrop().into(profileImageView);
        }

        //Format phone number as user is typing
        phoneEnterField.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        picButton = (Button) findViewById(R.id.profile_pic_button);
        picButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPictureDialog();
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Validate values
                int nameLen = nameEnterField.getText().length();
                View mainView = (View) findViewById(R.id.main_content);

                if (nameLen < 4) {
                    nameEnterField.setError("At least 4 characters");
                    Snackbar.make(mainView, "Name input error", Snackbar.LENGTH_SHORT)
                            .show();
                }

                int phoneLen = phoneEnterField.getText().length();
                if (phoneLen != 14) {
                    phoneEnterField.setError("Invalid Phone");




                    Snackbar.make(mainView, "Phone input error",
                            Snackbar.LENGTH_SHORT)
                            .show();



                }

                //Validation successful, save values and message user

                // Save original Values before sending, in-case user changes their mind
                final String beforeName = PrefsUtil.getName(mActivity.getApplicationContext());
                final long beforePhone = PrefsUtil.getPhone(mActivity.getApplicationContext());

                // Store new values
                final String nameToSet = nameEnterField.getText().toString();
                String formattedNum = PhoneNumberUtils.stripSeparators(phoneEnterField.getText().toString());
                final long phoneToSet = Long.valueOf(formattedNum);

                PrefsUtil.setProfile(mActivity.getApplicationContext(), nameToSet, phoneToSet);

                Snackbar.make(mainView, R.string.favorite_confirm, Snackbar.LENGTH_LONG)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Reset to original
                                boolean complete = PrefsUtil.setProfile(mActivity.getApplicationContext(), beforeName, beforePhone);
                                if (complete) {
                                    setPhoneNameValues();
                                }
                            }
                        })
                        .show();
            }
        });

        // Set initial values from Prefs
        setPhoneNameValues();
    }

    private void setPhoneNameValues() {
        String name = PrefsUtil.getName(this);
        if (!name.equals(PrefsUtil.PREFS_NAME_UNSET)) {
            nameEnterField.setText(name);
        }

        final long phone = PrefsUtil.getPhone(this);
        if (phone != 0) {
            phoneEnterField.setText(phone + "");
        }
    }

    private void setPictureDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        LayoutInflater inflater = mActivity.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_picture, null);

        dialogView.findViewById(R.id.buttonTakePic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        dialogView.findViewById(R.id.buttonChoosePic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, CHOOSE_PICTURE_CODE);
            }
        });

        builder.setView(dialogView);
        builder.setTitle(mActivity.getString(R.string.picture_dialog_title));
        builder.setCancelable(true);
        builder.setPositiveButton(mActivity.getString(R.string.picture_dialog_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Send the phone number to get validated
                Log.d("Dialog", "The positive button was pressed");
            }
        });

        photoDialog = builder.create();
        photoDialog.show();
    }

    // https://developer.android.com/training/camera/photobasics.html
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    // https://developer.android.com/training/camera/photobasics.html
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.e(TAG, "Error: "+ex.getMessage());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, TAKE_PICTURE_CODE);
            }
        }
    }

    private void setupDrawerContent(final NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        switch (menuItem.getItemId()) {
                            case R.id.nav_home:
                                Intent osIntent = new Intent(getApplicationContext(), MainActivity.class);
                                osIntent.putExtra(MainActivity.EXTRA_FRAG_TYPE, MainActivity.OS_FRAG);
                                startActivity(osIntent);
                                return true;
                            case R.id.nav_devices:
                                Intent deviceIntent = new Intent(getApplicationContext(), MainActivity.class);
                                deviceIntent.putExtra(MainActivity.EXTRA_FRAG_TYPE, MainActivity.DEVICE_FRAG);
                                startActivity(deviceIntent);
                                return true;
                            case R.id.nav_favorites:
                                Intent favIntent = new Intent(getApplicationContext(), MainActivity.class);
                                favIntent.putExtra(MainActivity.EXTRA_FRAG_TYPE, MainActivity.FAV_FRAG);
                                startActivity(favIntent);
                                return true;
                            case R.id.nav_userinfo:
                                drawerLayout.closeDrawers();
                                return true;
                            case R.id.nav_link1:
                                Intent browser1 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.android.com/"));
                                startActivity(browser1);
                                return true;
                            case R.id.nav_link2:
                                Intent browser2 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://developer.android.com/"));
                                startActivity(browser2);
                                return true;
                            default:
                                return true;
                        }
                    }
                });
    }
}

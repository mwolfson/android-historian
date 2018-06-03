package com.designdemo.uaha.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.designdemo.uaha.ui.MainActivity;
import com.designdemo.uaha.util.PrefsUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.support.android.designlibdemo.R;

public class UserActivity extends AppCompatActivity {
    private Activity mainActivity;

    private DrawerLayout drawerLayout;
    private AppCompatEditText nameEnterField;
    private AppCompatEditText phoneEnterField;
    private FloatingActionButton fab;
    private Button picButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        mainActivity = this;

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        setupViews();

        NavigationView navigationView = findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_actions, menu);
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

    private void setupViews() {
        drawerLayout = findViewById(R.id.drawer_layout);
        nameEnterField = findViewById(R.id.name_edit);
        phoneEnterField = findViewById(R.id.phone_edit);

        //Format phone number as user is typing
        phoneEnterField.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        picButton = findViewById(R.id.profile_pic_button);
        picButton.setOnClickListener(v -> {
            setPictureDialog();
        });

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            //Validate values
            int nameLen = nameEnterField.getText().length();
            View mainView = findViewById(R.id.main_content);

            if (nameLen < 4) {
                nameEnterField.setError(getString(R.string.at_least_4_char));
                nameEnterField.requestFocus();
                Snackbar.make(mainView, getString(R.string.name_input_error), Snackbar.LENGTH_SHORT).show();
                return;
            }

            int phoneLen = phoneEnterField.getText().length();
            if (phoneLen != 14) {
                phoneEnterField.setError(getString(R.string.invalid_phone));
                phoneEnterField.requestFocus();
                Snackbar.make(mainView, getString(R.string.phone_input_error),Snackbar.LENGTH_SHORT).show();
                return;
            }

            // Save original Values before sending, in-case user changes their mind
            final String beforeName = PrefsUtil.getName(mainActivity.getApplicationContext());
            final long beforePhone = PrefsUtil.getPhone(mainActivity.getApplicationContext());

            // Store new values
            final String nameToSet = nameEnterField.getText().toString();
            String formattedNum = PhoneNumberUtils.stripSeparators(phoneEnterField.getText().toString());
            final long phoneToSet = Long.valueOf(formattedNum);

            PrefsUtil.setProfile(mainActivity.getApplicationContext(), nameToSet, phoneToSet);

            Snackbar.make(mainView, getString(R.string.profile_saved_confirm), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.undo), view -> {
                        // Reset to original
                        boolean complete = PrefsUtil.setProfile(mainActivity.getApplicationContext(), beforeName, beforePhone);
                        if (complete) {
                            setPhoneNameValues();
                        }
                    })
                    .show();
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
        AlertDialog photoDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        LayoutInflater inflater = mainActivity.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_picture, null);
        builder.setView(dialogView);
        builder.setTitle(mainActivity.getString(R.string.picture_dialog_title));
        builder.setCancelable(true);
        builder.setPositiveButton(mainActivity.getString(R.string.picture_dialog_button), (dialog, which) -> {
            Log.d("Dialog", "The positive button was pressed");
        });

        final SwitchCompat prefSwitch = (SwitchCompat) dialogView.findViewById(R.id.photo_pref_switch);
        prefSwitch.setChecked(true);
        prefSwitch.setOnClickListener(v -> {
            if (prefSwitch.isChecked()) {
                Log.d("Dialog", "The Photo switch was enabled");
            } else {
                Log.d("Dialog", "The Photo switch was disabled");
            }
        });

        photoDialog = builder.create();
        photoDialog.show();
    }

    private void setupDrawerContent(final NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
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
                            Intent browser2 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://material.io/"));
                            startActivity(browser2);
                            return true;
                        default:
                            return true;
                    }
                });
    }
}

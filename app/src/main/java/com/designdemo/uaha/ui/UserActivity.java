package com.designdemo.uaha.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.designdemo.uaha.util.PrefsUtil;
import com.designdemo.uaha.util.UiUtil;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.support.android.designlibdemo.R;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class UserActivity extends AppCompatActivity {
    private Activity mainActivity;

    private DrawerLayout drawerLayout;
    private AppCompatEditText nameEnterField;
    private AppCompatEditText phoneEnterField;
    private FloatingActionButton fab;
    private Button picButton;

    private BottomSheetBehavior bottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        mainActivity = this;

        BottomAppBar bottomAppBar = findViewById(R.id.bottom_appbar);
        setSupportActionBar(bottomAppBar);
        bottomAppBar.replaceMenu(R.menu.profile_actions);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        setupViews();
        setupTextScaleDialog();
        setupChips();

        NavigationView navigationView = findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
    }

    private void setupChips() {
        Chip chipEntry1 = findViewById(R.id.chip_entry1);
        chipEntry1.setOnCloseIconClickListener(view -> {
            view.setVisibility(View.GONE);
        });

        Chip chipEntry2 = findViewById(R.id.chip_entry2);
        chipEntry2.setOnCloseIconClickListener(view -> {
            view.setVisibility(View.GONE);
        });

        Chip chipEntry3 = findViewById(R.id.chip_entry3);
        chipEntry3.setOnCloseIconClickListener(view -> {
            view.setVisibility(View.GONE);
        });

        Chip chipEntry4 = findViewById(R.id.chip_entry4);
        chipEntry4.setOnCloseIconClickListener(view -> {
            view.setVisibility(View.GONE);
        });

        Chip chipEntry5 = findViewById(R.id.chip_entry5);
        chipEntry5.setOnCloseIconClickListener(view -> {
            view.setVisibility(View.GONE);
        });

        ChipGroup filter1Group = findViewById(R.id.filter1_group);
        filter1Group.setOnCheckedChangeListener((chipGroup, i) -> {
            switch (i) {
                case R.id.choice_item1:
                    Log.d("MSW", "Filter1 Item 1");
                    break;
                case R.id.choice_item2:
                    Log.d("MSW", "Filter1 Item 2");
                    break;
                case R.id.choice_item3:
                    Log.d("MSW", "Filter1 Item 3");
                    break;
            }
        });

        ChipGroup filter2Group = findViewById(R.id.filter2_group);
        filter2Group.setOnCheckedChangeListener((chipGroup, i) -> {
            switch (i) {
                case R.id.filter2_item1:
                    Log.d("MSW", "Filter2 Item 1");
                    break;
                case R.id.filter2_item2:
                    Log.d("MSW", "Filter2 Item 2");
                    break;
                case R.id.filter2_item3:
                    Log.d("MSW", "Filter2 Item 3");
                    break;
                case R.id.filter2_item4:
                    Log.d("MSW", "Filter2 Item 4");
                    break;
                case R.id.filter2_item5:
                    Log.d("MSW", "Filter2 Item 5");
                    break;
                case R.id.filter2_item6:
                    Log.d("MSW", "Filter2 Item 6");
                    break;
                case R.id.filter2_item7:
                    Log.d("MSW", "Filter2 Item 7");
                    break;
            }
        });

        EditText customChipEdit = findViewById(R.id.chip_edit);

        ChipGroup entryGroup = findViewById(R.id.chipgroup_entry);
        Activity activity = this;

        customChipEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                String textEntered = customChipEdit.getText().toString();
                Chip dynamicChip = new Chip(activity);
                dynamicChip.setText(textEntered);
                dynamicChip.setCloseIconVisible(true);
                dynamicChip.setCheckable(true);
                dynamicChip.setChipIcon(getResources().getDrawable(R.drawable.vct_account));
                dynamicChip.setOnCloseIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dynamicChip.setVisibility(View.GONE);
                    }
                });

                entryGroup.addView(dynamicChip);
                customChipEdit.requestFocus();
                return false;
            }
        });

//        Chip chipActionCustom = findViewById(R.id.chip_action_custom);
//        chipActionCustom.setOnClickListener(view -> {
//            chipActionCustom.setChipDrawable(R.drawable.);
//        });

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
                Snackbar.make(mainView, getString(R.string.phone_input_error), Snackbar.LENGTH_SHORT).show();
                return;
            }

            // Save original Values before sending, in-case user changes their mind
            final String beforeName = PrefsUtil.INSTANCE.getName(mainActivity.getApplicationContext());
            final long beforePhone = PrefsUtil.INSTANCE.getPhone(mainActivity.getApplicationContext());

            // Store new values
            final String nameToSet = nameEnterField.getText().toString();
            String formattedNum = PhoneNumberUtils.stripSeparators(phoneEnterField.getText().toString());
            final long phoneToSet = Long.valueOf(formattedNum);

            PrefsUtil.INSTANCE.setProfile(mainActivity.getApplicationContext(), nameToSet, phoneToSet);

            Snackbar.make(mainView, getString(R.string.profile_saved_confirm), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.undo), view -> {
                        // Reset to original
                        boolean complete = PrefsUtil.INSTANCE.setProfile(mainActivity.getApplicationContext(), beforeName, beforePhone);
                        if (complete) {
                            setPhoneNameValues();
                        }
                    })
                    .show();
        });

        // Set initial values from Prefs
        setPhoneNameValues();
    }

    private void setupTextScaleDialog() {
        View bottomSheet = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        ImageButton closeButton = findViewById(R.id.textscale_close);
        closeButton.setOnClickListener(view -> {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            bottomSheetBehavior.setPeekHeight(0);
        });

        Button showHide = findViewById(R.id.show_bottom_sheet);
        showHide.setOnClickListener(view -> {
            bottomSheetBehavior.setPeekHeight(300);
        });

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheetBehavior.setPeekHeight(0);
                }
            }

            @Override
            public void onSlide(View bottomSheet, float slideOffset) {
                closeButton.setRotation(slideOffset * -180);
            }
        });

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bottomSheetBehavior.setPeekHeight(0);
    }

    /**
     * When an item is clicked, this will launch an Alert Dialog with information specific to that item
     *
     * @param view
     */
    public void scaleTextItemClicked(View view) {
        TextView temp = (TextView) view;
        String scaleText = temp.getText().toString();
        String valueToSet = "No Value";

        //Sets custom text in the dialog
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_textscale, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        TextView caseText = dialogView.findViewById(R.id.ts_textcase);
        TextView fontText = dialogView.findViewById(R.id.ts_font);
        TextView sizeText = dialogView.findViewById(R.id.ts_size);
        TextView letterSpacingText = dialogView.findViewById(R.id.ts_letter_spacing);

        caseText.setText(UiUtil.INSTANCE.applySpecialFormatting(getString(R.string.case_text), getString(R.string.sentence)));
        fontText.setText(UiUtil.INSTANCE.applySpecialFormatting(getString(R.string.font_text), getString(R.string.regular)));

        switch (scaleText) {
            case "Headline1":
                valueToSet = getString(R.string.st_h1);
                letterSpacingText.setText(UiUtil.INSTANCE.applySpecialFormatting(getString(R.string.letter_spacing), getString(R.string.ls_neg1_5)));
                sizeText.setText(UiUtil.INSTANCE.applySpecialFormatting(getString(R.string.size), "96"));
                break;
            case "Headline2":
                valueToSet = getString(R.string.st_h2);
                letterSpacingText.setText(UiUtil.INSTANCE.applySpecialFormatting(getString(R.string.letter_spacing), getString(R.string.ls_neg5)));
                sizeText.setText(UiUtil.INSTANCE.applySpecialFormatting(getString(R.string.size), "60"));
                break;
            case "Headline3":
                valueToSet = getString(R.string.st_h3);
                letterSpacingText.setText(UiUtil.INSTANCE.applySpecialFormatting(getString(R.string.letter_spacing), getString(R.string.ls_zero)));
                sizeText.setText(UiUtil.INSTANCE.applySpecialFormatting(getString(R.string.size), "48"));
                break;
            case "Headline4":
                valueToSet = getString(R.string.st_h4);
                letterSpacingText.setText(UiUtil.INSTANCE.applySpecialFormatting(getString(R.string.letter_spacing), getString(R.string.ls_25)));
                sizeText.setText(UiUtil.INSTANCE.applySpecialFormatting(getString(R.string.size), "34"));
                break;
            case "Headline5":
                valueToSet = getString(R.string.st_h5);
                letterSpacingText.setText(UiUtil.INSTANCE.applySpecialFormatting(getString(R.string.letter_spacing), getString(R.string.ls_zero)));
                sizeText.setText(UiUtil.INSTANCE.applySpecialFormatting(getString(R.string.size), "24"));
                break;
            case "Headline6":
                valueToSet = getString(R.string.st_h6);
                letterSpacingText.setText(UiUtil.INSTANCE.applySpecialFormatting(getString(R.string.letter_spacing), getString(R.string.ls_15)));
                sizeText.setText(UiUtil.INSTANCE.applySpecialFormatting(getString(R.string.size), "20"));
                break;
            case "Subtitle1":
                valueToSet = getString(R.string.st_subtitle1);
                letterSpacingText.setText(UiUtil.INSTANCE.applySpecialFormatting(getString(R.string.letter_spacing), getString(R.string.ls_15)));
                sizeText.setText(UiUtil.INSTANCE.applySpecialFormatting(getString(R.string.size), "16"));
                break;
            case "Subtitle2":
                valueToSet = getString(R.string.st_subtitle2);
                letterSpacingText.setText(UiUtil.INSTANCE.applySpecialFormatting(getString(R.string.letter_spacing), getString(R.string.ls_1)));
                sizeText.setText(UiUtil.INSTANCE.applySpecialFormatting(getString(R.string.size), "14"));
                break;
            case "Body1":
                valueToSet = getString(R.string.st_body1);
                letterSpacingText.setText(UiUtil.INSTANCE.applySpecialFormatting(getString(R.string.letter_spacing), getString(R.string.ls_5)));
                sizeText.setText(UiUtil.INSTANCE.applySpecialFormatting(getString(R.string.size), "16"));
                break;
            case "Body2":
                valueToSet = getString(R.string.st_body2);
                letterSpacingText.setText(UiUtil.INSTANCE.applySpecialFormatting(getString(R.string.letter_spacing), getString(R.string.ls_25)));
                sizeText.setText(UiUtil.INSTANCE.applySpecialFormatting(getString(R.string.size), "14"));
                break;
            case "Button":
                valueToSet = getString(R.string.st_button);
                letterSpacingText.setText(UiUtil.INSTANCE.applySpecialFormatting(getString(R.string.letter_spacing), getString(R.string.ls_75)));
                caseText.setText(UiUtil.INSTANCE.applySpecialFormatting(getString(R.string.case_text), getString(R.string.all_caps)));
                sizeText.setText(UiUtil.INSTANCE.applySpecialFormatting(getString(R.string.size), "14"));
                break;
            case "Caption":
                valueToSet = getString(R.string.st_caption);
                letterSpacingText.setText(UiUtil.INSTANCE.applySpecialFormatting(getString(R.string.letter_spacing), getString(R.string.ls_4)));
                sizeText.setText(UiUtil.INSTANCE.applySpecialFormatting(getString(R.string.size), "12"));
                break;
            case "Overline":
                valueToSet = getString(R.string.st_overline);
                letterSpacingText.setText(UiUtil.INSTANCE.applySpecialFormatting(getString(R.string.letter_spacing), getString(R.string.ls_1dot5)));
                caseText.setText(UiUtil.INSTANCE.applySpecialFormatting(getString(R.string.case_text), getString(R.string.all_caps)));
                sizeText.setText(UiUtil.INSTANCE.applySpecialFormatting(getString(R.string.size), "10"));
                break;
            default:
                valueToSet = "Unset";
                caseText.setText(UiUtil.INSTANCE.applySpecialFormatting(getString(R.string.case_text), "Unset"));
                sizeText.setText(UiUtil.INSTANCE.applySpecialFormatting(getString(R.string.size), "Unset"));
                break;
        }

        builder.setMessage(getString(R.string.text_appearance_style_example, valueToSet));
        builder.setTitle(valueToSet);
        builder.create();
        builder.show();
    }


    private void setPhoneNameValues() {
        String name = PrefsUtil.INSTANCE.getName(this);
        if (!name.equals(PrefsUtil.INSTANCE.getPREFS_NAME_UNSET())) {
            nameEnterField.setText(name);
        }

        final long phone = PrefsUtil.INSTANCE.getPhone(this);
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

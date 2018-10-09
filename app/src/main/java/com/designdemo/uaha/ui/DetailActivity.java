package com.designdemo.uaha.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import com.designdemo.uaha.data.DeviceEntity;
import com.designdemo.uaha.data.VersionData;
import com.designdemo.uaha.net.FonoApiFactory;
import com.designdemo.uaha.net.FonoApiService;
import com.designdemo.uaha.util.PrefsUtil;
import com.designdemo.uaha.util.UiUtil;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.support.android.designlibdemo.R;

import java.io.IOException;
import java.util.List;

import static com.support.android.designlibdemo.BuildConfig.FONO_API_KEY;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_APP_NAME = "os_name";
    public static final String EXTRA_INDEX = "os_index";

    private WebView regsWv;
    private FloatingActionButton fab;
    private CollapsingToolbarLayout collapsingToolbar;

    //Details Card Views
    private int iconColor = 0;
    private LinearLayout specLayout;
    private TextView deviceName;
    private TextView features;
    private TextView featuresCont;
    private TextView releaseDate;
    private TextView multitouch;
    private TextView weight;
    private TextView size;
    private TextView dimen;
    private TextView cpu;
    private TextView memory;
    private TextView camera;
    private TextView display;
    private TextView resolution;
    private TextView video;


    private DeviceEntity deviceInfo = null;

    private OkHttpClient okclient;
    private int osVersion;
    private String androidName = "unset";

    //    private static final String TOKEN = "3564b7c851660d0155a011884aad146f3248cabe271d0aa3";
    private static final String TOKEN = FONO_API_KEY;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        handleIntent(intent);

        if (androidName == "unset") {
            androidName = intent.getStringExtra(EXTRA_APP_NAME);
        }

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
        }

        okclient = new OkHttpClient();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition();
        }

        setupToolbar(androidName);
        loadBackdrop();
        setupViews();
        setupFab();
        setupDetailsInfo();
        setupPalette();
        new InfoGatherTask().execute();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);

    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
        }
    }

    private void setupToolbar(String androidName) {
        osVersion = VersionData.getOsNum(androidName);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Activity myActiviy = this;

        BottomAppBar bottomAppBar = findViewById(R.id.bottom_appbar);
        bottomAppBar.replaceMenu(R.menu.detail_actions);
        bottomAppBar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_shuffle:
                    // Get a random Android product, and then refresh the UI
                    this.androidName = VersionData.getRandomProductName();
                    onResume();
                    return true;
            }
            return false;
        });

        collapsingToolbar = findViewById(R.id.collapsing_toolbar);

        //Split the and Version to display better
        String splitString = VersionData.getProductName(osVersion);
        String[] parts = splitString.split("-");
        collapsingToolbar.setTitle(parts[0]);
    }
//
//    /**
//     * Circular reveal exit animation
//     */
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    private void doCircularExitAnimation() {
//        int x = layoutContent.getRight();
//        int y = layoutContent.getBottom();
//
//        int startRadius = 0;
//        int endRadius = (int) Math.hypot(layoutMain.getWidth(), layoutMain.getHeight());
//
//        Animator anim = ViewAnimationUtils.createCircularReveal(layoutButtons, x, y, startRadius, endRadius);
//
//        anim.start();
//
//
//    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (hasFocus) {
                startPostponedEnterTransition();
            }
        }
    }

    private void loadBackdrop() {
        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        Glide.with(this).load(VersionData.getOsDrawable(osVersion)).centerCrop().into(imageView);
    }

    private void setupViews() {
        regsWv = findViewById(R.id.regulations_webview);
    }


    private void setupFab() {
        fab = findViewById((R.id.fab_detail));

        // Set initial state based on pref
        setFabIcon();

        // To over-ride the color of the FAB other then the theme color
        //fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey_300)));

        fab.setOnClickListener(v -> {
            PrefsUtil.INSTANCE.toggleFavorite(getApplicationContext(), osVersion);
            setFabIcon();

            // Notify the User with a snackbar
            // Need to set a calculate a specific offset for this so it appears higher then the BottomAppBar per the specification
            float pxScreenHeight = UiUtil.INSTANCE.getScreenHeight(this);
            float pxToolbar = UiUtil.INSTANCE.getPxForRes(R.dimen.snackbar_offset, this);
            float pxTopOffset = pxScreenHeight - pxToolbar;
            float sideOffset = UiUtil.INSTANCE.getPxForRes(R.dimen.large_margin, this);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            lp.topMargin = (int) pxTopOffset;
            lp.leftMargin = (int) sideOffset;
            lp.setMargins(lp.leftMargin, lp.topMargin, lp.rightMargin, lp.bottomMargin);

            View mainView = findViewById(R.id.main_content);
            Snackbar snackbar = Snackbar.make(mainView, R.string.favorite_confirm, Snackbar.LENGTH_LONG);
            View snackbarLayout = snackbar.getView();

            // Set the Layout Params we calculated before showing the Snackbar
            snackbarLayout.setLayoutParams(lp);
            snackbar.show();
        });
    }

    private void setFabIcon() {
        if (PrefsUtil.INSTANCE.isFavorite(getApplicationContext(), osVersion)) {
            fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite_on));
        } else {
            fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite_off));
        }
    }

    private void setupPalette() {
        Bitmap bm = BitmapFactory.decodeResource(getResources(), VersionData.getOsDrawable(osVersion));

        Palette.PaletteAsyncListener listener = palette -> {
            Log.d("Palette", "Palette has been generated");
            TextView pal_vib = findViewById(R.id.palette_vibrant);
            TextView perc1left = findViewById(R.id.perc1_left);
            perc1left.setBackgroundColor(palette.getVibrantColor(0x000000));
            pal_vib.setBackgroundColor(palette.getVibrantColor(0x000000));

            TextView pal_vib_dark = findViewById(R.id.palette_vibrant_dark);
            TextView perc1mid = findViewById(R.id.perc1_middle);
            perc1mid.setBackgroundColor(palette.getDarkVibrantColor(0x000000));
            pal_vib_dark.setBackgroundColor(palette.getDarkVibrantColor(0x000000));

            TextView pal_vib_light = findViewById(R.id.palette_vibrant_light);
            TextView perc1right = findViewById(R.id.perc1_right);
            perc1right.setBackgroundColor(palette.getLightVibrantColor(0x000000));
            pal_vib_light.setBackgroundColor(palette.getLightVibrantColor(0x000000));

            TextView pal_muted = findViewById(R.id.palette_muted);
            TextView perc2left = findViewById(R.id.perc2_left);
            perc2left.setBackgroundColor(palette.getMutedColor(0x000000));
            pal_muted.setBackgroundColor(palette.getMutedColor(0x000000));

            TextView pal_muted_dark = findViewById(R.id.palette_muted_dark);
            TextView perc2mid = findViewById(R.id.perc2_middle);
            perc2mid.setBackgroundColor(palette.getDarkMutedColor(0x000000));
            pal_muted_dark.setBackgroundColor(palette.getDarkMutedColor(0x000000));

            TextView pal_muted_light = findViewById(R.id.palette_muted_light);
            TextView perc2right = findViewById(R.id.perc2_right);
            perc2right.setBackgroundColor(palette.getLightMutedColor(0x000000));
            pal_muted_light.setBackgroundColor(palette.getLightMutedColor(0x000000));

            //Noticed the Expanded white doesn't show everywhere, use Palette to fix this
            collapsingToolbar.setExpandedTitleColor(palette.getVibrantColor(0x000000));

            if (palette.getVibrantColor(0x000000) == 0x000000) {
                Log.d("MSW", "this one is the other type");
                iconColor = palette.getMutedColor(0x000000);
            } else {
                iconColor = palette.getVibrantColor(0x000000);
            }

            //The back button should also have a better color applied to ensure it is visible,
            // Get a swatch, and get a more specific color for title from it.
            Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();
            int arrowColor = ContextCompat.getColor(this, R.color.black);
            if (vibrantSwatch != null) {
                arrowColor = vibrantSwatch.getTitleTextColor();
            }

            int res = getResources().getIdentifier("abc_ic_ab_back_material", "drawable", getPackageName());
            Drawable upArrow = getColorizedDrawable(res);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
            collapsingToolbar.setCollapsedTitleTextColor(arrowColor);
        };

        // Start this Async, because it takes some time to generate
        Palette.from(bm).generate(listener);
    }

    private void setupDetailsInfo() {
        deviceName = findViewById(R.id.details_device_name);
        features = findViewById(R.id.details_features);
        featuresCont = findViewById(R.id.details_features_c);
        releaseDate = findViewById(R.id.spec_releasedate);
        specLayout = findViewById(R.id.spec_layout);
        multitouch = findViewById(R.id.spec_multitouch);
        weight = findViewById(R.id.spec_weight);
        size = findViewById(R.id.spec_size);
        dimen = findViewById(R.id.spec_dimen);
        cpu = findViewById(R.id.spec_cpu);
        memory = findViewById(R.id.spec_memory);
        camera = findViewById(R.id.spec_camera);
        display = findViewById(R.id.spec_display);
        resolution = findViewById(R.id.spec_resolution);
        video = findViewById(R.id.spec_video);
    }

    private void invalidateDeviceInfoViews() {
        if (deviceInfo != null) {
            deviceName.setText(deviceInfo.getDeviceName());
            deviceName.setTextColor(iconColor);
            features.setText(deviceInfo.getFeatures());
            featuresCont.setText(deviceInfo.getFeatures_c());

            if (deviceInfo.getMultitouch() != null)
                setupSpecItem(R.drawable.vct_multitouch, R.string.spec_multitouch, deviceInfo.getMultitouch(), multitouch);
            else
                setupSpecItem(R.drawable.vct_multitouch, R.string.spec_multitouch, "NA", multitouch);

            String cpuInfo = deviceInfo.getChipset() + "\n" +  deviceInfo.getCpu();
            setupSpecItem(R.drawable.vct_cpu, R.string.spec_cpu, cpuInfo, cpu);

            setupSpecItem(R.drawable.vct_date, R.string.spec_release_date, deviceInfo.getAnnounced(), releaseDate);
            setupSpecItem(R.drawable.vct_size, R.string.spec_size, deviceInfo.getSize(), size);
            setupSpecItem(R.drawable.vct_weight, R.string.spec_weight, deviceInfo.getWeight(), weight);
            setupSpecItem(R.drawable.vct_dimen, R.string.spec_dimen, deviceInfo.getDimensions(), dimen);
            setupSpecItem(R.drawable.vct_memory, R.string.spec_memory, deviceInfo.getInternal(), memory);
            setupSpecItem(R.drawable.vct_camera, R.string.spec_camera, deviceInfo.getPrimary_(), camera);
            setupSpecItem(R.drawable.vct_video, R.string.spec_video, deviceInfo.getVideo(), video);
            setupSpecItem(R.drawable.vct_display, R.string.spec_display, deviceInfo.getType(), display);
            setupSpecItem(R.drawable.vct_resolution, R.string.spec_resolution, deviceInfo.getResolution(), resolution);

            specLayout.setVisibility(View.VISIBLE);
        } else {
            specLayout.setVisibility(View.GONE);
        }
    }

    private void setupSpecItem(@DrawableRes int drawable, @StringRes int title, String info, TextView view) {
        Drawable specDrawable = getColorizedDrawable(drawable);
        view.setCompoundDrawablesWithIntrinsicBounds(specDrawable, null, null, null);
        view.setText(UiUtil.INSTANCE.applyBoldFirstWord(getString(title), info));
    }

    private Drawable getColorizedDrawable(@DrawableRes int res) {
        Drawable drawable = ContextCompat.getDrawable(this, res);
        drawable.setColorFilter(iconColor, PorterDuff.Mode.SRC_ATOP);
        return drawable;
    }


    private class InfoGatherTask extends AsyncTask<String, Void, String> {
        private String phone;

        @Override
        protected String doInBackground(String... params) {
            String respStr = "";
            FonoApiService api = new FonoApiFactory().create();
            retrofit2.Response<List<DeviceEntity>> response = null;

            String[] product = VersionData.getProductName(osVersion).split("-");
            String deviceName = product[0];
            String manufacturer = product[1];
            try {
                response = api.getDevice(TOKEN, deviceName, manufacturer, null).execute();
                List<DeviceEntity> devices = response.body();
                if (devices != null) {
                    for (DeviceEntity dv : devices) {
                        Log.d("MSW", "Device Item is: " + dv.getDeviceName());
                        deviceInfo = dv;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }

            String req = "https://en.wikipedia.org/w/api.php?action=query&titles=" + VersionData.getWikiQuery(osVersion) + "&prop=revisions&rvprop=content&format=jsonfm";
            Log.d("NET", "WIKIPedia request: " + req);

            Request request = new Request.Builder()
                    .url(req)
                    .build();

            try {
                Response blacklistResp = okclient.newCall(request).execute();
                respStr = blacklistResp.body().string();
                Log.d("NET", "WIKIPedia_RESPONSE: " + respStr);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return respStr;
        }

        @Override
        protected void onPostExecute(String result) {
            regsWv.loadData(result, "text/html", null);
            invalidateDeviceInfoViews();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}

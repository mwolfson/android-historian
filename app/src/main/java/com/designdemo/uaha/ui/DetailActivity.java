package com.designdemo.uaha.ui;

import android.animation.Animator;
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

import com.designdemo.uaha.data.VersionData;
import com.designdemo.uaha.util.PrefsUtil;
import com.designdemo.uaha.util.UiUtil;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AnimationUtils;
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

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_APP_NAME = "os_name";
    public static final String EXTRA_INDEX = "os_index";

    private WebView regsWv;
    private FloatingActionButton fab;
    private CollapsingToolbarLayout collapsingToolbar;

    private OkHttpClient okclient;
    private int osVersion;
    private String androidName = "unset";

    private CoordinatorLayout layoutMain;
    private CoordinatorLayout layoutContent;
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
        Log.d("MSW", "Name in is:" + androidName);

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.d("MSW", "The Query is: " + query);
        }

        okclient = new OkHttpClient();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition();
        }

        setupToolbar(androidName);
        loadBackdrop();
        setupViews();
        setupFab();
        setupPalette();
        new WikiPullTask().execute();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);

    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.d("MSW", "The Query is: " + query);
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
                    Log.d("MSW", "SHUFFLE IT UP");
                    this.androidName = VersionData.getRandomProductName();
                    onResume();
                    Log.d("MSW", "SHUFFLE DONE" + androidName);
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
        layoutMain = findViewById(R.id.main_content);
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

            //The back button should also have a better color applied to ensure it is visible
            String resName = Build.VERSION.SDK_INT >= 23 ? "abc_ic_ab_back_material" : "abc_ic_ab_back_mtrl_am_alpha";
            int res = getResources().getIdentifier(resName, "drawable", getPackageName());
            Drawable upArrow = getResources().getDrawable(res);
            upArrow.setColorFilter(palette.getVibrantColor(0x000000), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
            collapsingToolbar.setCollapsedTitleTextColor(palette.getVibrantColor(0x000000));

        };

        // Start this Async, because it takes some time to generate
        Palette.from(bm).generate(listener);

    }

    //TODO - this will replaced with a proper API call (and network stack)
    private class WikiPullTask extends AsyncTask<String, Void, String> {
        private String phone;

        @Override
        protected String doInBackground(String... params) {
            String respStr = "";

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
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}

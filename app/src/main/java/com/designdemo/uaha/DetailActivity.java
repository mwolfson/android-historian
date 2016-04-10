package com.designdemo.uaha;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.support.android.designlibdemo.R;

import java.io.IOException;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_NAME = "os_name";
    public static final String EXTRA_INDEX = "os_index";

    private WebView regsWv;
    private FloatingActionButton fab;
    private CollapsingToolbarLayout collapsingToolbar;

    private OkHttpClient okclient;
    private int osVersion;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        final String androidName = intent.getStringExtra(EXTRA_NAME);
        osVersion = VersionData.getOsNum(androidName);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(VersionData.getProductName(osVersion));

        okclient = new OkHttpClient();
        loadBackdrop();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition();
        }

        setupViews();
        setupFab();
        setupPalette();
        new WikiPullTask().execute();
    }

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
        regsWv = (WebView) findViewById(R.id.regulations_webview);
    }


    private void setupFab() {
        fab = (FloatingActionButton) findViewById((R.id.fab_detail));

        // Set initial state based on pref
        setFabIcon();

        // To over-ride the color of the FAB other then the theme color
        //fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey_300)));

        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PrefsUtil.toggleFavorite(getApplicationContext(), osVersion);
                setFabIcon();

                //Send the user a message to let them know change was made
                View mainView = findViewById(R.id.main_content);
                Snackbar.make(mainView, R.string.favorite_confirm, Snackbar.LENGTH_LONG)
                        .show(); // Don’t forget to show!
            }
        });
    }

    private void setFabIcon() {
        if (PrefsUtil.isFavorite(getApplicationContext(), osVersion)) {
            fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite_on));
        } else {
            fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite_off));
        }
    }

    private void setupPalette() {
        Bitmap bm = BitmapFactory.decodeResource(getResources(), VersionData.getOsDrawable(osVersion));

        Palette.PaletteAsyncListener listener = new Palette.PaletteAsyncListener() {
            public void onGenerated(Palette palette) {
                Log.d("Palette", "Palette has been generated");
                TextView pal_vib = (TextView) findViewById(R.id.palette_vibrant);
                TextView perc1left = (TextView) findViewById(R.id.perc1_left);
                perc1left.setBackgroundColor(palette.getVibrantColor(0x000000));
                pal_vib.setBackgroundColor(palette.getVibrantColor(0x000000));

                TextView pal_vib_dark = (TextView) findViewById(R.id.palette_vibrant_dark);
                TextView perc1mid = (TextView) findViewById(R.id.perc1_middle);
                perc1mid.setBackgroundColor(palette.getDarkVibrantColor(0x000000));
                pal_vib_dark.setBackgroundColor(palette.getDarkVibrantColor(0x000000));

                TextView pal_vib_light = (TextView) findViewById(R.id.palette_vibrant_light);
                TextView perc1right = (TextView) findViewById(R.id.perc1_right);
                perc1right.setBackgroundColor(palette.getLightVibrantColor(0x000000));
                pal_vib_light.setBackgroundColor(palette.getLightVibrantColor(0x000000));

                TextView pal_muted = (TextView) findViewById(R.id.palette_muted);
                TextView perc2left = (TextView) findViewById(R.id.perc2_left);
                perc2left.setBackgroundColor(palette.getMutedColor(0x000000));
                pal_muted.setBackgroundColor(palette.getMutedColor(0x000000));

                TextView pal_muted_dark = (TextView) findViewById(R.id.palette_muted_dark);
                TextView perc2mid = (TextView) findViewById(R.id.perc2_middle);
                perc2mid.setBackgroundColor(palette.getDarkMutedColor(0x000000));
                pal_muted_dark.setBackgroundColor(palette.getDarkMutedColor(0x000000));

                TextView pal_muted_light = (TextView) findViewById(R.id.palette_muted_light);
                TextView perc2right = (TextView) findViewById(R.id.perc2_right);
                perc2right.setBackgroundColor(palette.getLightMutedColor(0x000000));
                pal_muted_light.setBackgroundColor(palette.getLightMutedColor(0x000000));

                //Noticed the Expanded white doesn't show everywhere, use Palette to fix this
                collapsingToolbar.setExpandedTitleColor(palette.getVibrantColor(0x000000));
            }
        };

        // Start this Async, because it takes some time to generate
        Palette.from(bm).generate(listener);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_actions, menu);
        return true;
    }

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

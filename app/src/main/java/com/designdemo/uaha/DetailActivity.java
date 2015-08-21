/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.designdemo.uaha;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
    private int os_version;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        final String androidName = intent.getStringExtra(EXTRA_NAME);
        os_version = VersionData.getOsNum(androidName);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(VersionData.getProductName(os_version));

        okclient = new OkHttpClient();
        loadBackdrop();

        setupViews();
        setupFab();
        setupPalette();
        new WikiPullTask().execute();
    }

    private void loadBackdrop() {
        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        Glide.with(this).load(VersionData.getOsDrawable(os_version)).centerCrop().into(imageView);
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
                PrefsUtil.toggleFavorite(getApplicationContext(), os_version);
                setFabIcon();

                //Send the user a message to let them know change was made
                View mainView = (View) findViewById(R.id.main_content);
                Snackbar.make(mainView, R.string.favorite_confirm, Snackbar.LENGTH_LONG)
                        .show(); // Don’t forget to show!
            }
        });
    }

    private void setFabIcon() {
        if (PrefsUtil.isFavorite(getApplicationContext(), os_version)) {
            fab.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_favorite_on));
        } else {
            fab.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_favorite_off));
        }
    }

    private void setupPalette() {
        Bitmap bm = BitmapFactory.decodeResource(getResources(), VersionData.getOsDrawable(os_version));

        Palette.PaletteAsyncListener listener = new Palette.PaletteAsyncListener() {
            public void onGenerated(Palette palette) {
                Log.d("Palette", "Palette has been generated");
                TextView pal_vib = (TextView) findViewById(R.id.palette_vibrant);
                pal_vib.setBackgroundColor(palette.getVibrantColor(0x000000));

                TextView pal_vib_dark = (TextView) findViewById(R.id.palette_vibrant_dark);
                pal_vib_dark.setBackgroundColor(palette.getDarkVibrantColor(0x000000));

                TextView pal_vib_light = (TextView) findViewById(R.id.palette_vibrant_light);
                pal_vib_light.setBackgroundColor(palette.getLightVibrantColor(0x000000));

                TextView pal_muted = (TextView) findViewById(R.id.palette_muted);
                pal_muted.setBackgroundColor(palette.getMutedColor(0x000000));

                TextView pal_muted_dark = (TextView) findViewById(R.id.palette_muted_dark);
                pal_muted_dark.setBackgroundColor(palette.getDarkMutedColor(0x000000));

                TextView pal_muted_light = (TextView) findViewById(R.id.palette_muted_light);
                pal_muted_light.setBackgroundColor(palette.getLightMutedColor(0x000000));

                //Noticed the Expanded white doesn't show everywhere, use Palette to fix this
                collapsingToolbar.setExpandedTitleColor(palette.getVibrantColor(0x000000));
            }
        };

        // Start this Async, because it takes some time to generate
        Palette.generateAsync(bm, listener);

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

            String req = "https://en.wikipedia.org/w/api.php?action=query&titles=" + VersionData.getWikiQuery(os_version) + "&prop=revisions&rvprop=content&format=jsonfm";
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

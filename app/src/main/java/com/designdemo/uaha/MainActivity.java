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
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.support.android.designlibdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 */
public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ViewPager viewPager;

    public static final int OS_FRAG = 0;
    public static final int DEVICE_FRAG = 1;
    public static final int FAV_FRAG = 2;
    public static final String EXTRA_FRAG_TYPE = "extraFragType";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
            viewPager.setCurrentItem(getIntent().getIntExtra(EXTRA_FRAG_TYPE, OS_FRAG));
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
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
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());

        Bundle args = new Bundle();
        ProductListFragment osFrag = new ProductListFragment();
        args.putInt(ProductListFragment.ARG_FRAG_TYPE, ProductListFragment.FRAG_TYPE_OS);
        osFrag.setArguments(args);

        Bundle args1 = new Bundle();
        ProductListFragment deviceFrag = new ProductListFragment();
        args1.putInt(ProductListFragment.ARG_FRAG_TYPE, ProductListFragment.FRAG_TYPE_DEVICE);
        deviceFrag.setArguments(args1);

        Bundle args2 = new Bundle();
        ProductListFragment favFrag = new ProductListFragment();
        args2.putInt(ProductListFragment.ARG_FRAG_TYPE, ProductListFragment.FRAG_TYPE_FAV);
        favFrag.setArguments(args2);

        adapter.addFragment(osFrag, "OS Version");
        adapter.addFragment(deviceFrag, "Device");
        adapter.addFragment(favFrag, "Favorites");
        viewPager.setAdapter(adapter);
    }

    private void setupDrawerContent(final NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        viewPager.setCurrentItem(0);
                        mDrawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_devices:
                        viewPager.setCurrentItem(1);
                        mDrawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_favorites:
                        viewPager.setCurrentItem(2);
                        mDrawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_userinfo:
                        Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                        startActivity(intent);
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

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
}

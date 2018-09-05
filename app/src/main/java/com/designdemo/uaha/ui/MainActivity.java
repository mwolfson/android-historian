package com.designdemo.uaha.ui;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.core.view.GravityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;

import com.support.android.designlibdemo.BottomNavActivity;
import com.support.android.designlibdemo.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ViewPager viewPager;

    public static final int OS_FRAG = 0;
    public static final int DEVICE_FRAG = 1;
    public static final int FAV_FRAG = 2;
    public static final String EXTRA_FRAG_TYPE = "extraFragType";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomAppBar bottomAppBar = findViewById(R.id.bottom_appbar);
        setSupportActionBar(bottomAppBar);
        bottomAppBar.replaceMenu(R.menu.main_actions);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        viewPager = findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
            viewPager.setCurrentItem(getIntent().getIntExtra(EXTRA_FRAG_TYPE, OS_FRAG));
        }

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_actions, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);

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

        adapter.addFragment(osFrag, getString(R.string.os_version));
        adapter.addFragment(deviceFrag, getString(R.string.devices));
        adapter.addFragment(favFrag, getString(R.string.favorites));
        viewPager.setAdapter(adapter);
    }

    private void setupDrawerContent(final NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    menuItem.setChecked(true);
                    switch (menuItem.getItemId()) {
                        case R.id.nav_home:
                            viewPager.setCurrentItem(0);
                            drawerLayout.closeDrawers();
                            return true;
                        case R.id.nav_devices:
                            viewPager.setCurrentItem(1);
                            drawerLayout.closeDrawers();
                            return true;
                        case R.id.nav_favorites:
                            viewPager.setCurrentItem(2);
                            drawerLayout.closeDrawers();
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
                        case R.id.nav_bottom_nav:
                            Intent bottomNavIntent = new Intent(getApplicationContext(), BottomNavActivity.class);
                            startActivity(bottomNavIntent);
                            return true;
                        default:
                            return true;
                    }
                });
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> fragments = new ArrayList<>();
        private final List<String> fragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            fragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitles.get(position);
        }
    }
}

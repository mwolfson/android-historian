package com.designdemo.uaha.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.core.view.GravityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        TabLayout tabLayout = findViewById(R.id.tabs);
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

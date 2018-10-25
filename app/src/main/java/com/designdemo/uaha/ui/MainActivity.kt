package com.designdemo.uaha.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle

import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout

import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.core.view.GravityCompat
import androidx.viewpager.widget.ViewPager
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity

import android.view.Menu
import android.view.MenuItem

import com.support.android.designlibdemo.R

import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    private var drawerLayout: DrawerLayout? = null
    private var viewPager: ViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomAppBar = findViewById<BottomAppBar>(R.id.bottom_appbar)
        setSupportActionBar(bottomAppBar)
        bottomAppBar.replaceMenu(R.menu.main_actions)

        val ab = supportActionBar
        ab?.setHomeAsUpIndicator(R.drawable.ic_menu)
        ab?.setDisplayHomeAsUpEnabled(true)

        drawerLayout = findViewById(R.id.drawer_layout)

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        if (navigationView != null) {
            setupDrawerContent(navigationView)
        }

        viewPager = findViewById(R.id.viewpager)
        if (viewPager != null) {
            setupViewPager(viewPager!!)
            viewPager!!.currentItem = intent.getIntExtra(EXTRA_FRAG_TYPE, OS_FRAG)
        }

        val tabLayout = findViewById<TabLayout>(R.id.tabs)
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_actions, menu)

        // Associate searchable configuration with the SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(componentName))
        searchView.isSubmitButtonEnabled = true

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                drawerLayout!!.openDrawer(GravityCompat.START)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = Adapter(supportFragmentManager)

        val args = Bundle()
        val osFrag = ProductListFragment()
        args.putInt(ProductListFragment.ARG_FRAG_TYPE, ProductListFragment.FRAG_TYPE_OS)
        osFrag.arguments = args

        val args1 = Bundle()
        val deviceFrag = ProductListFragment()
        args1.putInt(ProductListFragment.ARG_FRAG_TYPE, ProductListFragment.FRAG_TYPE_DEVICE)
        deviceFrag.arguments = args1

        val args2 = Bundle()
        val favFrag = ProductListFragment()
        args2.putInt(ProductListFragment.ARG_FRAG_TYPE, ProductListFragment.FRAG_TYPE_FAV)
        favFrag.arguments = args2

        adapter.addFragment(osFrag, getString(R.string.os_version))
        adapter.addFragment(deviceFrag, getString(R.string.devices))
        adapter.addFragment(favFrag, getString(R.string.favorites))
        viewPager.adapter = adapter
    }

    private fun setupDrawerContent(navigationView: NavigationView) {
        navigationView.setNavigationItemSelectedListener { menuItem ->
            var retVal = false
            menuItem.isChecked = true
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    viewPager!!.currentItem = 0
                    drawerLayout!!.closeDrawers()
                    retVal = true
                }
                R.id.nav_devices -> {
                    viewPager!!.currentItem = 1
                    drawerLayout!!.closeDrawers()
                    retVal = true
                }
                R.id.nav_favorites -> {
                    viewPager!!.currentItem = 2
                    drawerLayout!!.closeDrawers()
                    retVal = true
                }
                R.id.nav_userinfo -> {
                    val intent = Intent(applicationContext, UserActivity::class.java)
                    startActivity(intent)
                    retVal = true
                }
                R.id.nav_link1 -> {
                    val browser1 = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.android.com/"))
                    startActivity(browser1)
                    retVal = true
                }
                R.id.nav_link2 -> {
                    val browser2 = Intent(Intent.ACTION_VIEW, Uri.parse("http://developer.android.com/"))
                    startActivity(browser2)
                    retVal = true
                }
                R.id.nav_bottom_nav -> {
                    val bottomNavIntent = Intent(applicationContext, BottomNavActivity::class.java)
                    startActivity(bottomNavIntent)
                    retVal = true
                }
                R.id.nav_motionlayout -> {
                    val motionLayoutIntent = Intent(applicationContext, MotionLayoutActivity::class.java)
                    startActivity(motionLayoutIntent)
                    retVal = true
                }
                else -> retVal = true
            }
            retVal
        }
    }

    internal class Adapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        private val fragments = ArrayList<Fragment>()
        private val fragmentTitles = ArrayList<String>()

        fun addFragment(fragment: Fragment, title: String) {
            fragments.add(fragment)
            fragmentTitles.add(title)
        }

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }

        override fun getPageTitle(position: Int): CharSequence {
            return fragmentTitles[position]
        }
    }

    companion object {

        val OS_FRAG = 0
        val DEVICE_FRAG = 1
        val FAV_FRAG = 2
        val EXTRA_FRAG_TYPE = "extraFragType"
    }
}

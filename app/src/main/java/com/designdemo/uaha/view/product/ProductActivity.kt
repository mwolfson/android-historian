package com.designdemo.uaha.view.product

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.designdemo.uaha.view.demo.BottomNavActivity
import com.designdemo.uaha.view.demo.MotionLayoutActivity
import com.designdemo.uaha.view.product.device.DeviceFragment
import com.designdemo.uaha.view.product.fav.FavFragment
import com.designdemo.uaha.view.product.os.OsFragment
import com.designdemo.uaha.view.user.UserActivity
import com.google.android.material.navigation.NavigationView
import com.support.android.designlibdemo.R
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class ProductActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomAppBar = bottom_appbar
        setSupportActionBar(bottomAppBar)
        bottomAppBar.replaceMenu(R.menu.main_actions)

        val ab = supportActionBar
        ab?.setHomeAsUpIndicator(R.drawable.ic_menu)
        ab?.setDisplayHomeAsUpEnabled(true)

        val navigationView = nav_view
        if (navigationView != null) {
            setupDrawerContent(navigationView)
        }

        setupViewPager(product_viewpager)
        product_viewpager.currentItem = intent.getIntExtra(EXTRA_FRAG_TYPE, OS_FRAG)

        product_tabs.setupWithViewPager(product_viewpager)
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
                drawer_layout.openDrawer(GravityCompat.START)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = Adapter(supportFragmentManager)

        val osFrag = OsFragment()

        val deviceFrag = DeviceFragment()

        val favFrag = FavFragment()

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
                    product_viewpager.currentItem = 0
                    drawer_layout.closeDrawers()
                    retVal = true
                }
                R.id.nav_devices -> {
                    product_viewpager.currentItem = 1
                    drawer_layout.closeDrawers()
                    retVal = true
                }
                R.id.nav_favorites -> {
                    product_viewpager.currentItem = 2
                    drawer_layout.closeDrawers()
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

        const val OS_FRAG = 0
        const val DEVICE_FRAG = 1
        const val FAV_FRAG = 2
        const val EXTRA_FRAG_TYPE = "extraFragType"
    }
}

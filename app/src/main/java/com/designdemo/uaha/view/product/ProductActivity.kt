package com.designdemo.uaha.view.product

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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
import com.support.android.designlibdemo.BuildConfig
import com.support.android.designlibdemo.R
import kotlinx.android.synthetic.main.activity_detail.bottom_appbar
import kotlinx.android.synthetic.main.activity_main.nav_view
import kotlinx.android.synthetic.main.activity_main.product_viewpager
import kotlinx.android.synthetic.main.activity_main.product_tabs
import kotlinx.android.synthetic.main.activity_main.drawer_layout

class ProductActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Base_Theme_DesignDemo)
        setContentView(R.layout.activity_main)

        val bottomAppBar = bottom_appbar
        setSupportActionBar(bottomAppBar)
        bottomAppBar.replaceMenu(R.menu.main_actions)

        val ab = supportActionBar
        ab?.setHomeAsUpIndicator(R.drawable.vct_menu)
        ab?.setDisplayHomeAsUpEnabled(true)

        val navigationView = nav_view
        if (navigationView != null) {
            setupDrawerContent(navigationView)
            val headerView = navigationView.getHeaderView(0)
            if (headerView != null) {
                val versionText = headerView.findViewById<TextView>(R.id.header_versioninfo)
                versionText.text = "Version:  ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"

                val appTitleText = headerView.findViewById<TextView>(R.id.header_apptitle)
                appTitleText.setOnClickListener {
                    val playStore = Intent(Intent.ACTION_VIEW, Uri.parse(
                            "https://play.google.com/store/apps/details?id=com.ableandroid.historian"))
                    startActivity(playStore)
                }
            }
        }

        setupViewPager(product_viewpager)
        product_viewpager?.currentItem = intent.getIntExtra(EXTRA_FRAG_TYPE, OS_FRAG)

        product_tabs?.setupWithViewPager(product_viewpager)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_actions, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                drawer_layout.openDrawer(GravityCompat.START)
                return true
            }
            R.id.menu_help -> {
                val bottomNavIntent = Intent(applicationContext, BottomNavActivity::class.java)
                startActivity(bottomNavIntent)
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
            val retVal: Boolean
            menuItem.isChecked = true
            when (menuItem.itemId) {
                R.id.nav_userinfo -> {
                    startActivity(Intent(applicationContext, UserActivity::class.java))
                    retVal = true
                }
                R.id.nav_link1 -> {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.android.com/")))
                    retVal = true
                }
                R.id.nav_link2 -> {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://material.io/")))
                    retVal = true
                }
                R.id.nav_bottom_nav -> {
                    startActivity(Intent(applicationContext, BottomNavActivity::class.java))
                    retVal = true
                }
                R.id.nav_motionlayout -> {
                    startActivity(Intent(applicationContext, MotionLayoutActivity::class.java))
                    retVal = true
                }
                R.id.nav_homepage -> {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://www.ableandroid.com/")))
                    retVal = true
                }
                R.id.nav_playlink -> {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/apps/testing/com.ableandroid.historian")))
                    retVal = true
                }
                R.id.nav_githublink -> {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/mwolfson/android-historian")))
                    retVal = true
                }
                else -> retVal = false
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

        override fun getItem(position: Int) = fragments[position]

        override fun getCount() = fragments.size

        override fun getPageTitle(position: Int) = fragmentTitles[position]
    }

    companion object {

        const val OS_FRAG = 0
        const val DEVICE_FRAG = 1
        const val FAV_FRAG = 2
        const val EXTRA_FRAG_TYPE = "extraFragType"
    }
}

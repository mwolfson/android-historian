package com.designdemo.uaha.view.detail

import android.annotation.TargetApi
import android.app.SearchManager
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.designdemo.uaha.data.model.detail.DetailEntity
import com.designdemo.uaha.data.model.product.ProductEntity
import com.designdemo.uaha.util.UiUtil
import com.designdemo.uaha.view.demo.BottomNavActivity
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import com.support.android.designlibdemo.R
import com.support.android.designlibdemo.R.color.black
import kotlinx.android.synthetic.main.activity_detail.*
import kotlin.random.Random

class DetailActivity : AppCompatActivity() {

    // Details Card Views
    private var iconColor = 0

    private var localProdItem: ProductEntity? = null

    private lateinit var detailViewModel: DetailViewModel

    private var androidName = "unset"

    companion object {
        private const val TAG = "DetailActivity"

        const val EXTRA_APP_NAME = "os_name"
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        if (androidName === "unset") {
            androidName = intent.getStringExtra(EXTRA_APP_NAME)
        }

        detailViewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)

        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
        }

        spec_layout.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()

        val intent = intent
        handleIntent(intent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition()
        }

        setupFab()

        val product = androidName.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        detailViewModel.getProductForName(product[0]).observe(this, Observer { prodItem ->
            prodItem?.let {
                localProdItem = prodItem
                Glide.with(this).load(prodItem.imgId).centerCrop().into(backdrop)
                setFabIcon()
                setupPalette(prodItem.imgId!!)
                setupToolbar()
            }
        })

        detailViewModel.getDetailsForItem(androidName).observe(this, Observer { detailsForItem ->
            detailsForItem?.let {
                setDeviceInfoViews(detailsForItem)
            }
        })

        detailViewModel.getProgressBarVisibility().observe(this, Observer { progVisibility ->
            if (progVisibility) {
                details_progress_layout.visibility = View.VISIBLE
            } else {
                details_progress_layout.visibility = View.GONE
            }
        })

        // Retrieve the phone and WIKI info from net, and put in DB when found
        detailViewModel.getFonoNetInfo(androidName)
//        detailViewModel.getWikiNetInfo("Android Oreo")
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            Log.d(TAG, "The search query is happenning ${intent.getStringExtra(SearchManager.QUERY)}")
            val query = intent.getStringExtra(SearchManager.QUERY)
        }
    }

    // TODO, setup this better
    private fun setupToolbar() {
        setSupportActionBar(detail_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        bottom_appbar.replaceMenu(R.menu.detail_actions)
        bottom_appbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_shuffle -> {
                    // Get a random Android product, and then refresh the UI
                    // TODO need to move to VM, because it is broken here
                    clearDeviceInfoViews()
                    val deviceStrings = resources.getStringArray(R.array.device_names)
                    val randInt = Random.nextInt(deviceStrings.size - 1)
                    // TODO Send this value to VM, and the force a refresh of views with data from this item
                    detailViewModel.updateProductFromRefresh(deviceStrings[randInt])
                    this.androidName = deviceStrings[randInt]
//                    onResume()
                }
                R.id.menu_help -> {
                    val bottomNavIntent = Intent(applicationContext, BottomNavActivity::class.java)
                    startActivity(bottomNavIntent)
                }
            }
            false
        }

        collapsing_toolbar.title = localProdItem!!.title
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && hasFocus) {
            startPostponedEnterTransition()
        }
    }

    private fun setupFab() {
        fab_detail.setOnClickListener {
            // Toggle the value to be opposite of what it is, to set
            var toSetFav = 1
            if (localProdItem?.isFav == 1) {
                toSetFav = 0
            }

            localProdItem?.apply {
                isFav = toSetFav
            }?.let { item ->
                detailViewModel.insertFav(item)
            }

            // Notify the User with a snackbar
            // Need to set the FAB as the anchor view, so this appears higher then the BottomAppBar per Material Spec
            Snackbar.make(detail_main_linear, R.string.favorite_confirm, Snackbar.LENGTH_LONG)
                    .setAnchorView(fab_detail)
                    .show()
        }
    }

    private fun setFabIcon() {
        if (localProdItem!!.isFav == 1) {
            fab_detail.isSelected = true
            fab_detail.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.vct_fav_white_sel))
        } else {
            fab_detail.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.vct_fav_white_unsel))
        }
    }

    @Suppress("UnnecessaryParentheses")
    private fun setupPalette(imgId: Int) {
        val bm = BitmapFactory.decodeResource(resources, imgId)
        try {
            Palette.from(bm).generate { palette ->
                Log.d(TAG, "Palette has been generated")
                perc1_left.setBackgroundColor(palette!!.getVibrantColor(0x000000))
                palette_vibrant.setBackgroundColor(palette.getVibrantColor(0x000000))

                perc1_middle.setBackgroundColor(palette.getDarkVibrantColor(0x000000))
                palette_vibrant_dark.setBackgroundColor(palette.getDarkVibrantColor(0x000000))

                perc1_right.setBackgroundColor(palette.getLightVibrantColor(0x000000))
                palette_vibrant_light.setBackgroundColor(palette.getLightVibrantColor(0x000000))

                perc2_left.setBackgroundColor(palette.getMutedColor(0x000000))
                palette_muted.setBackgroundColor(palette.getMutedColor(0x000000))

                perc2_middle.setBackgroundColor(palette.getDarkMutedColor(0x000000))
                palette_muted_dark.setBackgroundColor(palette.getDarkMutedColor(0x000000))

                perc2_right.setBackgroundColor(palette.getLightMutedColor(0x000000))
                palette_muted_light.setBackgroundColor(palette.getLightMutedColor(0x000000))

                // Noticed the Expanded white doesn't show everywhere, use Palette to fix this
                collapsing_toolbar?.setExpandedTitleColor(palette.getVibrantColor(0x000000))

                iconColor = if (palette.getVibrantColor(0x000000) == 0x000000)
                    palette.getMutedColor(0x000000)
                else
                    palette.getVibrantColor(0x000000)

                // The back button should also have a better color applied to ensure it is visible,
                val res = resources.getIdentifier("abc_ic_ab_back_material", "drawable", packageName)
                val upArrow = getColorizedDrawable(res)
                val whiteArrow = getColorizedDrawable(res, R.color.white_grey)

                // This applies the custom color to the home button, when expanded, and white when collapsed
                appbar?.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener {
                    appBarLayout: AppBarLayout, offset: Int ->
                    val isCollapsed = (offset == (-1 * appBarLayout.totalScrollRange))
                    if (!isCollapsed)
                        supportActionBar?.setHomeAsUpIndicator(upArrow)
                    else
                        supportActionBar?.setHomeAsUpIndicator(whiteArrow)
                })
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error with image! + $e", e)
        }
    }

    private fun setDeviceInfoViews(detailItem: DetailEntity) {
        details_device_name.text = detailItem.DeviceName
        details_features.text = detailItem.features
        details_features_c.text = detailItem.features_c

        if (detailItem.multitouch != null)
            setupSpecItem(R.drawable.vct_multitouch, R.string.spec_multitouch, detailItem.multitouch, spec_multitouch)
        else
            setupSpecItem(R.drawable.vct_multitouch, R.string.spec_multitouch, "NA", spec_multitouch)

        val cpuInfo = detailItem.chipset + "\n" + detailItem.cpu
        setupSpecItem(R.drawable.vct_cpu, R.string.spec_cpu, cpuInfo, spec_cpu)

        if (detailItem.announced != null)
            setupSpecItem(R.drawable.vct_date, R.string.spec_release_date, detailItem.announced, spec_releasedate)

        if (detailItem.size != null)
            setupSpecItem(R.drawable.vct_size, R.string.spec_size, detailItem.size, spec_size)

        if (detailItem.weight != null)
            setupSpecItem(R.drawable.vct_weight, R.string.spec_weight, detailItem.weight, spec_weight)

        if (detailItem.dimensions != null)
            setupSpecItem(R.drawable.vct_dimen, R.string.spec_dimen, detailItem.dimensions, spec_dimen)

        if (detailItem.internal != null)
            setupSpecItem(R.drawable.vct_memory, R.string.spec_memory, detailItem.internal, spec_memory)

        if (detailItem.primary_ != null)
            setupSpecItem(R.drawable.vct_camera, R.string.spec_camera, detailItem.primary_, spec_camera)

        if (detailItem.video != null)
            setupSpecItem(R.drawable.vct_video, R.string.spec_video, detailItem.video, spec_video)

        if (detailItem.type != null)
            setupSpecItem(R.drawable.vct_display, R.string.spec_display, detailItem.type, spec_display)

        if (detailItem.resolution != null)
            setupSpecItem(R.drawable.vct_resolution, R.string.spec_resolution, detailItem.resolution, spec_resolution)

        spec_layout.visibility = View.VISIBLE
    }

    private fun clearDeviceInfoViews() {
        details_device_name.text = getString(R.string.getting_information)
        details_device_name.setTextColor(resources.getColor(black))
        details_features.text = ""
        details_features_c.text = ""

        setupSpecItem(R.drawable.vct_multitouch, R.string.spec_multitouch, "", spec_multitouch)
        setupSpecItem(R.drawable.vct_cpu, R.string.spec_cpu, "", spec_cpu)
        setupSpecItem(R.drawable.vct_date, R.string.spec_release_date, "", spec_releasedate)
        setupSpecItem(R.drawable.vct_size, R.string.spec_size, "", spec_size)
        setupSpecItem(R.drawable.vct_weight, R.string.spec_weight, "", spec_weight)
        setupSpecItem(R.drawable.vct_dimen, R.string.spec_dimen, "", spec_dimen)
        setupSpecItem(R.drawable.vct_memory, R.string.spec_memory, "", spec_memory)
        setupSpecItem(R.drawable.vct_camera, R.string.spec_camera, "", spec_camera)
        setupSpecItem(R.drawable.vct_video, R.string.spec_video, "", spec_video)
        setupSpecItem(R.drawable.vct_display, R.string.spec_display, "", spec_display)
        setupSpecItem(R.drawable.vct_resolution, R.string.spec_resolution, "", spec_resolution)
        spec_layout.visibility = View.VISIBLE
    }

    private fun setupSpecItem(@DrawableRes drawable: Int, @StringRes title: Int, info: String, view: TextView) {
        val specDrawable = getColorizedDrawable(drawable)
        view.setCompoundDrawablesWithIntrinsicBounds(specDrawable, null, null, null)
        view.text = UiUtil.applyBoldFirstWord(getString(title), info)
    }

    private fun getColorizedDrawable(@DrawableRes res: Int): Drawable? {
        val drawable = ContextCompat.getDrawable(this, res)
        drawable?.setColorFilter(iconColor, PorterDuff.Mode.SRC_ATOP)
        return drawable
    }

    private fun getColorizedDrawable(@DrawableRes res: Int, color: Int): Drawable? {
        val drawable = ContextCompat.getDrawable(this, res)
        drawable?.setColorFilter(resources.getColor(color), PorterDuff.Mode.SRC_ATOP)
        return drawable
    }
}

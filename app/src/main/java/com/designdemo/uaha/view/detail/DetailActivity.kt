package com.designdemo.uaha.view.detail

import android.annotation.TargetApi
import android.app.SearchManager
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.designdemo.uaha.data.model.VersionData
import com.designdemo.uaha.data.model.detail.DetailEntity
import com.designdemo.uaha.data.model.product.ProductEntity
import com.designdemo.uaha.data.model.wiki.WikiResponse
import com.designdemo.uaha.net.FonoApiFactory
import com.designdemo.uaha.net.WikiApiFactory
import com.designdemo.uaha.util.UiUtil
import com.google.android.material.snackbar.Snackbar
//import com.support.android.designlibdemo.BuildConfig.FONO_API_KEY
import com.support.android.designlibdemo.R
import com.support.android.designlibdemo.R.color.black
import kotlinx.android.synthetic.main.activity_detail.*
import okhttp3.OkHttpClient
import java.io.IOException

class DetailActivity : AppCompatActivity() {

    //Details Card Views
    private var iconColor = 0

    private var localProdItem: ProductEntity? = null

    private lateinit var detailViewModel: DetailViewModel

    private lateinit var okclient: OkHttpClient
    private var osVersion: Int = 0
    private var androidName = "unset"

    companion object {
        const private val TAG = "DetailActivity"

        const val EXTRA_APP_NAME = "os_name"

        // To use the FONO API, you will need to add your own API key to the gradle.properties file
        // Copy the file named gradle.properties.dist (in project base) to gradle.properties to define this variable
        // App will degrade gracefully if KEY is not found
//        const val TOKEN = FONO_API_KEY
        const val TOKEN = "NA"
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        if (androidName === "unset") {
            androidName = intent.getStringExtra(EXTRA_APP_NAME)
        }

        detailViewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)

        okclient = OkHttpClient()

        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
        }

        spec_layout.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()

        val intent = intent
        handleIntent(intent)

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition()
        }

        setupToolbar(androidName)
        setupFab()

        val product = androidName.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        detailViewModel.getProductForName(product[0]).observe(this, Observer { prodItem ->
            prodItem?.let{
                localProdItem = prodItem
                Glide.with(this).load(prodItem.imgId).centerCrop().into(backdrop)
                setFabIcon()
                setupPalette()

            }
        })

        detailViewModel.getDetailsForItem(androidName).observe(this, Observer { detailsForItem ->
            detailsForItem?.let{
                device_info_progress.visibility = View.GONE
                setDeviceInfoViews(detailsForItem)
            }
        })

        //Retrieve the phone info from net, and put in DB when found
        detailViewModel.getFonoNetInfo(androidName)
    }


    override fun onNewIntent(intent: Intent) {
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
        }
    }

    //TODO, setup this better
    private fun setupToolbar(androidName: String) {
        osVersion = VersionData.getOsNum(androidName)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        bottom_appbar.replaceMenu(R.menu.detail_actions)
        bottom_appbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_shuffle -> {
                    // Get a random Android product, and then refresh the UI
                    clearDeviceInfoViews()
                    this.androidName = VersionData.getRandomPhoneName()
                    onResume()
                    true
                }
            }
            false
        }

        //Split the and Version to display better
        val splitString = VersionData.getProductName(osVersion)
        val parts = splitString.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        collapsing_toolbar.title = parts[0]
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (hasFocus) {
                startPostponedEnterTransition()
            }
        }
    }

    private fun setupFab() {
        fab_detail.setOnClickListener { v ->
            //Toggle the value to be opposite of what it is, to set
            var toSetFav = 1
            if (localProdItem?.isFav == 1) {
                toSetFav = 0
            }

            val prodFaved = localProdItem?.copy(isFav = toSetFav)
            detailViewModel.insertFav(prodFaved!!)

            // Notify the User with a snackbar
            // Need to set a calculate a specific offset for this so it appears higher then the BottomAppBar per the specification
            val pxScreenHeight = UiUtil.getScreenHeight(this)
            val pxToolbar = UiUtil.getPxForRes(R.dimen.snackbar_offset, this)
            val pxTopOffset = pxScreenHeight - pxToolbar
            val sideOffset = UiUtil.getPxForRes(R.dimen.large_margin, this)

            val lp = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            )
            lp.topMargin = pxTopOffset.toInt()
            lp.leftMargin = sideOffset.toInt()
            lp.setMargins(lp.leftMargin, lp.topMargin, lp.rightMargin, lp.bottomMargin)

            val mainView = findViewById<View>(R.id.user_main_content)
            val snackbar = Snackbar.make(mainView, R.string.favorite_confirm, Snackbar.LENGTH_LONG)
            val snackbarLayout = snackbar.view

            // Set the Layout Params we calculated before showing the Snackbar
            snackbarLayout.layoutParams = lp
            snackbar.show()
        }
    }

    private fun setFabIcon() {
        if (localProdItem!!.isFav == 1) {
            fab_detail.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite_on))
        } else {
            fab_detail.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite_off))
        }
    }


    private fun setupPalette() {
        val bm = BitmapFactory.decodeResource(resources, VersionData.getOsDrawable(osVersion))
        try {
            Palette.from(bm).generate { palette ->
                Log.d("Palette", "Palette has been generated")
                val palVib = findViewById<TextView>(R.id.palette_vibrant)
                val perc1left = findViewById<TextView>(R.id.perc1_left)
                perc1left.setBackgroundColor(palette!!.getVibrantColor(0x000000))
                palVib.setBackgroundColor(palette!!.getVibrantColor(0x000000))

                val palVibDark = findViewById<TextView>(R.id.palette_vibrant_dark)
                val perc1mid = findViewById<TextView>(R.id.perc1_middle)
                perc1mid.setBackgroundColor(palette!!.getDarkVibrantColor(0x000000))
                palVibDark.setBackgroundColor(palette!!.getDarkVibrantColor(0x000000))

                val palVibLight = findViewById<TextView>(R.id.palette_vibrant_light)
                val perc1right = findViewById<TextView>(R.id.perc1_right)
                perc1right.setBackgroundColor(palette!!.getLightVibrantColor(0x000000))
                palVibLight.setBackgroundColor(palette!!.getLightVibrantColor(0x000000))

                val palMuted = findViewById<TextView>(R.id.palette_muted)
                val perc2left = findViewById<TextView>(R.id.perc2_left)
                perc2left.setBackgroundColor(palette!!.getMutedColor(0x000000))
                palMuted.setBackgroundColor(palette!!.getMutedColor(0x000000))

                val palMutedDark = findViewById<TextView>(R.id.palette_muted_dark)
                val perc2mid = findViewById<TextView>(R.id.perc2_middle)
                perc2mid.setBackgroundColor(palette!!.getDarkMutedColor(0x000000))
                palMutedDark.setBackgroundColor(palette!!.getDarkMutedColor(0x000000))

                val palMutedLight = findViewById<TextView>(R.id.palette_muted_light)
                val perc2right = findViewById<TextView>(R.id.perc2_right)
                perc2right.setBackgroundColor(palette!!.getLightMutedColor(0x000000))
                palMutedLight.setBackgroundColor(palette!!.getLightMutedColor(0x000000))

                //Noticed the Expanded white doesn't show everywhere, use Palette to fix this
                collapsing_toolbar.setExpandedTitleColor(palette!!.getVibrantColor(0x000000))

                iconColor = if (palette!!.getVibrantColor(0x000000) == 0x000000) {
                    palette!!.getMutedColor(0x000000)
                } else {
                    palette!!.getVibrantColor(0x000000)
                }


                //The back button should also have a better color applied to ensure it is visible,
                // Get a swatch, and get a more specific color for title from it.
                val vibrantSwatch = palette!!.vibrantSwatch
                var arrowColor = ContextCompat.getColor(this, R.color.black)
                if (vibrantSwatch != null) {
                    arrowColor = vibrantSwatch!!.titleTextColor
                }

                val res = resources.getIdentifier("abc_ic_ab_back_material", "drawable", packageName)
                val upArrow = getColorizedDrawable(res)
                supportActionBar?.setHomeAsUpIndicator(upArrow)
                collapsing_toolbar.setCollapsedTitleTextColor(arrowColor)
            }
        } catch (e: Exception) {
            Log.e("DetailActivity", "Error with image! + ${e.toString()}", e)
        }
    }


    private fun setDeviceInfoViews(detailItem: DetailEntity) {
        details_device_name.text = detailItem.DeviceName
        details_device_name.setTextColor(iconColor)
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
        device_info_progress.visibility = View.VISIBLE
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

}

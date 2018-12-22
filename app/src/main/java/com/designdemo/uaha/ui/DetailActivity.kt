package com.designdemo.uaha.ui

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
import android.webkit.WebView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.designdemo.uaha.data.DeviceEntity
import com.designdemo.uaha.data.VersionData
import com.designdemo.uaha.data.wiki.WikiResponse
import com.designdemo.uaha.net.FonoApiFactory
import com.designdemo.uaha.net.WikiApiFactory
import com.designdemo.uaha.util.PrefsUtil
import com.designdemo.uaha.util.UiUtil
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.support.android.designlibdemo.R
import com.support.android.designlibdemo.R.color.black
import kotlinx.android.synthetic.main.activity_detail.*
import okhttp3.OkHttpClient
import java.io.IOException

class DetailActivity : AppCompatActivity() {
    private lateinit var regsWv: WebView
    private lateinit var fab: FloatingActionButton
    private lateinit var collapsingToolbar: CollapsingToolbarLayout

    //Details Card Views
    private var iconColor = 0
    private lateinit var specLayout: LinearLayout
    private lateinit var deviceName: TextView
    private lateinit var features: TextView
    private lateinit var featuresCont: TextView
    private lateinit var releaseDate: TextView
    private lateinit var multitouch: TextView
    private lateinit var weight: TextView
    private lateinit var size: TextView
    private lateinit var dimen: TextView
    private lateinit var cpu: TextView
    private lateinit var memory: TextView
    private lateinit var camera: TextView
    private lateinit var display: TextView
    private lateinit var resolution: TextView
    private lateinit var video: TextView
    private lateinit var progress: ProgressBar

    private var deviceInfo: DeviceEntity? = null

    private lateinit var okclient: OkHttpClient
    private var osVersion: Int = 0
    private var androidName = "unset"

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
    }

    override fun onResume() {
        super.onResume()

        val intent = intent
        handleIntent(intent)

        if (androidName === "unset") {
            androidName = intent.getStringExtra(EXTRA_APP_NAME)
        }

        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
        }

        okclient = OkHttpClient()

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition()
        }

        setupToolbar(androidName)
        loadBackdrop()
        setupViews()
        setupFab()
        setupDetailsInfo()
        setupPalette()
        InfoGatherTask().execute()
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

    private fun setupToolbar(androidName: String) {
        osVersion = VersionData.getOsNum(androidName)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val bottomAppBar = findViewById<BottomAppBar>(R.id.bottom_appbar)
        bottomAppBar.replaceMenu(R.menu.detail_actions)
        bottomAppBar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_shuffle -> {
                    // Get a random Android product, and then refresh the UI
                    this.androidName = VersionData.getRandomPhoneName()
                    onResume()
                    true
                }
            }
            false
        }

        collapsingToolbar = collapsing_toolbar

        //Split the and Version to display better
        val splitString = VersionData.getProductName(osVersion)
        val parts = splitString.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        collapsingToolbar.title = parts[0]
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

    private fun loadBackdrop() {
        val imageView = findViewById<View>(R.id.backdrop) as ImageView
        Glide.with(this).load(VersionData.getOsDrawable(osVersion)).centerCrop().into(imageView)
    }

    private fun setupViews() {
        regsWv = regulations_webview
    }


    private fun setupFab() {
        fab = fab_detail

        // Set initial state based on pref
        setFabIcon()

        // To over-ride the color of the FAB other then the theme color
        //fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fab_selector)));
        fab.setOnClickListener { v ->
            PrefsUtil.toggleFavorite(applicationContext, osVersion)
            setFabIcon()

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
        if (PrefsUtil.isFavorite(applicationContext, osVersion)) {
            fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite_on))
        } else {
            fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite_off))
        }
    }


    private fun setupPalette() {
        val bm = BitmapFactory.decodeResource(resources, VersionData.getOsDrawable(osVersion))
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
            collapsingToolbar.setExpandedTitleColor(palette!!.getVibrantColor(0x000000))

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
            collapsingToolbar.setCollapsedTitleTextColor(arrowColor)
        }
    }

    private fun setupDetailsInfo() {
        deviceName = details_device_name
        features = details_features
        featuresCont = details_features_c
        releaseDate = spec_releasedate
        specLayout = spec_layout
        multitouch = spec_multitouch
        weight = spec_weight
        dimen = spec_dimen
        cpu = spec_cpu
        memory = spec_memory
        camera = spec_camera
        display = spec_display
        resolution = spec_resolution
        video = spec_video
        size = spec_size
        progress = device_info_progress
    }

    private fun setDeviceInfoViews() {
        if (deviceInfo != null) {
            device_info_progress.visibility = View.GONE
            deviceName.text = deviceInfo!!.deviceName
            deviceName.setTextColor(iconColor)
            features.text = deviceInfo!!.features
            featuresCont.text = deviceInfo!!.features_c

            if (deviceInfo!!.multitouch != null)
                setupSpecItem(R.drawable.vct_multitouch, R.string.spec_multitouch, deviceInfo!!.multitouch, multitouch)
            else
                setupSpecItem(R.drawable.vct_multitouch, R.string.spec_multitouch, "NA", multitouch)

            val cpuInfo = deviceInfo!!.chipset + "\n" + deviceInfo!!.cpu
            setupSpecItem(R.drawable.vct_cpu, R.string.spec_cpu, cpuInfo, cpu)

            if (deviceInfo!!.announced != null)
                setupSpecItem(R.drawable.vct_date, R.string.spec_release_date, deviceInfo!!.announced, releaseDate)

            if (deviceInfo!!.size != null)
                setupSpecItem(R.drawable.vct_size, R.string.spec_size, deviceInfo!!.size, size)

            if (deviceInfo!!.weight != null)
                setupSpecItem(R.drawable.vct_weight, R.string.spec_weight, deviceInfo!!.weight, weight)

            if (deviceInfo!!.dimensions != null)
                setupSpecItem(R.drawable.vct_dimen, R.string.spec_dimen, deviceInfo!!.dimensions, dimen)

            if (deviceInfo!!.internal != null)
                setupSpecItem(R.drawable.vct_memory, R.string.spec_memory, deviceInfo!!.internal, memory!!)

            if (deviceInfo!!.primary_ != null)
                setupSpecItem(R.drawable.vct_camera, R.string.spec_camera, deviceInfo!!.primary_, camera!!)

            if (deviceInfo!!.video != null)
                setupSpecItem(R.drawable.vct_video, R.string.spec_video, deviceInfo!!.video, video!!)

            if (deviceInfo!!.type != null)
                setupSpecItem(R.drawable.vct_display, R.string.spec_display, deviceInfo!!.type, display!!)

            if (deviceInfo!!.resolution != null)
                setupSpecItem(R.drawable.vct_resolution, R.string.spec_resolution, deviceInfo!!.resolution, resolution!!)

            specLayout.visibility = View.VISIBLE
        } else {
            specLayout.visibility = View.GONE
        }
    }


    private fun clearDeviceInfoViews() {
        device_info_progress.visibility = View.VISIBLE
        deviceName.text = getString(R.string.getting_information)
        deviceName.setTextColor(resources.getColor(black))
        features.text = ""
        featuresCont.text = ""

        setupSpecItem(R.drawable.vct_multitouch, R.string.spec_multitouch, "", multitouch)
        setupSpecItem(R.drawable.vct_cpu, R.string.spec_cpu, "", cpu)
        setupSpecItem(R.drawable.vct_date, R.string.spec_release_date, "", releaseDate)
        setupSpecItem(R.drawable.vct_size, R.string.spec_size, "", size)
        setupSpecItem(R.drawable.vct_weight, R.string.spec_weight, "", weight)
        setupSpecItem(R.drawable.vct_dimen, R.string.spec_dimen, "", dimen)
        setupSpecItem(R.drawable.vct_memory, R.string.spec_memory, "", memory)
        setupSpecItem(R.drawable.vct_camera, R.string.spec_camera, "", camera)
        setupSpecItem(R.drawable.vct_video, R.string.spec_video, "", video)
        setupSpecItem(R.drawable.vct_display, R.string.spec_display, "", display)
        setupSpecItem(R.drawable.vct_resolution, R.string.spec_resolution, "", resolution)
        specLayout.visibility = View.VISIBLE
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


    private inner class InfoGatherTask : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg params: String): String {
            var respStr = ""
            val api = FonoApiFactory().create()
            var response: retrofit2.Response<List<DeviceEntity>>? = null

            val product = VersionData.getProductName(osVersion).split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val deviceName = product[0]
            val manufacturer = product[1]
            try {
                response = api.getDevice(TOKEN, deviceName, manufacturer, null).execute()
                val devices = response!!.body()
                if (devices != null) {
                    for (dv in devices) {
                        deviceInfo = dv
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            val wikiService = WikiApiFactory().create()
            var wikiResponse: retrofit2.Response<WikiResponse>?= null
            try {
                wikiResponse = wikiService.getWikiResponse(deviceName, "revisions", "json", "content").execute()
                var result = wikiResponse!!.body()
                if (result != null) {

                }


            } catch (ioe: IOException) {
                ioe.printStackTrace()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

            return respStr
        }

        override fun onPostExecute(result: String) {
            regsWv.loadData(result, "text/html", null)
            setDeviceInfoViews()
        }

        override fun onPreExecute() {
            clearDeviceInfoViews()
        }

        override fun onProgressUpdate(vararg values: Void) {}
    }

    companion object {
        const private val TAG = "DetailActivity"

        const val EXTRA_APP_NAME = "os_name"

        // To use the FONO API, you will need to add your own API key to the gradle.properties file
        // Copy the file named gradle.properties.dist (in project base) to gradle.properties to define this variable
        // App will degrade gracefully if KEY is not found
//        const private val TOKEN = FONO_API_KEY
        private val TOKEN = "NA"
    }
}

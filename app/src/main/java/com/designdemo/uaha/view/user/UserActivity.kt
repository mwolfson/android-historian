package com.designdemo.uaha.view.user

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.work.WorkInfo
import com.designdemo.uaha.data.model.user.UserEntity
import com.designdemo.uaha.util.PEEK_HEIGHT_PIXEL
import com.designdemo.uaha.util.ROTATION_180
import com.designdemo.uaha.util.UiUtil
import com.designdemo.uaha.view.demo.BottomNavActivity
import com.designdemo.uaha.view.product.ProductActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.from
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.support.android.designlibdemo.R
import kotlinx.android.synthetic.main.activity_user.*
import kotlinx.android.synthetic.main.dialog_picture.view.*
import kotlinx.android.synthetic.main.dialog_textscale.view.*

class UserActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "UserActivity"
    }

    private var mainActivity: Activity? = null

    private lateinit var userViewModel: UserViewModel

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>

    // Local copy of usersInfo list, to be used when applying Undo button in snackbar
    private var users = listOf<UserEntity>()

    // Lambda to add a close listener on the chip, and also put a random background color
    val setChipCloseAndRandomColor: (Chip) -> Unit = {
        it.setOnCloseIconClickListener { chipIn -> chipIn.visibility = View.GONE }
        it.setChipBackgroundColorResource(UiUtil.getRandomColor())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        mainActivity = this

        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)

        userViewModel.allUserEntity.observe(this, Observer { userInfo ->
            users = userInfo

            if (users.isEmpty()) {
                name_edit?.setText("")
                phone_edit?.setText("")
                password_edit?.setText("")
            } else {
                val userInfo = users.last()

                name_edit?.setText(userInfo.name)
                phone_edit?.setText(userInfo.phone)
                password_edit?.setText(userInfo.password)
            }
        })

        // Status results from an update attempt (validation errors handled here)
        userViewModel.getAddUserStatus().observe(this, Observer { statusInt ->
            when (statusInt) {
                R.string.name_input_error -> {
                    name_edit?.error = getString(statusInt)
                    name_edit?.requestFocus()
                    showSnackbar(statusInt)
                }
                R.string.phone_input_error -> {
                    phone_edit?.error = getString(statusInt)
                    phone_edit?.requestFocus()
                    showSnackbar(statusInt)
                }
                R.string.invalid_password -> {
                    password_edit?.error = getString(statusInt)
                    password_edit?.requestFocus()
                    showSnackbar(statusInt)
                }
                R.string.profile_saved_confirm -> {
                    // If there is a value to reset show snackbar with undo option
                    val sizeOfList = users.size
                    if (sizeOfList > 1) {
                        val oldUserInfo = users[sizeOfList - 2]

                        // Show snackbar, and include the option to Undo the previous operation
                        val snackbar = Snackbar.make(user_main_coordinator, getString(statusInt), Snackbar.LENGTH_LONG)
                                .setAction(getString(R.string.undo)) {
                                    userViewModel.addUserData(oldUserInfo)
                                }
                        snackbar.anchorView = user_fab
                        snackbar.show()
                    } else {
                        // No backup available, so don't show undo option
                        showSnackbar(statusInt)
                    }
                }
                else -> Log.d("AddUserError", "Unexpected status message returned: $statusInt")
            }
        })

        // Setup BottomAppBar
        setSupportActionBar(bottom_appbar)
        bottom_appbar.replaceMenu(R.menu.profile_actions)

        val ab = supportActionBar
        ab?.setHomeAsUpIndicator(R.drawable.vct_menu)
        ab?.setDisplayHomeAsUpEnabled(true)

        setupViews()
        setupTextScaleDialog()
        setupChips()

        val navigationView = nav_view
        if (navigationView != null) {
            setupDrawerContent(navigationView)
            val headerView = navigationView.getHeaderView(0)
            if (headerView != null) {
                val versionText = headerView.findViewById<TextView>(R.id.header_versioninfo)
                versionText.text = UiUtil.versionInfo(this)

                val appTitleText = headerView.findViewById<TextView>(R.id.header_apptitle)
                appTitleText.setOnClickListener {
                    val playStore = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=com.ableandroid.historian"))
                    startActivity(playStore)
                }
            }
        }

        // Observe status of Notification Worker
        userViewModel.outputWorkInfos.observe(this, workInfosObserver())
    }

    private fun workInfosObserver(): Observer<List<WorkInfo>> {
        return Observer { listOfWorkInfo ->
            // If there are no matching work info, do nothing
            if (listOfWorkInfo.isNullOrEmpty()) {
                return@Observer
            }

            val workInfo = listOfWorkInfo[0]

            Log.d("UserActivity", "Work info state is: ${workInfo.state}")

            if (workInfo.state == WorkInfo.State.ENQUEUED) {
                Log.d("UserActivity", "IS ENQUEUED")
//                notifstart_button_start.isEnabled = false
//                notifstart_button_end.isEnabled = true
            } else {
                Log.d("UserActivity", "IS not scheduled")
//                notifstart_button_start.isEnabled = true
//                notifstart_button_end.isEnabled = false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.profile_actions, menu)
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

    private fun setupViews() {
        // Format phone number as user is typing
        phone_edit.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        profile_pic_button?.setOnClickListener { setPictureDialog() }

        user_fab?.setOnClickListener {
            val userInfo = UserEntity(name_edit.text.toString(), phone_edit.text.toString(), password_edit.text.toString())
            userViewModel.addUserData(userInfo)
        }

        chip_userinfo_label.requestFocus()

        notifstart_button_start?.setOnClickListener {
            userViewModel.startNotif()
        }

        notifstart_button_end?.setOnClickListener {
            userViewModel.cancelNotif()
        }

        darkmode_group.setOnCheckedChangeListener { _, radioClicked ->
            userViewModel.setDarkMode(radioClicked, this)
        }

        val currentNightMode = AppCompatDelegate.getDefaultNightMode()
        when (currentNightMode) {
            MODE_NIGHT_NO -> darkmode_group.check(R.id.radio_light)
            MODE_NIGHT_YES -> darkmode_group.check(R.id.radio_dark)
            else -> darkmode_group.check(R.id.radio_setting)
        }
    }

    private fun showSnackbar(@StringRes displayString: Int) {
        val snackbar = Snackbar.make(user_main_scroll_layout, getString(displayString), Snackbar.LENGTH_SHORT)
        // Need to set a calculate a specific offset for this so it appears higher then the BottomAppBar per spec
        snackbar.anchorView = user_fab
        snackbar.show()
    }

    private fun setupChips() {
        val chipEntry1 = chip_entry1
        setChipCloseAndRandomColor(chipEntry1)

        val chipEntry2 = chip_entry2
        setChipCloseAndRandomColor(chipEntry2)

        val chipEntry3 = chip_entry3
        setChipCloseAndRandomColor(chipEntry3)

        val chipEntry4 = chip_entry4
        setChipCloseAndRandomColor(chipEntry4)

        val chipEntry5 = chip_entry5
        setChipCloseAndRandomColor(chipEntry5)

        val filter1Group = filter1_group
        filter1Group.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.choice_item1 -> Log.d(TAG, "Filter1 Item 1")
                R.id.choice_item2 -> Log.d(TAG, "Filter1 Item 2")
                R.id.choice_item3 -> Log.d(TAG, "Filter1 Item 3")
            }
        }

        val filter2Group = filter2_group
        filter2Group.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.filter2_item1 -> Log.d(TAG, "Filter2 Item 1")
                R.id.filter2_item2 -> Log.d(TAG, "Filter2 Item 2")
                R.id.filter2_item3 -> Log.d(TAG, "Filter2 Item 3")
                R.id.filter2_item4 -> Log.d(TAG, "Filter2 Item 4")
                R.id.filter2_item5 -> Log.d(TAG, "Filter2 Item 5")
                R.id.filter2_item6 -> Log.d(TAG, "Filter2 Item 6")
                R.id.filter2_item7 -> Log.d(TAG, "Filter2 Item 7")
            }
        }

        val customChipEdit = chip_edit

        val entryGroup = chipgroup_entry

        val activity = this

        customChipEdit.setOnEditorActionListener { _, _, _ ->
            saveChipEntry(customChipEdit, activity, entryGroup)
            false
        }

        val chipActionCustom = chip_action_custom
        chipActionCustom.setOnClickListener {
            saveChipEntry(customChipEdit, activity, entryGroup)
        }
    }

    private fun saveChipEntry(customChipEdit: EditText, activity: Activity, entryGroup: ChipGroup) {
        val textEntered = customChipEdit.text.toString()
        val dynamicChip = Chip(activity)
        dynamicChip.text = textEntered
        dynamicChip.isCloseIconVisible = true
        dynamicChip.isCheckable = true

        // Set a random icon for demo
        dynamicChip.chipIcon = ContextCompat.getDrawable(this, UiUtil.getRandomDrawable())
        setChipCloseAndRandomColor(dynamicChip)

        entryGroup.addView(dynamicChip)
        customChipEdit.requestFocus()
        customChipEdit.setText("")
    }

    private fun setupTextScaleDialog() {
        val bottomSheet = bottom_sheet
        bottomSheetBehavior = from(bottomSheet)

        val closeButton = textscale_close
        closeButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            bottomSheetBehavior.setPeekHeight(0)
        }

        val showHide = show_bottom_sheet
        showHide.setOnClickListener { bottomSheetBehavior.setPeekHeight(PEEK_HEIGHT_PIXEL) }

        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheetBehavior.peekHeight = 0
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                closeButton.rotation = slideOffset * ROTATION_180
            }
        })

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetBehavior.peekHeight = 0
    }

    /**
     * When an item is clicked, this will launch an Alert Dialog with information specific to that item
     *
     * @param view
     */
    fun scaleTextItemClicked(view: View) {
        val temp = view as TextView
        val scaleText = temp.text.toString()
        var valueToSet = "No Value"

        // Sets custom text in the dialog
        val dialogView = layoutInflater.inflate(R.layout.dialog_textscale, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)

        val caseText = dialogView.ts_textcase
        val fontText = dialogView.ts_font
        val sizeText = dialogView.ts_size
        val letterSpacingText = dialogView.ts_letter_spacing

        caseText.text = UiUtil.applySpecialFormatting(getString(R.string.case_text), getString(R.string.sentence))
        fontText.text = UiUtil.applySpecialFormatting(getString(R.string.font_text), getString(R.string.regular))

        // Creates special strings to display Scale Type information in the dialog when you selected an item
        val setupTextScaleType: (Int, Int, Int) -> Int = { val1, val2, val3 ->
            valueToSet = getString(val1)
            letterSpacingText.text = UiUtil.applySpecialFormatting(getString(R.string.letter_spacing), getString(val2))
            sizeText.text = UiUtil.applySpecialFormatting(getString(R.string.size), getString(val3))
            0
        }

        when (scaleText) {
            "Headline1" -> {
                setupTextScaleType(R.string.st_h1, R.string.ls_neg1_5, R.string.sp_96)
                fontText.text = UiUtil.applySpecialFormatting(getString(R.string.font_text), getString(R.string.light_font))
            }
            "Headline2" -> {
                setupTextScaleType(R.string.st_h2, R.string.ls_neg5, R.string.sp_60)
                fontText.text = UiUtil.applySpecialFormatting(getString(R.string.font_text), getString(R.string.light_font))
            }
            "Headline3" -> setupTextScaleType(R.string.st_h3, R.string.ls_zero, R.string.sp_48)
            "Headline4" -> setupTextScaleType(R.string.st_h4, R.string.ls_25, R.string.sp_34)
            "Headline5" -> setupTextScaleType(R.string.st_h5, R.string.ls_zero, R.string.sp_24)
            "Headline6" -> {
                setupTextScaleType(R.string.st_h6, R.string.ls_15, R.string.sp_20)
                fontText.text = UiUtil.applySpecialFormatting(getString(R.string.font_text), getString(R.string.medium))
            }
            "Subtitle1" -> setupTextScaleType(R.string.st_subtitle1, R.string.ls_15, R.string.sp_16)
            "Subtitle2" -> setupTextScaleType(R.string.st_subtitle2, R.string.ls_1, R.string.sp_14)
            "Body1" -> setupTextScaleType(R.string.st_body1, R.string.ls_5, R.string.sp_16)
            "Body2" -> setupTextScaleType(R.string.st_body2, R.string.ls_25, R.string.sp_14)
            "Button" -> {
                setupTextScaleType(R.string.st_button, R.string.ls_75, R.string.sp_14)
                caseText.text = UiUtil.applySpecialFormatting(getString(R.string.case_text),
                        getString(R.string.all_caps))
                fontText.text = UiUtil.applySpecialFormatting(getString(R.string.font_text), getString(R.string.medium))
            }
            "Caption" -> setupTextScaleType(R.string.st_caption, R.string.ls_4, R.string.sp_12)
            "Overline" -> {
                setupTextScaleType(R.string.st_overline, R.string.ls_1dot5, R.string.sp_10)
                caseText.text = UiUtil.applySpecialFormatting(
                        getString(R.string.case_text),
                        getString(R.string.all_caps))
            }
            else -> {
                valueToSet = "Unset"
                caseText.text = UiUtil.applySpecialFormatting(getString(R.string.case_text), getString(R.string.unset))
                sizeText.text = UiUtil.applySpecialFormatting(getString(R.string.size), getString(R.string.unset))
            }
        }

        builder.setMessage(getString(R.string.text_appearance_style_example, valueToSet))
        builder.setTitle(valueToSet)
        builder.create()
        builder.show()
    }

    private fun setPictureDialog() {
        val photoDialog: AlertDialog
        val builder = AlertDialog.Builder(this)
        val inflater = mainActivity!!.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_picture, null)
        builder.setView(dialogView)
        builder.setTitle(mainActivity!!.getString(R.string.picture_dialog_title))
        builder.setCancelable(true)
        builder.setPositiveButton(mainActivity!!.getString(R.string.picture_dialog_button)) { _, _ ->
            Log.d("Dialog", "Positive button pressed") }

        val prefSwitch = dialogView.photo_pref_switch
        prefSwitch.isChecked = true
        prefSwitch.setOnClickListener {
            if (prefSwitch.isChecked) {
                Log.d(TAG, "The Photo switch was enabled")
            } else {
                Log.d(TAG, "The Photo switch was disabled")
            }
        }

        photoDialog = builder.create()
        photoDialog.show()
    }

    private fun setupDrawerContent(navigationView: NavigationView) {
        navigationView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    val osIntent = Intent(applicationContext, ProductActivity::class.java)
                    osIntent.putExtra(ProductActivity.EXTRA_FRAG_TYPE, ProductActivity.OS_FRAG)
                    startActivity(osIntent)
                }
                R.id.nav_devices -> {
                    val deviceIntent = Intent(applicationContext, ProductActivity::class.java)
                    deviceIntent.putExtra(ProductActivity.EXTRA_FRAG_TYPE, ProductActivity.DEVICE_FRAG)
                    startActivity(deviceIntent)
                }
                R.id.nav_favorites -> {
                    val favIntent = Intent(applicationContext, ProductActivity::class.java)
                    favIntent.putExtra(ProductActivity.EXTRA_FRAG_TYPE, ProductActivity.FAV_FRAG)
                    startActivity(favIntent)
                }
                R.id.nav_userinfo -> drawer_layout.closeDrawers()
                R.id.nav_homepage -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://www.ableandroid.com/")))
                R.id.nav_playlink -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/apps/testing/com.ableandroid.historian")))
                R.id.nav_githublink -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/mwolfson/android-historian")))
                R.id.nav_link1 -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.android.com/")))
                R.id.nav_link2 -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://material.io/")))
            }
            true
        }
    }
}
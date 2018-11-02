package com.designdemo.uaha.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.telephony.PhoneNumberUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView

import com.designdemo.uaha.util.PrefsUtil
import com.designdemo.uaha.util.UiUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.support.android.designlibdemo.R

import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import kotlinx.android.synthetic.main.activity_user.*
import kotlinx.android.synthetic.main.dialog_picture.view.*
import kotlinx.android.synthetic.main.dialog_textscale.view.*

class UserActivity : AppCompatActivity() {
    private var mainActivity: Activity? = null

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var nameEnterField: AppCompatEditText
    private lateinit var phoneEnterField: AppCompatEditText
    private lateinit var fab: FloatingActionButton
    private lateinit var picButton: Button
    private lateinit var userLabelChip: Chip

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>

    // Lambda to add a close listener on the chip, and also put a random background color
    val setChipCloseAndRandomColor: (Chip) -> Unit = {
        it.setOnCloseIconClickListener { it -> it.visibility = View.GONE }
        it.setChipBackgroundColorResource(UiUtil.getRandomColor())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        mainActivity = this

        val bottomAppBar = bottom_appbar
        setSupportActionBar(bottomAppBar)
        bottomAppBar.replaceMenu(R.menu.profile_actions)

        val ab = supportActionBar
        ab?.setHomeAsUpIndicator(R.drawable.ic_menu)
        ab?.setDisplayHomeAsUpEnabled(true)

        setupViews()
        setupTextScaleDialog()
        setupChips()

        val navigationView = nav_view
        setupDrawerContent(navigationView)
    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.profile_actions, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupViews() {
        drawerLayout = drawer_layout
        nameEnterField = name_edit
        phoneEnterField = phone_edit
        userLabelChip = chip_userinfo_label

        //Format phone number as user is typing
        phoneEnterField.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        picButton = profile_pic_button
        picButton.setOnClickListener { v -> setPictureDialog() }

        val mainView = user_main_content

        //TODO - this validation could use a refactor to cleanup its order of operations and duplicated code
        //TODO Also, the Screen calculation stuff is duplicated on the DetailActivity
        // Lambda to check is the name length fits our requirement
        val isNameValid: (TextView) -> Int = {
            var retVal = 0
            val nameLen = it.text?.length

            if (nameLen in 0..3) {
                retVal = R.string.at_least_4_char
            }
            retVal
        }

        //Lambda to check if phone number is valid
        val isPhoneValid: (TextView) -> Int = {
            var retVal = 0
            val phoneLen = phoneEnterField.text?.length
            if (phoneLen != 14) {
                retVal = R.string.invalid_phone
            }
            retVal
        }

        // Setup Snackbar, including special accomidations for BottomAppBar
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

        fab = user_fab
        fab.setOnClickListener { v ->
            //Validate values
            val nameError = isNameValid(nameEnterField)
            if (nameError != 0) {
                nameEnterField.error = getString(nameError)
                nameEnterField.requestFocus()
                val snackbar = Snackbar.make(mainView, getString(R.string.name_input_error), Snackbar.LENGTH_SHORT)
                val snackbarLayout = snackbar.view
                snackbarLayout.layoutParams = lp
                snackbar.show()
            }

            val phoneError = isPhoneValid(phoneEnterField)
            if (phoneError != 0) {
                phoneEnterField.error = getString(phoneError)
                phoneEnterField.requestFocus()
                val snackbar = Snackbar.make(mainView, getString(R.string.phone_input_error), Snackbar.LENGTH_SHORT)
                val snackbarLayout = snackbar.view
                snackbarLayout.layoutParams = lp
                snackbar.show()
            }

            // Save original Values before sending, in-case user changes their mind
            val beforeName = PrefsUtil.getName(mainActivity!!.applicationContext)
            val beforePhone = PrefsUtil.getPhone(mainActivity!!.applicationContext)

            // Store new values
            val nameToSet = nameEnterField.text.toString()
            val formattedNum = PhoneNumberUtils.stripSeparators(phoneEnterField.text.toString())
            val phoneToSet = java.lang.Long.valueOf(formattedNum)

            PrefsUtil.setProfile(mainActivity!!.applicationContext, nameToSet, phoneToSet)

            val snackbar = Snackbar.make(mainView, getString(R.string.profile_saved_confirm), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.undo)) { _ ->
                        // Reset to original
                        val complete = PrefsUtil.setProfile(mainActivity!!.applicationContext, beforeName!!, beforePhone)
                        if (complete) {
                            setPhoneNameValues()
                        }
                    }
            val snackbarLayout = snackbar.view
            snackbarLayout.layoutParams = lp
            snackbar.show()
        }

        // Set initial values from Prefs
        setPhoneNameValues()
        userLabelChip.requestFocus()
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
        filter1Group.setOnCheckedChangeListener { chipGroup, i ->
            when (i) {
                R.id.choice_item1 -> Log.d(TAG, "Filter1 Item 1")
                R.id.choice_item2 -> Log.d(TAG, "Filter1 Item 2")
                R.id.choice_item3 -> Log.d(TAG, "Filter1 Item 3")
            }
        }

        val filter2Group = filter2_group
        filter2Group.setOnCheckedChangeListener { chipGroup, i ->
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

        customChipEdit.setOnEditorActionListener { textView, i, keyEvent ->
            saveChipEntry(customChipEdit, activity, entryGroup)
            false
        }

        val chipActionCustom = chip_action_custom
        chipActionCustom.setOnClickListener { view ->
            saveChipEntry(customChipEdit, activity, entryGroup)
        }

    }

    private fun saveChipEntry(customChipEdit: EditText, activity: Activity, entryGroup: ChipGroup) {
        val textEntered = customChipEdit.text.toString()
        val dynamicChip = Chip(activity)
        dynamicChip.text = textEntered
        dynamicChip.isCloseIconVisible = true
        dynamicChip.isCheckable = true

        //Set a random icon for demo
        dynamicChip.chipIcon = ContextCompat.getDrawable(this, UiUtil.getRandomDrawable())
        setChipCloseAndRandomColor(dynamicChip)

        entryGroup.addView(dynamicChip)
        customChipEdit.requestFocus()
        customChipEdit.setText("")
    }

    private fun setupTextScaleDialog() {
        val bottomSheet = bottom_sheet
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)

        val closeButton = textscale_close
        closeButton.setOnClickListener { view ->
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            bottomSheetBehavior.setPeekHeight(0)
        }

        val showHide = show_bottom_sheet
        showHide.setOnClickListener { view -> bottomSheetBehavior.setPeekHeight(300) }

        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheetBehavior.peekHeight = 0
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                closeButton.rotation = slideOffset * -180
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

        //Sets custom text in the dialog
        val dialogView = layoutInflater.inflate(R.layout.dialog_textscale, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)

        val caseText = dialogView.ts_textcase
        val fontText = dialogView.ts_font
        val sizeText = dialogView.ts_size
        val letterSpacingText = dialogView.ts_letter_spacing

        caseText.text = UiUtil.applySpecialFormatting(getString(R.string.case_text), getString(R.string.sentence))
        fontText.text = UiUtil.applySpecialFormatting(getString(R.string.font_text), getString(R.string.regular))

        //Creates special strings to display Scale Type information in the dialog when you selected an item
        val setupTextScaleType : (Int, Int, Int) -> Int = {
            val1, val2, val3 ->
            valueToSet = getString(val1)
            letterSpacingText.text = UiUtil.applySpecialFormatting(getString(R.string.letter_spacing), getString(val2))
            sizeText.text = UiUtil.applySpecialFormatting(getString(R.string.size), getString(val3))
            0
        }

        when (scaleText) {
            "Headline1" -> {
                setupTextScaleType(R.string.st_h1, R.string.ls_neg1_5, R.string.sp_96 )
                fontText.text = UiUtil.applySpecialFormatting(getString(R.string.font_text), getString(R.string.light))
            }
            "Headline2" -> {
                setupTextScaleType(R.string.st_h2, R.string.ls_neg5, R.string.sp_60 )
                fontText.text = UiUtil.applySpecialFormatting(getString(R.string.font_text), getString(R.string.light))
            }
            "Headline3" -> {
                setupTextScaleType(R.string.st_h3, R.string.ls_zero, R.string.sp_48 )
            }
            "Headline4" -> {
                setupTextScaleType(R.string.st_h4, R.string.ls_25, R.string.sp_34 )
            }
            "Headline5" -> {
                setupTextScaleType(R.string.st_h5, R.string.ls_zero, R.string.sp_24 )
            }
            "Headline6" -> {
                setupTextScaleType(R.string.st_h6, R.string.ls_15, R.string.sp_20 )
                fontText.text = UiUtil.applySpecialFormatting(getString(R.string.font_text), getString(R.string.medium))
            }
            "Subtitle1" -> {
                setupTextScaleType(R.string.st_subtitle1, R.string.ls_15, R.string.sp_16 )
            }
            "Subtitle2" -> {
                setupTextScaleType(R.string.st_subtitle2, R.string.ls_1, R.string.sp_14 )
            }
            "Body1" -> {
                setupTextScaleType(R.string.st_body1, R.string.ls_5, R.string.sp_16 )
            }
            "Body2" -> {
                setupTextScaleType(R.string.st_body2, R.string.ls_25, R.string.sp_14 )
            }
            "Button" -> {
                setupTextScaleType(R.string.st_button, R.string.ls_75, R.string.sp_14 )
                caseText.text = UiUtil.applySpecialFormatting(getString(R.string.case_text), getString(R.string.all_caps))
                fontText.text = UiUtil.applySpecialFormatting(getString(R.string.font_text), getString(R.string.medium))
            }
            "Caption" -> {
                setupTextScaleType(R.string.st_caption, R.string.ls_4, R.string.sp_12 )
            }
            "Overline" -> {
                setupTextScaleType(R.string.st_overline, R.string.ls_1dot5, R.string.sp_10 )
                caseText.text = UiUtil.applySpecialFormatting(getString(R.string.case_text), getString(R.string.all_caps))
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


    private fun setPhoneNameValues() {
        val name = PrefsUtil.getName(this)
        if (name != PrefsUtil.PREFS_NAME_UNSET) {
            nameEnterField.setText(name)
        }

        val phone = PrefsUtil.getPhone(this)
        if (phone != 0L) {
            phoneEnterField.setText(phone.toString())
        }
    }

    private fun setPictureDialog() {
        val photoDialog: AlertDialog
        val builder = AlertDialog.Builder(this)
        val inflater = mainActivity!!.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_picture, null)
        builder.setView(dialogView)
        builder.setTitle(mainActivity!!.getString(R.string.picture_dialog_title))
        builder.setCancelable(true)
        builder.setPositiveButton(mainActivity!!.getString(R.string.picture_dialog_button)) { dialog, which -> Log.d("Dialog", "The positive button was pressed") }

        val prefSwitch = dialogView.photo_pref_switch
        prefSwitch.isChecked = true
        prefSwitch.setOnClickListener { v ->
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
            var retVal = false
            menuItem.isChecked = true
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    val osIntent = Intent(applicationContext, MainActivity::class.java)
                    osIntent.putExtra(MainActivity.EXTRA_FRAG_TYPE, MainActivity.OS_FRAG)
                    startActivity(osIntent)
                    retVal = true
                }
                R.id.nav_devices -> {
                    val deviceIntent = Intent(applicationContext, MainActivity::class.java)
                    deviceIntent.putExtra(MainActivity.EXTRA_FRAG_TYPE, MainActivity.DEVICE_FRAG)
                    startActivity(deviceIntent)
                    retVal = true
                }
                R.id.nav_favorites -> {
                    val favIntent = Intent(applicationContext, MainActivity::class.java)
                    favIntent.putExtra(MainActivity.EXTRA_FRAG_TYPE, MainActivity.FAV_FRAG)
                    startActivity(favIntent)
                    retVal = true
                }
                R.id.nav_userinfo -> {
                    drawerLayout.closeDrawers()
                    retVal = true
                }
                R.id.nav_link1 -> {
                    val browser1 = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.android.com/"))
                    startActivity(browser1)
                    retVal = true
                }
                R.id.nav_link2 -> {
                    val browser2 = Intent(Intent.ACTION_VIEW, Uri.parse("http://material.io/"))
                    startActivity(browser2)
                    retVal = true
                }
                else -> retVal = true
            }
            retVal
        }
    }

    companion object {
        private val TAG = "UserActivity"
    }

}

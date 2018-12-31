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
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.StringRes

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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.designdemo.uaha.data.model.user.UserInfo
import com.designdemo.uaha.util.InjectorUtils
import com.designdemo.uaha.view.product.ProductActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior.from
import kotlinx.android.synthetic.main.activity_user.*
import kotlinx.android.synthetic.main.dialog_picture.view.*
import kotlinx.android.synthetic.main.dialog_textscale.view.*

class UserActivity : AppCompatActivity() {
    private var mainActivity: Activity? = null

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var nameEnterField: AppCompatEditText
    private lateinit var phoneEnterField: AppCompatEditText
    private lateinit var passwordEnterField: AppCompatEditText
    private lateinit var fab: FloatingActionButton
    private lateinit var picButton: Button
    private lateinit var userLabelChip: Chip

    private lateinit var userViewModel: UserViewModel

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>

    //Local copy of usersInfo list, to be used when applying Undo button in snackbar
    private lateinit var users: List<UserInfo>

    // Lambda to add a close listener on the chip, and also put a random background color
    val setChipCloseAndRandomColor: (Chip) -> Unit = {
        it.setOnCloseIconClickListener { it -> it.visibility = View.GONE }
        it.setChipBackgroundColorResource(UiUtil.getRandomColor())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        mainActivity = this


        //Setup ViewModel
        val viewModelFactory = InjectorUtils.provideProfileViewModelFactory()
        users = listOf()

        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel::class.java)

        //User Data Has Been Updated
        userViewModel.getUserData().observe(this, Observer { usersIn ->
            users = usersIn

            if (users!!.isEmpty()) {
                nameEnterField.setText("")
                phoneEnterField.setText("")
                passwordEnterField.setText("")
            } else {
                val userInfo = users!!.last()

                nameEnterField.setText(userInfo.name)
                phoneEnterField.setText(userInfo.phone)
                passwordEnterField.setText(userInfo.password)
            }
        })

        //Status results from an update attempt (validation errors handled here)
        userViewModel.getAddUserStatus().observe(this, Observer { statusInt ->
            when (statusInt) {
                R.string.name_input_error -> {
                    nameEnterField.error = getString(statusInt)
                    nameEnterField.requestFocus()
                    showSnackbar(statusInt)
                }
                R.string.phone_input_error -> {
                    phoneEnterField.error = getString(statusInt)
                    phoneEnterField.requestFocus()
                    showSnackbar(statusInt)
                }
                R.string.invalid_password -> {
                    passwordEnterField.error = getString(statusInt)
                    passwordEnterField.requestFocus()
                    showSnackbar(statusInt)
                }
                R.string.profile_saved_confirm -> {
                    // If there is a value to reset show snackbar with undo option
                    val sizeOfList = users.size
                    if (sizeOfList > 1) {
                        val oldUserInfo = users.get(sizeOfList - 2)

                        val snackbar = Snackbar.make(user_main_content, getString(statusInt), Snackbar.LENGTH_LONG)
                                .setAction(getString(R.string.undo)) { _ ->
                                    userViewModel.addUserData(oldUserInfo)
                                }
                        val snackbarLayout = snackbar.view
                        snackbarLayout.layoutParams = getSnackBarLayoutParams()
                        snackbar.show()
                    } else {
                        //No backup available, so don't show undo option
                        showSnackbar(statusInt)
                    }
                }
                else -> {
                    Log.d("AddUserError", "Unexpected status message returned: $statusInt")
                }
            }
        })

        //Setup BottomAppBar
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
        passwordEnterField = password_edit
        userLabelChip = chip_userinfo_label

        //Format phone number as user is typing
        phoneEnterField.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        picButton = profile_pic_button
        picButton.setOnClickListener { v -> setPictureDialog() }

        fab = user_fab
        fab.setOnClickListener { v ->
            saveUserInfo()
        }

        userLabelChip.requestFocus()
    }


    private fun setUserInfoValues(users: List<UserInfo>) {
        if (users!!.isEmpty()) {
            nameEnterField.setText("")
            phoneEnterField.setText("")
            passwordEnterField.setText("")
        } else {
            val userInfo = users!!.last()

            Log.d("MSW", "${userInfo.name} - ${userInfo.phone} - ${userInfo.password}")
            nameEnterField.setText(userInfo.name)
            phoneEnterField.setText(userInfo.phone)
            passwordEnterField.setText(userInfo.password)
        }
    }

    /**
     * Validates and saves user info
     */
    private fun saveUserInfo() {
        val userInfo = UserInfo(nameEnterField.text.toString(), phoneEnterField.text.toString(), passwordEnterField.text.toString())
        userViewModel.addUserData(userInfo)
    }

    private fun showSnackbar(@StringRes displayString: Int) {
        val snackbar = Snackbar.make(user_main_content, getString(displayString), Snackbar.LENGTH_SHORT)
        val snackbarLayout = snackbar.view
        snackbarLayout.layoutParams = getSnackBarLayoutParams()
        snackbar.show()
    }

    /**
     * Setup Snackbar, including special accomidations for BottomAppBar
     * Notify the User with a snackbar
     * Need to set a calculate a specific offset for this so it appears higher then the BottomAppBar per the specification
     * TODO sigh, also need to accomidate for if the keyboard is showing
     *  TODO The Screen calculation stuff is duplicated on the DetailActivity
     */
    private fun getSnackBarLayoutParams(): LinearLayout.LayoutParams {
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
        return lp
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
        bottomSheetBehavior = from(bottomSheet)

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
        val setupTextScaleType: (Int, Int, Int) -> Int = { val1, val2, val3 ->
            valueToSet = getString(val1)
            letterSpacingText.text = UiUtil.applySpecialFormatting(getString(R.string.letter_spacing), getString(val2))
            sizeText.text = UiUtil.applySpecialFormatting(getString(R.string.size), getString(val3))
            0
        }

        when (scaleText) {
            "Headline1" -> {
                setupTextScaleType(R.string.st_h1, R.string.ls_neg1_5, R.string.sp_96)
                fontText.text = UiUtil.applySpecialFormatting(getString(R.string.font_text), getString(R.string.light))
            }
            "Headline2" -> {
                setupTextScaleType(R.string.st_h2, R.string.ls_neg5, R.string.sp_60)
                fontText.text = UiUtil.applySpecialFormatting(getString(R.string.font_text), getString(R.string.light))
            }
            "Headline3" -> {
                setupTextScaleType(R.string.st_h3, R.string.ls_zero, R.string.sp_48)
            }
            "Headline4" -> {
                setupTextScaleType(R.string.st_h4, R.string.ls_25, R.string.sp_34)
            }
            "Headline5" -> {
                setupTextScaleType(R.string.st_h5, R.string.ls_zero, R.string.sp_24)
            }
            "Headline6" -> {
                setupTextScaleType(R.string.st_h6, R.string.ls_15, R.string.sp_20)
                fontText.text = UiUtil.applySpecialFormatting(getString(R.string.font_text), getString(R.string.medium))
            }
            "Subtitle1" -> {
                setupTextScaleType(R.string.st_subtitle1, R.string.ls_15, R.string.sp_16)
            }
            "Subtitle2" -> {
                setupTextScaleType(R.string.st_subtitle2, R.string.ls_1, R.string.sp_14)
            }
            "Body1" -> {
                setupTextScaleType(R.string.st_body1, R.string.ls_5, R.string.sp_16)
            }
            "Body2" -> {
                setupTextScaleType(R.string.st_body2, R.string.ls_25, R.string.sp_14)
            }
            "Button" -> {
                setupTextScaleType(R.string.st_button, R.string.ls_75, R.string.sp_14)
                caseText.text = UiUtil.applySpecialFormatting(getString(R.string.case_text), getString(R.string.all_caps))
                fontText.text = UiUtil.applySpecialFormatting(getString(R.string.font_text), getString(R.string.medium))
            }
            "Caption" -> {
                setupTextScaleType(R.string.st_caption, R.string.ls_4, R.string.sp_12)
            }
            "Overline" -> {
                setupTextScaleType(R.string.st_overline, R.string.ls_1dot5, R.string.sp_10)
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
                    val osIntent = Intent(applicationContext, ProductActivity::class.java)
                    osIntent.putExtra(ProductActivity.EXTRA_FRAG_TYPE, ProductActivity.OS_FRAG)
                    startActivity(osIntent)
                    retVal = true
                }
                R.id.nav_devices -> {
                    val deviceIntent = Intent(applicationContext, ProductActivity::class.java)
                    deviceIntent.putExtra(ProductActivity.EXTRA_FRAG_TYPE, ProductActivity.DEVICE_FRAG)
                    startActivity(deviceIntent)
                    retVal = true
                }
                R.id.nav_favorites -> {
                    val favIntent = Intent(applicationContext, ProductActivity::class.java)
                    favIntent.putExtra(ProductActivity.EXTRA_FRAG_TYPE, ProductActivity.FAV_FRAG)
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
        const private val TAG = "UserActivity"
    }

}

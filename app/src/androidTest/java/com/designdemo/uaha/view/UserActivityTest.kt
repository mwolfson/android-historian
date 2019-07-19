package com.designdemo.uaha.view

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.hasErrorText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.designdemo.uaha.view.user.UserActivity
import com.google.android.material.textfield.TextInputLayout
import com.support.android.designlibdemo.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserActivityTest {
    lateinit var nameValid: String
    lateinit var nameBad: String

    lateinit var phoneValid: String
    lateinit var phoneBad: String

    lateinit var passwordValid: String
    lateinit var passwordBad: String

    @get:Rule
    var activityRule: ActivityTestRule<UserActivity> = ActivityTestRule(UserActivity::class.java)

    @Before
    fun initStringsEntered() {
        nameValid = "mike"
        nameBad = "mik"

        phoneValid = "3035551212"
        phoneBad = "9934"

        passwordValid = "Passwrd1"
        passwordBad = "passbad"
    }

    @Test
    fun testAllValidEntries() {
        // Enter 3 valid values and press enter
        onView(withId(R.id.name_edit)).perform(typeText(nameValid))
        onView(withId(R.id.phone_edit)).perform(typeText(phoneValid))
        onView(withId(R.id.password_edit)).perform(typeText(passwordValid))
        onView(withId(R.id.user_fab)).perform(click())

        onView(withId(com.google.android.material.R.id.snackbar_text)).check(matches(withText(R.string.profile_saved_confirm)))
    }

    @Test
    fun testHintValuesValid() {
        onView(withId(R.id.name_editlayout)).check(matches(hasTextInputLayoutHintText(activityRule.activity.getString(R.string.profile_nickname_hint))))
        onView(withId(R.id.phone_editlayout)).check(matches(hasTextInputLayoutHintText(activityRule.activity.getString(R.string.profile_phone_hint))))
        onView(withId(R.id.password_editlayout)).check(matches(hasTextInputLayoutHintText(activityRule.activity.getString(R.string.enter_a_password))))
    }

    @Test
    fun testAllValidEntries_NameBad() {
        onView(withId(R.id.name_edit)).perform(typeText(nameBad))
        onView(withId(R.id.phone_edit)).perform(typeText(phoneValid))
        onView(withId(R.id.password_edit)).perform(typeText(passwordValid))
        onView(withId(R.id.user_fab)).perform(click())

        onView(withId(com.google.android.material.R.id.snackbar_text)).check(matches(withText(R.string.name_input_error)))
        onView(withId(R.id.name_edit)).check(matches(hasErrorText(activityRule.activity.getString(R.string.name_input_error))))
    }

    @Test
    fun testAllValidEntries_PhoneBad() {
        onView(withId(R.id.name_edit)).perform(typeText(nameValid))
        onView(withId(R.id.phone_edit)).perform(typeText(phoneBad))
        onView(withId(R.id.password_edit)).perform(typeText(passwordValid))
        onView(withId(R.id.user_fab)).perform(click())

        onView(withId(com.google.android.material.R.id.snackbar_text)).check(matches(withText(R.string.phone_input_error)))
        onView(withId(R.id.phone_edit)).check(matches(hasErrorText(activityRule.activity.getString(R.string.phone_input_error))))
    }

    @Test
    fun testAllValidEntries_PasswordBad() {
        onView(withId(R.id.name_edit)).perform(typeText(nameValid))
        onView(withId(R.id.phone_edit)).perform(typeText(phoneValid))
        onView(withId(R.id.password_edit)).perform(typeText(passwordBad))
        onView(withId(R.id.user_fab)).perform(click())

        onView(withId(com.google.android.material.R.id.snackbar_text)).check(matches(withText(R.string.invalid_password)))
        onView(withId(R.id.password_edit)).check(matches(hasErrorText(activityRule.activity.getString(R.string.invalid_password))))
    }

    fun hasTextInputLayoutHintText(expectedErrorText: String): Matcher<View> {
        return object : TypeSafeMatcher<View>() {

            public override fun matchesSafely(view: View): Boolean {
                if (view !is TextInputLayout) {
                    return false
                }

                val error = view.hint ?: return false

                val hint = error.toString()

                return expectedErrorText == hint
            }

            override fun describeTo(description: Description) {}
        }
    }
}
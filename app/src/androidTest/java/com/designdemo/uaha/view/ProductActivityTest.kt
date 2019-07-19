package com.designdemo.uaha.view

import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.designdemo.uaha.view.product.ProductActivity
import com.support.android.designlibdemo.BuildConfig
import com.support.android.designlibdemo.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class ProductActivityTest {

    lateinit var versionName: String
    var versionCode = 0

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(ProductActivity::class.java)

    @Before
    fun initStringsEntered() {
        versionName = BuildConfig.VERSION_NAME
        versionCode = BuildConfig.VERSION_CODE
    }

    @Test
    fun versionNameVerificationTest() {
        val appCompatImageButton = onView(allOf(withContentDescription("Navigate up"), childAtPosition(allOf(withId(R.id.bottom_appbar), childAtPosition(withId(R.id.user_main_coordinator), 1)), 1), isDisplayed()))
        appCompatImageButton.perform(click())

        val textView = onView(allOf(withId(R.id.header_versioninfo), withText("Version:  $versionName ($versionCode)"), childAtPosition(childAtPosition(withId(R.id.navigation_header_container), 0), 1), isDisplayed()))
        textView.check(matches(withText("Version:  $versionName ($versionCode)")))
    }

    @Test
    fun checkMenuCorrectTest() {
        val appCompatImageButton = onView(allOf(withContentDescription("Navigate up"), childAtPosition(allOf(withId(R.id.bottom_appbar), childAtPosition(withId(R.id.user_main_coordinator), 1)), 1), isDisplayed()))
        appCompatImageButton.perform(click())

        val checkedTextView = onView(allOf(withId(R.id.design_menu_item_text), childAtPosition(childAtPosition(withId(R.id.design_navigation_view), 1), 0), isDisplayed()))
        checkedTextView.check(matches(isDisplayed()))
    }

    private fun childAtPosition(parentMatcher: Matcher<View>, position: Int): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent) && view == parent.getChildAt(position)
            }
        }
    }
}

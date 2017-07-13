package com.designdemo.uaha;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.support.android.designlibdemo.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class DeviceListTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void deviceListTest() {
        ViewInteraction appCompatTextView = onView(
                allOf(withText("Device"), isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.recyclerview),
                        withParent(allOf(withId(R.id.main_content),
                                withParent(allOf(withId(R.id.viewpager),
                                        withParent(allOf(withId(R.id.main_content),
                                                withParent(allOf(withId(R.id.drawer_layout),
                                                        withParent(allOf(withId(android.R.id.content),
                                                                withParent(withId(R.id.action_bar_root)))))))))))),
                        isDisplayed()));
        recyclerView.check(matches(isDisplayed()));

        ViewInteraction textView = onView(
                allOf(withId(android.R.id.text1), withText("HTC G1"), isDisplayed()));
        textView.check(matches(withText("HTC G1")));

        ViewInteraction textView2 = onView(
                allOf(withId(android.R.id.text1), withText("Motorola Droid"), isDisplayed()));
        textView2.check(matches(withText("Motorola Droid")));

        ViewInteraction textView3 = onView(
                allOf(withId(android.R.id.text1), withText("Motorola Droid"), isDisplayed()));
        textView3.check(matches(withText("Motorola Droid")));

    }
}

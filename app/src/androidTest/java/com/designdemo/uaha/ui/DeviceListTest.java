package com.designdemo.uaha.ui;


import com.designdemo.uaha.ui.MainActivity;
import com.support.android.designlibdemo.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

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

        ViewInteraction g1View = onView(
                allOf(withId(R.id.list_title), withText("G1"), isDisplayed()));
        g1View.check(matches(withText("G1")));

        ViewInteraction droidView = onView(
                allOf(withId(R.id.list_title), withText("Droid"), isDisplayed()));
        droidView.check(matches(withText("Droid")));

//
//        ViewInteraction textView2 = onView(
//                allOf(withId(android.R.id.text1), withText("Motorola Droid"), isDisplayed()));
//        textView2.check(matches(withText("Motorola Droid")));
//
//        ViewInteraction textView3 = onView(
//                allOf(withId(android.R.id.text1), withText("Motorola Droid"), isDisplayed()));
//        textView3.check(matches(withText("Motorola Droid")));

    }
}

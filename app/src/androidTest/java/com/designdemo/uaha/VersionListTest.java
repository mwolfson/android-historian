package com.designdemo.uaha;


import androidx.test.espresso.ViewInteraction;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.suitebuilder.annotation.LargeTest;

import com.designdemo.uaha.ui.MainActivity;
import com.support.android.designlibdemo.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class VersionListTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void versionListTest() {
        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.recyclerview),
                        withParent(allOf(withId(R.id.main_content),
                                withParent(withId(R.id.viewpager)))),
                        isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction imageView = onView(
                allOf(withId(R.id.backdrop),
                        withParent(allOf(withId(R.id.collapsing_toolbar),
                                withParent(allOf(withId(R.id.appbar),
                                        withParent(allOf(withId(R.id.main_content),
                                                withParent(allOf(withId(android.R.id.content),
                                                        withParent(withId(R.id.action_bar_root)))))))))),
                        isDisplayed()));
        imageView.check(matches(isDisplayed()));

    }
}

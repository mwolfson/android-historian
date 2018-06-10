package com.designdemo.uaha.ui;


import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.designdemo.uaha.ui.MainActivity;
import com.support.android.designlibdemo.R;

import org.hamcrest.core.AllOf;
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
import static androidx.test.espresso.matcher.ViewMatchers.withText;
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

    @Test
    public void testListContent() {
        ViewInteraction recyclerView = onView(
                AllOf.allOf(withId(R.id.recyclerview),
                        withParent(AllOf.allOf(withId(R.id.main_content),
                                withParent(AllOf.allOf(withId(R.id.viewpager),
                                        withParent(AllOf.allOf(withId(R.id.main_content),
                                                withParent(AllOf.allOf(withId(R.id.drawer_layout),
                                                        withParent(AllOf.allOf(withId(android.R.id.content),
                                                                withParent(withId(R.id.action_bar_root)))))))))))),
                        isDisplayed()));
        recyclerView.check(matches(isDisplayed()));

//        onView(withId(R.id.recyclerview)).perform(RecyclerViewActions.actionOnItemAtPosition(0, ViewMatcher. ));

        ViewInteraction viewCupcake = onView(allOf(withId(R.id.list_title), withText("Cupcake"), isDisplayed()));
        viewCupcake.check(matches(withText("Cupcake")));

        ViewInteraction viewDonut = onView(allOf(withId(R.id.list_title), withText("Donut"), isDisplayed()));
        viewDonut.check(matches(withText("Donut")));

        ViewInteraction viewEclair = onView(allOf(withId(R.id.list_title), withText("Eclair"), isDisplayed()));
        viewEclair.check(matches(withText("Eclair")));
        
        ViewInteraction viewFroyo = onView(allOf(withId(R.id.list_title), withText("Froyo"), isDisplayed()));
        viewFroyo.check(matches(withText("Froyo")));

//        ViewInteraction viewGingerbread = onView(allOf(withId(R.id.list_title), withText("Gingerbread"), isDisplayed()));
//        viewGingerbread.check(matches(withText("Gingerbread")));
//
//        ViewInteraction viewHoneycomb = onView(allOf(withId(R.id.list_title), withText("Honeycomb"), isDisplayed()));
//        viewHoneycomb.check(matches(withText("Honeycomb")));
//
//        ViewInteraction viewIcs = onView(allOf(withId(R.id.list_title), withText("Ice Cream Sandwich"), isDisplayed()));
//        viewIcs.check(matches(withText("Ice Cream Sandwich")));
        
    }

}

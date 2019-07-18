package com.designdemo.uaha.view;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.designdemo.uaha.view.detail.DetailActivity;
import com.support.android.designlibdemo.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.AllOf.allOf;


@LargeTest
@RunWith(AndroidJUnit4.class)
public class DetailActivityTest  {

    @Rule
    public ActivityTestRule<DetailActivity> mActivityTestRule =
            new ActivityTestRule<DetailActivity>(DetailActivity.class) {
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation()
                            .getTargetContext();
                    Intent result = new Intent(targetContext, DetailActivity.class);
                    result.putExtra("os_name", "Cupcake-API Level 3");
                    return result;
                }
            };

    @Test
    public void detailViewTest() {
//        onView(withId(withIdΩR.id.collapsing_toolbar)).check(matches(hasDescendant(withText("Cupcake"))));

        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.detail_toolbar))))
                .check(matches(withText("Cupcake")));
    }



}

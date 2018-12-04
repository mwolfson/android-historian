package com.designdemo.uaha.ui;

import android.content.Context;
import android.content.Intent;

import com.designdemo.uaha.data.VersionData;
import com.support.android.designlibdemo.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.InstrumentationRegistry;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


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
                    result.putExtra(DetailActivity.Companion.getEXTRA_APP_NAME(), VersionData.getProductName(VersionData.OS_GINGERBREAD));
                    return result;
                }
            };

    @Test
    public void detailViewTest() {
        onView(withId(R.id.toolbar)).check(matches(withText("Gingerbread")));
    }

//    @Test
//    public void fabInteractTest() {
////        onClick()
//    }
}

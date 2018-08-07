package com.designdemo.uaha.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.util.DisplayMetrics;

import androidx.annotation.DimenRes;

public class UiUtil {

    /**
     * Will return a pixel value, for a specific dimen
     *
     * @param res Resource Id of a Dimension value
     * @param context
     * @return
     */
    public static float getPxForRes(@DimenRes int res, Context context) {
        Resources resources = context.getResources();
        float dimenValue = resources.getDimension(res);

        return convertDpToPixel(dimenValue, context);
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    /**
     * Get the height of the current screen
     *
     * @param context
     * @return height of screen in Pixels
     */
    public static float getScreenHeight(Activity context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float pxHeight = displayMetrics.heightPixels;

        return pxHeight;
    }

    /**
     * Get the width of the current screen
     *
     * @param context
     * @return width of screen in Pixels
     */
    public static float getScreenWidth(Activity context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float pxWidth = displayMetrics.widthPixels;

        return pxWidth;
    }

    /**
     * This will apply special formatting to the input strings.  The first word will be bolded and second will be italicized
     *
     * @param word1 Word to Bold, and make slightly smaller
     * @param word2 Word to Italicize
     * @return
     */
    public static SpannableStringBuilder applySpecialFormatting(String word1, String word2) {
        SpannableStringBuilder str = new SpannableStringBuilder(word1 + ":  " + word2);
        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, word1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        str.setSpan(new AbsoluteSizeSpan(16, true), word1.length() + 1, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        str.setSpan(new android.text.style.StyleSpan(Typeface.ITALIC), word1.length() + 1, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return str;
    }
}

package com.designdemo.uaha.util

import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.util.DisplayMetrics

import androidx.annotation.DimenRes
import com.support.android.designlibdemo.R
import java.util.Random

object UiUtil {

    /**
     * Will return a pixel value, for a specific dimen
     *
     * @param res Resource Id of a Dimension value
     * @param context
     * @return
     */
    fun getPxForRes(@DimenRes res: Int, context: Context): Float {
        val resources = context.resources
        val dimenValue = resources.getDimension(res)
        return convertDpToPixel(dimenValue, context)
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    fun convertDpToPixel(dp: Float, context: Context): Float {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    fun convertPixelsToDp(px: Float, context: Context): Float {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return px / (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    /**
     * Get the height of the current screen
     *
     * @param context
     * @return height of screen in Pixels
     */
    fun getScreenHeight(context: Activity): Float {
        val displayMetrics = DisplayMetrics()
        context.windowManager.defaultDisplay.getMetrics(displayMetrics)

        return displayMetrics.heightPixels.toFloat()
    }

    /**
     * Get the width of the current screen
     *
     * @param context
     * @return width of screen in Pixels
     */
    fun getScreenWidth(context: Activity): Float {
        val displayMetrics = DisplayMetrics()
        context.windowManager.defaultDisplay.getMetrics(displayMetrics)

        return displayMetrics.widthPixels.toFloat()
    }

    /**
     * This will apply special formatting to the input strings.  The first word will be bolded and second will be italicized
     *
     * @param word1 Word to Bold, and make slightly smaller
     * @param word2 Word to Italicize
     * @return
     */
    fun applySpecialFormatting(word1: String, word2: String): SpannableStringBuilder {
        val str = SpannableStringBuilder("$word1:  $word2")
        str.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, word1.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        str.setSpan(AbsoluteSizeSpan(16, true), word1.length + 1, str.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        str.setSpan(android.text.style.StyleSpan(Typeface.ITALIC), word1.length + 1, str.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        return str
    }

    fun applyBoldFirstWord(word1: String, word2: String): SpannableStringBuilder {
        val str = SpannableStringBuilder("$word1\n$word2")
        str.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, word1.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        return str
    }

    fun getRandomColor(): Int {
        val colors = intArrayOf(R.color.red_500, R.color.pink_500, R.color.purple_500, R.color.deep_purple_500, R.color.indigo_500, R.color.blue_500, R.color.light_blue_500, R.color.cyan_500, R.color.teal_500, R.color.green_500, R.color.light_green_500, R.color.lime_500, R.color.yellow_500, R.color.amber_500, R.color.orange_500, R.color.deep_orange_500, R.color.brown_500, R.color.blue_grey_500, R.color.grey_500)
        val randInt = Random().nextInt(colors.size)
        return colors[randInt]
    }

    fun getRandomDrawable(): Int {
        val drawables = intArrayOf(R.drawable.vct_location_pin, R.drawable.vct_business, R.drawable.vct_train, R.drawable.vct_cost)
        val randInt = Random().nextInt(drawables.size)
        return drawables[randInt]
    }
}

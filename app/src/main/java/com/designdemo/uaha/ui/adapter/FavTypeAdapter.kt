package com.designdemo.uaha.ui.adapter

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide
import com.designdemo.uaha.data.VersionData
import com.designdemo.uaha.ui.DetailActivity
import com.support.android.designlibdemo.R
import java.util.Random

import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_favorites.view.*

class FavTypeAdapter(private val activity: Activity, private val values: List<String>) : RecyclerView.Adapter<FavTypeAdapter.ViewHolder>() {

    private val typedValue = TypedValue()
    private val background: Int

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        var boundString: String? = null
        val imageView: ImageView = view.fav_item_avatar
        val textView: TextView = view.fav_item_title
        val textView2: TextView = view.fav_item_content

        override fun toString(): String {
            return super.toString() + " '" + textView.text
        }
    }

    fun getValueAt(position: Int): String {
        return values[position]
    }

    init {
        activity.applicationContext.theme.resolveAttribute(R.attr.selectableItemBackground, typedValue, true)
        background = typedValue.resourceId
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavTypeAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_favorites, parent, false)
        view.setBackgroundResource(background)

        return FavTypeAdapter.ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.boundString = values[position]
        holder.textView.text = values[position]
        // We will set random length text to offset this view for staggarred effect
        val rand = Random()
        val randomNum = rand.nextInt(3)

        var strRes = when (randomNum) {
            0 -> R.string.ipsum_med
            1 -> R.string.ipsum_long
            else -> R.string.ipsum_short
        }

        holder.textView2.setText(strRes)

        holder.view.setOnClickListener { v ->
            val context = v.context
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_APP_NAME, holder.boundString)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val transitionName = context.getString(R.string.transition_string)
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, holder.imageView, transitionName)
                context.startActivity(intent, options.toBundle())
            } else {
                context.startActivity(intent)
            }
        }

        Glide.with(holder.imageView.context)
                .load(VersionData.getOsDrawable(VersionData.getOsNum(holder.boundString!!)))
                .fitCenter()
                .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return values.size
    }
}

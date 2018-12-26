package com.designdemo.uaha.view.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide
import com.designdemo.uaha.data.model.VersionData
import com.designdemo.uaha.view.DetailActivity
import com.support.android.designlibdemo.R

import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_device.view.*

class DeviceTypeAdapter(private val activity: Activity, context: Context, private val values: List<String>) : RecyclerView.Adapter<DeviceTypeAdapter.ViewHolder>() {

    private val typedValue = TypedValue()
    private val background: Int

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        var boundString: String? = null
        val imageView: ImageView = view.device_item_avatar
        val titleText: TextView = view.device_item_title
        val subTitleText: TextView = view.device_item_subtext
        var osVersion = 0

        override fun toString(): String {
            return super.toString() + " '" + titleText.text
        }
    }

    fun getValueAt(position: Int): String {
        return values[position]
    }

    init {
        context.theme.resolveAttribute(R.attr.selectableItemBackground, typedValue, true)
        background = typedValue.resourceId
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceTypeAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_device, parent, false)
        view.setBackgroundResource(background)

        return DeviceTypeAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeviceTypeAdapter.ViewHolder, position: Int) {
        holder.boundString = values[position]
        val splitString = values[position]
        val parts = splitString.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        holder.titleText.text = parts[0]
        holder.subTitleText.text = parts[1]

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
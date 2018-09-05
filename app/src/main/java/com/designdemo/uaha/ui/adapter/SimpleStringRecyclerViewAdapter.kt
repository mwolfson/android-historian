package com.designdemo.uaha.ui.adapter

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
import com.designdemo.uaha.data.VersionData
import com.designdemo.uaha.ui.DetailActivity
import com.support.android.designlibdemo.R

import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView

class SimpleStringRecyclerViewAdapter(private val activity: Activity, context: Context, private val values: List<String>, private val IS_DEVICE: Boolean) : RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder>() {

    private val typedValue = TypedValue()
    private val background: Int

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        var boundString: String? = null
        val imageView: ImageView
        val titleText: TextView
        val subTitleText: TextView
        var os_version: Int = 0

        init {
            imageView = view.findViewById(R.id.avatar)
            titleText = view.findViewById(R.id.list_title)
            subTitleText = view.findViewById(R.id.list_subtext)
        }

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleStringRecyclerViewAdapter.ViewHolder {
        var view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_os, parent, false)

        if(IS_DEVICE) {
            view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_device, parent, false)
        }

        view.setBackgroundResource(background)
        return SimpleStringRecyclerViewAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: SimpleStringRecyclerViewAdapter.ViewHolder, position: Int) {
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
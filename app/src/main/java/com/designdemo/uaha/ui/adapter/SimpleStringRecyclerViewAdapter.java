package com.designdemo.uaha.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.designdemo.uaha.data.VersionData;
import com.designdemo.uaha.ui.DetailActivity;
import com.support.android.designlibdemo.R;

import java.util.List;

import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

public class SimpleStringRecyclerViewAdapter
        extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {

    private final TypedValue typedValue = new TypedValue();
    private int background;
    private List<String> values;
    private Activity activity;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public String boundString;

        public final View view;
        public final ImageView imageView;
        public final TextView titleText;
        public final TextView subTitleText;
        public int os_version;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            imageView = view.findViewById(R.id.avatar);
            titleText = view.findViewById(R.id.list_title);
            subTitleText = view.findViewById(R.id.list_subtext);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + titleText.getText();
        }
    }

    public String getValueAt(int position) {
        return values.get(position);
    }

    public SimpleStringRecyclerViewAdapter(Activity mActivityIn, Context context, List<String> items) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, typedValue, true);
        activity = mActivityIn;
        background = typedValue.resourceId;
        values = items;
    }

    @Override
    public SimpleStringRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        view.setBackgroundResource(background);
        return new SimpleStringRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleStringRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.boundString = values.get(position);
        String splitString = values.get(position);
        String[] parts = splitString.split("-");
        holder.titleText.setText(parts[0]);
        holder.subTitleText.setText(parts[1]);

        holder.view.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_NAME, holder.boundString);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                String transitionName = context.getString(R.string.transition_string);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, holder.imageView, transitionName);
                context.startActivity(intent, options.toBundle());
            } else {
                context.startActivity(intent);
            }
        });

        Glide.with(holder.imageView.getContext())
                .load(VersionData.getOsDrawable(VersionData.getOsNum(holder.boundString)))
                .fitCenter()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return values.size();
    }
}
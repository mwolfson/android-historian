package com.designdemo.uaha.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
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
import java.util.Random;

import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

public class SimpleStaggaredRecyclerViewAdapter
        extends RecyclerView.Adapter<SimpleStaggaredRecyclerViewAdapter.ViewHolder> {

    private final TypedValue typedValue = new TypedValue();
    private int background;
    private List<String> values;
    private Activity activity;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public String boundString;

        public final View view;
        public final ImageView imageView;
        public final TextView textView;
        public final TextView textView2;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            imageView = (ImageView) view.findViewById(R.id.avatar);
            textView = (TextView) view.findViewById(R.id.text_title);
            textView2 = (TextView) view.findViewById(R.id.text_content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + textView.getText();
        }
    }

    public String getValueAt(int position) {
        return values.get(position);
    }

    public SimpleStaggaredRecyclerViewAdapter(Activity mActivityIn, List<String> items) {
        mActivityIn.getApplicationContext().getTheme().resolveAttribute(R.attr.selectableItemBackground, typedValue, true);
        background = typedValue.resourceId;
        values = items;
        activity = mActivityIn;
    }

    @Override
    public SimpleStaggaredRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stag_list_item, parent, false);
        view.setBackgroundResource(background);
        return new SimpleStaggaredRecyclerViewAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.boundString = values.get(position);
        holder.textView.setText(values.get(position));
        // We will set random length text to offset this view for staggarred effect
        Random rand = new Random();
        int randomNum = rand.nextInt(3);

        int strRes = 0;

        Log.d("MSW", "The random number is: " +strRes);
        switch (randomNum) {
            case (0):
                strRes = R.string.ipsum_med;
                break;
            case (1):
                strRes = R.string.ipsum_long;
                break;
            default:
                strRes = R.string.ipsum_short;
                break;
        }

        holder.textView2.setText(strRes);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra(DetailActivity.EXTRA_NAME, holder.boundString);

                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    String transitionName = context.getString(R.string.transition_string);
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, holder.imageView, transitionName);
                    context.startActivity(intent, options.toBundle());
                } else {
                    context.startActivity(intent);
                }
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

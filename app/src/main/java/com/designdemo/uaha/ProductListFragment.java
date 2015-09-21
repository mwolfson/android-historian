package com.designdemo.uaha;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.support.android.designlibdemo.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProductListFragment extends Fragment {

    public static final int FRAG_TYPE_OS = 1;
    public static final int FRAG_TYPE_DEVICE = 2;
    public static final int FRAG_TYPE_FAV = 3;

    public static final String ARG_FRAG_TYPE = "frag_arg_type";

    private int thisFragType = 0;
    private Activity mainActivity;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView rv = (RecyclerView) inflater.inflate(
                R.layout.fragment_prod_list, container, false);

        mainActivity = getActivity();

        thisFragType = getArguments().getInt(ARG_FRAG_TYPE, 0);
        Log.d("MSW", "The Frag Type is: " + thisFragType);
        setupRecyclerView(rv);
        return rv;
    }

    // TODO - add ItemAnimators get Staggered Layout example working better by varying the length of content more
    private void setupRecyclerView(RecyclerView recyclerView) {
        switch (thisFragType) {
            case (FRAG_TYPE_OS):
                recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
                recyclerView.addItemDecoration(new GridDividerDecoration(recyclerView.getContext()));
                recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(getActivity(),
                        getDataList()));
                break;
            case (FRAG_TYPE_DEVICE):
                recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), 2));
                recyclerView.addItemDecoration(new GridDividerDecoration(recyclerView.getContext()));
                recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(getActivity(),
                        getDataList()));
                break;
            case (FRAG_TYPE_FAV):
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                //TODO - since this is varied content length, it would be better as cards
                // https://www.google.com/design/spec/components/cards.html#cards-content
//                recyclerView.addItemDecoration(new GridDividerDecoration(recyclerView.getContext()));
                recyclerView.setAdapter(new SimpleStaggaredRecyclerViewAdapter(getActivity(),
                        getDataList()));

                break;
        }
    }

    private List<String> getDataList() {
        ArrayList<String> list = new ArrayList<>(VersionData.NUM_OF_OS);
        switch (thisFragType) {
            case (FRAG_TYPE_OS):
                for (int x = 0; x <= VersionData.NUM_OF_OS; x++) {
                    list.add(VersionData.osStrings[x]);
                }
                break;
            case (FRAG_TYPE_DEVICE):
                for (int x = 0; x <= VersionData.NUM_OF_OS; x++) {
                    list.add(VersionData.deviceStrings[x]);
                }
                break;
            case (FRAG_TYPE_FAV):
                list = PrefsUtil.getFavorites(mainActivity);
                break;
        }
        return list;
    }

    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {

        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        private List<String> mValues;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public String mBoundString;

            public final View mView;
            public final ImageView mImageView;
            public final TextView mTextView;
            public int os_version;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = (ImageView) view.findViewById(R.id.avatar);
                mTextView = (TextView) view.findViewById(android.R.id.text1);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mTextView.getText();
            }
        }

        public String getValueAt(int position) {
            return mValues.get(position);
        }

        public SimpleStringRecyclerViewAdapter(Context context, List<String> items) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item, parent, false);
            view.setBackgroundResource(mBackground);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mBoundString = mValues.get(position);
            holder.mTextView.setText(mValues.get(position));

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra(DetailActivity.EXTRA_NAME, holder.mBoundString);

                    context.startActivity(intent);
                }
            });

            Glide.with(holder.mImageView.getContext())
                    .load(VersionData.getOsDrawable(VersionData.getOsNum(holder.mBoundString)))
                    .fitCenter()
                    .into(holder.mImageView);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }
    }

    public static class SimpleStaggaredRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleStaggaredRecyclerViewAdapter.ViewHolder> {

        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        private List<String> mValues;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public String mBoundString;

            public final View mView;
            public final ImageView mImageView;
            public final TextView mTextView;
            public final TextView mTextView2;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = (ImageView) view.findViewById(R.id.avatar);
                mTextView = (TextView) view.findViewById(R.id.text_title);
                mTextView2 = (TextView) view.findViewById(R.id.text_content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mTextView.getText();
            }
        }

        public String getValueAt(int position) {
            return mValues.get(position);
        }

        public SimpleStaggaredRecyclerViewAdapter(Context context, List<String> items) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.stag_list_item, parent, false);
            view.setBackgroundResource(mBackground);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mBoundString = mValues.get(position);
            holder.mTextView.setText(mValues.get(position));
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

            holder.mTextView2.setText(strRes);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra(DetailActivity.EXTRA_NAME, holder.mBoundString);

                    context.startActivity(intent);
                }
            });

            Glide.with(holder.mImageView.getContext())
                    .load(VersionData.getOsDrawable(VersionData.getOsNum(holder.mBoundString)))
                    .fitCenter()
                    .into(holder.mImageView);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }
    }
}

package com.designdemo.uaha.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.designdemo.uaha.ui.adapter.SimpleStaggaredRecyclerViewAdapter;
import com.designdemo.uaha.ui.adapter.SimpleStringRecyclerViewAdapter;
import com.designdemo.uaha.ui.common.GridDividerDecoration;
import com.designdemo.uaha.data.VersionData;
import com.designdemo.uaha.util.PrefsUtil;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

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

    private BottomSheetBehavior bottomSheetBehavior;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_prod_list, container, false);

        RecyclerView rv = mainView.findViewById(R.id.recyclerview);

        mainActivity = getActivity();

        thisFragType = getArguments().getInt(ARG_FRAG_TYPE, 0);
        setupRecyclerView(rv);

        View bottomSheet = mainView.findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheetBehavior.setPeekHeight(0);
                }
            }

            @Override
            public void onSlide(View bottomSheet, float slideOffset) {
            }
        });

        bottomSheetBehavior.setPeekHeight(300);

        return mainView;
    }

    // TODO - add ItemAnimators get Staggered Layout example working better by varying the length of content more
    private void setupRecyclerView(RecyclerView recyclerView) {
        switch (thisFragType) {
            case (FRAG_TYPE_OS):
                LinearLayoutManager llm = new LinearLayoutManager(recyclerView.getContext());
                recyclerView.setLayoutManager(llm);
                recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), llm.getOrientation()));
                recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(getActivity(), getActivity(), getDataList()));
                break;
            case (FRAG_TYPE_DEVICE):
                recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), 2));
                recyclerView.addItemDecoration(new GridDividerDecoration(recyclerView.getContext()));
                recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(getActivity(), getActivity(), getDataList()));
                break;
            case (FRAG_TYPE_FAV):
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                recyclerView.setAdapter(new SimpleStaggaredRecyclerViewAdapter(getActivity(), getDataList()));
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
                for (int x = 0; x <= VersionData.NUM_OF_DEVICES; x++) {
                    list.add(VersionData.deviceStrings[x]);
                }
                break;
            case (FRAG_TYPE_FAV):
                list = PrefsUtil.getFavorites(mainActivity);
                break;
        }
        return list;
    }

}

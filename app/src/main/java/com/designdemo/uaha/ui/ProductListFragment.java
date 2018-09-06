package com.designdemo.uaha.ui;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.designdemo.uaha.data.VersionData;
import com.designdemo.uaha.ui.adapter.SimpleStaggaredRecyclerViewAdapter;
import com.designdemo.uaha.ui.adapter.SimpleStringRecyclerViewAdapter;
import com.designdemo.uaha.util.PrefsUtil;
import com.dgreenhalgh.android.simpleitemdecoration.grid.GridDividerItemDecoration;
import com.support.android.designlibdemo.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

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
        View mainView = inflater.inflate(R.layout.fragment_prod_list, container, false);

        RecyclerView rv = mainView.findViewById(R.id.recyclerview);

        mainActivity = getActivity();

        thisFragType = getArguments().getInt(ARG_FRAG_TYPE, 0);
        setupRecyclerView(rv);

        return mainView;
    }

    // TODO - add ItemAnimators get Staggered Layout example working better by varying the length of content more
    private void setupRecyclerView(RecyclerView recyclerView) {
        switch (thisFragType) {
            case (FRAG_TYPE_OS):
                LinearLayoutManager llm = new LinearLayoutManager(recyclerView.getContext());
                recyclerView.setLayoutManager(llm);
                recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), llm.getOrientation()));
                recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(getActivity(), getActivity(), getDataList(), false));
                break;
            case (FRAG_TYPE_DEVICE):
                Drawable gridDivider = ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.grid_divider);
                recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), 2));
                recyclerView.addItemDecoration(new GridDividerItemDecoration(gridDivider, gridDivider, 2));
                recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(getActivity(), getActivity(), getDataList(), true));
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
                list = PrefsUtil.INSTANCE.getFavorites(mainActivity);
                Log.d("MSW", "List of products is sized:" + list.size());
                break;
        }
        return list;
    }

}

package com.designdemo.uaha.view.product.device

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.*
import com.designdemo.uaha.data.model.product.ProductItem
import com.designdemo.uaha.view.product.device.DeviceTypeAdapter
import com.designdemo.uaha.view.product.fav.FavTypeAdapter
import com.designdemo.uaha.view.product.os.OsTypeAdapter
import com.dgreenhalgh.android.simpleitemdecoration.grid.GridDividerItemDecoration
import com.support.android.designlibdemo.R
import kotlinx.android.synthetic.main.fragment_prod_list.view.*

class DeviceFragment : Fragment() {

    private var mainActivity: FragmentActivity? = null

    private lateinit var deviceViewModel: DeviceViewModel

    @Nullable
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val mainView = inflater.inflate(R.layout.fragment_prod_list, container, false)

        val rv = mainView.recyclerview

        mainActivity = activity

        deviceViewModel = ViewModelProviders.of(this).get(DeviceViewModel::class.java)
        deviceViewModel.getDeviceData().observe(this, Observer { devList ->
            if (devList.isNotEmpty()) {
                setupRecyclerView(rv, devList)
            }
        })

        return mainView
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, devList : List<ProductItem>) {
        val gridDivider = ContextCompat.getDrawable(recyclerView.context, R.drawable.grid_divider)
        recyclerView.layoutManager = GridLayoutManager(recyclerView.context, 2)
        recyclerView.addItemDecoration(GridDividerItemDecoration(gridDivider, gridDivider, 2))
        recyclerView.adapter = DeviceTypeAdapter(mainActivity!!, mainActivity!!, devList)
    }


}

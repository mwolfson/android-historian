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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.designdemo.uaha.data.model.product.ProductEntity
import com.designdemo.uaha.view.ui.GridDividerItemDecoration
import com.support.android.designlibdemo.R
import kotlinx.android.synthetic.main.fragment_prod_list.product_recyclerview
import kotlinx.android.synthetic.main.fragment_prod_list.view.product_recyclerview
import kotlinx.android.synthetic.main.fragment_prod_list.view.product_nodata_text
import kotlinx.android.synthetic.main.fragment_prod_list.view.product_nodata_layout

class DeviceFragment : Fragment() {

    private var mainActivity: FragmentActivity? = null

    private lateinit var deviceViewModel: DeviceViewModel

    @Nullable
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val mainView = inflater.inflate(R.layout.fragment_prod_list, container, false)

        val rv = mainView.product_recyclerview
        val noText = mainView.product_nodata_text
        val noLayout = mainView.product_nodata_layout

        mainActivity = activity

        deviceViewModel = ViewModelProviders.of(this).get(DeviceViewModel::class.java)
        deviceViewModel.getDeviceData().observe(this, Observer { devList ->
            if (devList.isNotEmpty()) {
                setupRecyclerView(rv, devList)
                product_recyclerview.setVisibility(View.VISIBLE)
                noLayout.setVisibility(View.GONE)
            } else {
                noText.setText(getString(R.string.getting_device_info))
                product_recyclerview.setVisibility(View.GONE)
                noLayout.setVisibility(View.VISIBLE)
            }
        })

        return mainView
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, devList: List<ProductEntity>) {
        val gridDivider = ContextCompat.getDrawable(recyclerView.context, R.drawable.grid_divider)
        recyclerView.layoutManager = GridLayoutManager(recyclerView.context, 2)
        gridDivider?.let { GridDividerItemDecoration(it, gridDivider, 2) }
                ?.let {
                    recyclerView.addItemDecoration(
                            it
                    )
                }
        recyclerView.adapter = DeviceTypeAdapter(mainActivity!!, mainActivity!!, devList)
    }
}

package com.designdemo.uaha.view.product.os

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

class OsFragment : Fragment() {

    private var mainActivity: FragmentActivity? = null

    private lateinit var osViewModel: OsViewModel

    @Nullable
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val mainView = inflater.inflate(R.layout.fragment_prod_list, container, false)

        val rv = mainView.recyclerview

        mainActivity = activity

        osViewModel = ViewModelProviders.of(this).get(OsViewModel::class.java)
        osViewModel.getOsData().observe(this, Observer { osList ->
            if (osList.isNotEmpty()) {
                setupRecyclerView(rv, osList)
            }
        })

        return mainView
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, devList : List<ProductItem>) {
        val llm = LinearLayoutManager(recyclerView.context)
        recyclerView.layoutManager = llm
        recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, llm.orientation))
        recyclerView.adapter = OsTypeAdapter(mainActivity!!, mainActivity!!, osViewModel.getOsData().value!!)
    }


}

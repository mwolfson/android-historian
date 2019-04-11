package com.designdemo.uaha.view.product.os

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.*
import com.designdemo.uaha.data.model.product.ProductEntity
import com.support.android.designlibdemo.R
import kotlinx.android.synthetic.main.fragment_prod_list.product_recyclerview
import kotlinx.android.synthetic.main.fragment_prod_list.view.product_recyclerview
import kotlinx.android.synthetic.main.fragment_prod_list.view.product_nodata_text
import kotlinx.android.synthetic.main.fragment_prod_list.view.product_nodata_layout

class OsFragment : Fragment() {

    private var mainActivity: FragmentActivity? = null

    private lateinit var osViewModel: OsViewModel

    @Nullable
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val mainView = inflater.inflate(R.layout.fragment_prod_list, container, false)

        val rv = mainView.product_recyclerview
        val noText = mainView.product_nodata_text
        val noLayout = mainView.product_nodata_layout

        mainActivity = activity

        osViewModel = ViewModelProviders.of(this).get(OsViewModel::class.java)
        osViewModel.getOsData().observe(this, Observer { osList ->
            if (osList.isNotEmpty()) {
                setupRecyclerView(rv, osList)
                product_recyclerview.setVisibility(View.VISIBLE)
                noLayout.setVisibility(View.GONE)
            } else {
                noText.setText(getString(R.string.getting_os_info))
                product_recyclerview.setVisibility(View.GONE)
                noLayout.setVisibility(View.VISIBLE)
            }
        })

        return mainView
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, devList: List<ProductEntity>) {
        val llm = LinearLayoutManager(recyclerView.context)
        recyclerView.layoutManager = llm
        recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, llm.orientation))
        recyclerView.adapter = OsTypeAdapter(mainActivity!!, mainActivity!!, osViewModel.getOsData().value!!)
    }
}

package com.designdemo.uaha.view.product.fav

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.designdemo.uaha.data.model.product.ProductEntity
import com.designdemo.uaha.view.product.device.DeviceViewModel
import com.support.android.designlibdemo.R
import kotlinx.android.synthetic.main.fragment_prod_list.product_recyclerview
import kotlinx.android.synthetic.main.fragment_prod_list.view.product_recyclerview
import kotlinx.android.synthetic.main.fragment_prod_list.view.product_nodata_text
import kotlinx.android.synthetic.main.fragment_prod_list.view.product_nodata_layout
import kotlinx.android.synthetic.main.fragment_prod_list.view.product_nodata_img

class FavFragment : Fragment() {

    private var mainActivity: FragmentActivity? = null

    private lateinit var favViewModel: FavViewModel

    @Nullable
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val mainView = inflater.inflate(R.layout.fragment_prod_list, container, false)

        val rv = mainView.product_recyclerview
        val noText = mainView.product_nodata_text
        val noLayout = mainView.product_nodata_layout
        val noTextImg = mainView.product_nodata_img

        mainActivity = activity
        favViewModel = ViewModelProvider(this).get(FavViewModel::class.java)
        favViewModel.getFavData().observe(viewLifecycleOwner, Observer { favList ->
            if (favList.isNotEmpty()) {
                setupRecyclerView(rv, favList)
                rv.visibility = View.VISIBLE
                noLayout.visibility = View.GONE
            } else {
                noText.text = getString(R.string.add_fav_text)
                noTextImg.visibility = View.VISIBLE
                noLayout.visibility = View.VISIBLE
                product_recyclerview.visibility = View.GONE
            }
        })

        return mainView
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, devList: List<ProductEntity>) {
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.adapter = FavTypeAdapter(mainActivity!!, favViewModel.getFavData().value!!)
    }
}

package com.designdemo.uaha.view.product

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
import com.designdemo.uaha.util.InjectorUtils
import com.designdemo.uaha.view.product.adapter.DeviceTypeAdapter
import com.designdemo.uaha.view.product.adapter.FavTypeAdapter
import com.designdemo.uaha.view.product.adapter.OsTypeAdapter
import com.dgreenhalgh.android.simpleitemdecoration.grid.GridDividerItemDecoration
import com.support.android.designlibdemo.R
import kotlinx.android.synthetic.main.fragment_prod_list.view.*

class ProductFragment : Fragment() {

    private var thisFragType: Int? = 0
    private var mainActivity: FragmentActivity? = null

    private lateinit var productViewModel: ProductViewModel

    private lateinit var devices: List<ProductItem>
    private lateinit var oses: List<ProductItem>
    private lateinit var faves: List<ProductItem>

    @Nullable
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val mainView = inflater.inflate(R.layout.fragment_prod_list, container, false)

        val rv = mainView.recyclerview

        mainActivity = activity

        val viewModelFactory = InjectorUtils.provideProductViewModelFactory()
        devices = listOf()
        oses = listOf()
        faves = listOf()

        thisFragType = arguments?.getInt(ARG_FRAG_TYPE, 0)

        productViewModel = ViewModelProviders.of(this, viewModelFactory).get(ProductViewModel::class.java)

        productViewModel.initOsList()
        productViewModel.getOsData().observe(this, Observer { osesIn ->
            oses = osesIn
            setupRecyclerView(rv)
        })

        //put this in a when to isolate it to only this type
        productViewModel.initDeviceList()
        productViewModel.getDeviceData().observe(this, Observer { devicesIn ->
            devices = devicesIn
            setupRecyclerView(rv)
        })

        productViewModel.initFavList(context!!)
        productViewModel.getFavData().observe(this, Observer { favesIn ->
            faves = favesIn
            setupRecyclerView(rv)
        })
        return mainView
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        when (thisFragType) {
            FRAG_TYPE_OS -> {
                val llm = LinearLayoutManager(recyclerView.context)
                recyclerView.layoutManager = llm
                recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, llm.orientation))
                recyclerView.adapter = OsTypeAdapter(mainActivity!!, mainActivity!!, oses)
            }
            FRAG_TYPE_DEVICE -> {
                val gridDivider = ContextCompat.getDrawable(recyclerView.context, R.drawable.grid_divider)
                recyclerView.layoutManager = GridLayoutManager(recyclerView.context, 2)
                recyclerView.addItemDecoration(GridDividerItemDecoration(gridDivider, gridDivider, 2))
                recyclerView.adapter = DeviceTypeAdapter(mainActivity!!, mainActivity!!, devices)
            }
            FRAG_TYPE_FAV -> {
                recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                recyclerView.adapter = FavTypeAdapter(mainActivity!!, faves)
            }
        }
    }

    companion object {

        const val FRAG_TYPE_OS = 1
        const val FRAG_TYPE_DEVICE = 2
        const val FRAG_TYPE_FAV = 3

        const val ARG_FRAG_TYPE = "frag_arg_type"
    }

}

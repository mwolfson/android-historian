package com.designdemo.uaha.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.*
import com.designdemo.uaha.data.model.VersionData
import com.designdemo.uaha.view.adapter.OsTypeAdapter
import com.designdemo.uaha.view.adapter.FavTypeAdapter
import com.designdemo.uaha.view.adapter.DeviceTypeAdapter
import com.designdemo.uaha.util.PrefsUtil
import com.dgreenhalgh.android.simpleitemdecoration.grid.GridDividerItemDecoration
import com.support.android.designlibdemo.R
import kotlinx.android.synthetic.main.fragment_prod_list.view.*
import java.util.*

class ProductListFragment : Fragment() {

    private var thisFragType: Int? = 0
    private var mainActivity: FragmentActivity? = null

    private val dataList: List<String>
        get() {
            var list = ArrayList<String>(VersionData.NUM_OF_OS)
            when (thisFragType) {
                FRAG_TYPE_OS -> for (x in 0..VersionData.NUM_OF_OS) {
                    list.add(VersionData.osStrings[x])
                }
                FRAG_TYPE_DEVICE -> for (x in 0..VersionData.NUM_OF_DEVICES) {
                    list.add(VersionData.deviceStrings[x])
                }
                FRAG_TYPE_FAV -> {
                    list = PrefsUtil.getFavorites(context)
                    Log.d("MSW", "List of products is sized:" + list.size)
                }
            }
            return list
        }

    @Nullable
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val mainView = inflater.inflate(R.layout.fragment_prod_list, container, false)

        val rv = mainView.recyclerview

        mainActivity = activity

        thisFragType = arguments?.getInt(ARG_FRAG_TYPE, 0)
        setupRecyclerView(rv)

        return mainView
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        when (thisFragType) {
            FRAG_TYPE_OS -> {
                val llm = LinearLayoutManager(recyclerView.context)
                recyclerView.layoutManager = llm
                recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, llm.orientation))
                recyclerView.adapter = OsTypeAdapter(mainActivity!!, mainActivity!!, dataList)
            }
            FRAG_TYPE_DEVICE -> {
                val gridDivider = ContextCompat.getDrawable(recyclerView.context, R.drawable.grid_divider)
                recyclerView.layoutManager = GridLayoutManager(recyclerView.context, 2)
                recyclerView.addItemDecoration(GridDividerItemDecoration(gridDivider, gridDivider, 2))
                recyclerView.adapter = DeviceTypeAdapter(mainActivity!!, mainActivity!!, dataList)
            }
            FRAG_TYPE_FAV -> {
                recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                recyclerView.adapter = FavTypeAdapter(mainActivity!!, dataList)
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

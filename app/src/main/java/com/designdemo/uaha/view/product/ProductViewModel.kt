package com.designdemo.uaha.view.product

import android.content.Context
import androidx.lifecycle.ViewModel
import com.designdemo.uaha.data.model.VersionData
import com.designdemo.uaha.data.model.product.ProductItem
import com.designdemo.uaha.data.model.product.ProductRepository
import com.designdemo.uaha.util.PrefsUtil
import java.util.*

class ProductViewModel(private val productRepository: ProductRepository) : ViewModel() {

    fun getDeviceData() = productRepository.getDeviceData()

    fun getOsData() = productRepository.getOsData()

    fun getFavData() = productRepository.getFavData()

    fun initDeviceList() {
        var devNameList = ArrayList<String>(VersionData.NUM_OF_DEVICES)
        var deviceList = ArrayList<ProductItem>()
        for (x in 0..VersionData.NUM_OF_DEVICES) {
            devNameList.add(VersionData.deviceStrings[x])
        }

        devNameList.forEach {
            val itemId = VersionData.getOsNum(it)
            val parts = it.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            val device = ProductItem(parts[0], parts[1], VersionData.getOsDrawable(itemId), VersionData.getWikiQuery(itemId))

            deviceList.add(device)
        }

        productRepository.initDeviceData(deviceList)
    }

    fun initOsList() {
        var osNameList = ArrayList<String>(VersionData.NUM_OF_OS)
        var osList = ArrayList<ProductItem>()
        for (x in 0..VersionData.NUM_OF_OS) {
            osNameList.add(VersionData.osStrings[x])
        }

        osNameList.forEach {
            val itemId = VersionData.getOsNum(it)
            val parts = it.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            val device = ProductItem(parts[0], parts[1], VersionData.getOsDrawable(itemId), VersionData.getWikiQuery(itemId))

            osList.add(device)
        }

        productRepository.initOsList(osList)
    }

    fun initFavList(cxt: Context) {
        var favNameList = PrefsUtil.getFavorites(cxt)
        var favList = ArrayList<ProductItem>()
        favNameList.forEach {
            val itemId = VersionData.getOsNum(it)
            val parts = it.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            val device = ProductItem(parts[0], parts[1], VersionData.getOsDrawable(itemId), VersionData.getWikiQuery(itemId))

            favList.add(device)
        }
        productRepository.initFavesList(favList)
    }

}
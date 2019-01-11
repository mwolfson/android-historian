package com.designdemo.uaha.view.product

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.designdemo.uaha.data.InfoDatabase
import com.designdemo.uaha.data.model.product.ProductItem
import com.designdemo.uaha.data.model.product.ProductRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ProductViewModel(application: Application) : AndroidViewModel(application) {

    private var parentJob = Job()
    private val couroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(couroutineContext)

    val repository : ProductRepository
    val allDevices : LiveData<List<ProductItem>>
    val allOses : LiveData<List<ProductItem>>
    val allFaves : LiveData<List<ProductItem>>

    init {
        val productItemDao = InfoDatabase.getDatabase(application, scope).productDao()
        repository = ProductRepository(productItemDao)

        allDevices = repository.allDeviceInfo
        allOses = repository.allOsInfo
        allFaves = repository.allFaveInfo
    }

    //Note, we are calling this from the addUserData, after validation is performed
    fun insert(productItem: ProductItem) = scope.launch(Dispatchers.IO){
        repository.insertItem(productItem)
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

//    fun getDeviceData() = productRepository.getDeviceData()
//
//    fun getOsData() = productRepository.getOsData()
//
//    fun getFavData() = productRepository.getFavData()
//
//    fun initDeviceList() {
//        var devNameList = ArrayList<String>(VersionData.NUM_OF_DEVICES)
//        var deviceList = ArrayList<ProductItem>()
//        for (x in 0..VersionData.NUM_OF_DEVICES) {
//            devNameList.add(VersionData.deviceStrings[x])
//        }
//
//        devNameList.forEach {
//            val itemId = VersionData.getOsNum(it)
//            val parts = it.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
//
//            val device = ProductItem(parts[0], parts[1], VersionData.getOsDrawable(itemId), VersionData.getWikiQuery(itemId))
//
//            deviceList.add(device)
//        }
//
//        productRepository.initDeviceData(deviceList)
//    }
//
//    fun initOsList() {
//        var osNameList = ArrayList<String>(VersionData.NUM_OF_OS)
//        var osList = ArrayList<ProductItem>()
//        for (x in 0..VersionData.NUM_OF_OS) {
//            osNameList.add(VersionData.osStrings[x])
//        }
//
//        osNameList.forEach {
//            val itemId = VersionData.getOsNum(it)
//            val parts = it.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
//
//            val device = ProductItem(parts[0], parts[1], VersionData.getOsDrawable(itemId), VersionData.getWikiQuery(itemId))
//
//            osList.add(device)
//        }
//
//        productRepository.initOsList(osList)
//    }
//
//    fun initFavList(cxt: Context) {
//        var favNameList = PrefsUtil.getFavorites(cxt)
//        var favList = ArrayList<ProductItem>()
//        favNameList.forEach {
//            val itemId = VersionData.getOsNum(it)
//            val parts = it.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
//
//            val device = ProductItem(parts[0], parts[1], VersionData.getOsDrawable(itemId), VersionData.getWikiQuery(itemId))
//
//            favList.add(device)
//        }
//        productRepository.initFavesList(favList)
//    }

}
package com.designdemo.uaha.net
//
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

import com.designdemo.uaha.data.model.detail.DetailEntity
import kotlinx.coroutines.Deferred
import retrofit2.Response

//
interface FonoApiService {

    /**
     * Search devices by specific informaton
     *
     * @param token api token (required)
     * @param device query info - device id, etc
     * @param brand manufacturer name
     * @param position position of device in result set (optional). omit, for only 1 result
     */
    @GET("/v1/getdevice")
    fun getDevice(
            @Query("token") token: String,
            @Query("device") device: String,
            @Query( "brand") brand: String? = null,
            @Query("position") position: Int? = null
    ): Deferred<Response<List<DetailEntity>>>

    /*
     * Get a list of the latest devices that were asdded
     *
     * @param token api token (required)
     * @param brand query info - device id, etc
     * @param limit max number of results, up to 100 (optional)
     */
    @GET("/v1/getlatest")
    fun getLatest(
            @Query("token") token: String,
            @Query("brand") brand: String,
            @Query("limit") limit: Int? = null
    ): Call<List<DetailEntity>>
//
}
package com.designdemo.uaha.net

import retrofit2.http.GET
import retrofit2.http.Query

import com.designdemo.uaha.data.model.detail.DetailEntity
import kotlinx.coroutines.Deferred
import retrofit2.Response

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
        @Query("brand") brand: String? = null,
        @Query("position") position: Int? = null
    ): Deferred<Response<List<DetailEntity>>>
}
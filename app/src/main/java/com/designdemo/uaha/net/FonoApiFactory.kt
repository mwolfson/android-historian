package com.designdemo.uaha.net

import okhttp3.OkHttpClient
import retrofit2.Retrofit

import retrofit2.converter.gson.GsonConverterFactory

class FonoApiFactory {

    @JvmOverloads
    fun create(okHttpClient: OkHttpClient? = null): FonoApiService = run {
        val retrofitBuilder = Retrofit.Builder()
                .baseUrl(baseUrlFono)
                .addConverterFactory(GsonConverterFactory.create())

        okHttpClient?.let {
            retrofitBuilder.client(okHttpClient)
        }

        retrofitBuilder.build().create(FonoApiService::class.java)
    }

    companion object {
        const val baseUrlFono = "https://fonoapi.freshpixl.com/"
    }

}
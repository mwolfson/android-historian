package com.designdemo.uaha.net

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

import retrofit2.converter.gson.GsonConverterFactory

class WikiApiFactory {

    @JvmOverloads
    fun create(okHttpClient: OkHttpClient? = null): WikiApiService = run {
        val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient().newBuilder()
                .addInterceptor(interceptor)
                .build()

        val retrofit = Retrofit.Builder()
                .baseUrl(baseUrlWiki)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        retrofit.create(WikiApiService::class.java)
    }

    companion object {
        const val baseUrlWiki = "https://en.wikipedia.org/"
    }
}
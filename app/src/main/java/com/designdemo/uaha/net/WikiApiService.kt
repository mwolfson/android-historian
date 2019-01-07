package com.designdemo.uaha.net
//
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

import com.designdemo.uaha.data.model.wiki.WikiResponse

interface WikiApiService {

    /**
     * val req = "https://en.wikipedia.org/w/api.php?action=query&titles=" + VersionData.getWikiQuery(osVersion) + "&prop=revisions&rvprop=content&format=json"
     *
     */
    @GET("/w/api.php?action=query")
    fun getWikiResponse(
            @Query("titles") queryTitle: String = "Android",
            @Query("prop") propIn: String = "revisions",
            @Query("format") formatIn: String = "json",
            @Query("rvprop") rvPropIn: String = "content"
    ): Call<WikiResponse>

}
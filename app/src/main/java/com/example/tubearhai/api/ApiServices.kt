package com.example.tubearhai.api

import com.example.tubearhai.model.BearData
import com.example.tubearhai.model.BearDataItem
import com.example.tubearhai.utilities.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {
    // https://api.punkapi.com/v2/beers?page=1&per_page=20
    @GET(Constants.beers)
    suspend fun getBearData(@Query("page") page : Int, @Query("per_page") per_page : Int) : Response<BearData>
}
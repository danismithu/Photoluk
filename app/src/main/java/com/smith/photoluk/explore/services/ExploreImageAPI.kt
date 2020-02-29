package com.smith.photoluk.explore.services

import com.smith.photoluk.explore.response.ErrorsResponse
import com.smith.photoluk.explore.response.ImageDataQueryResponse
import com.smith.photoluk.explore.response.ImageDataResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ExploreImageAPI {
    @GET("photos")
    fun getImageFeedAsync(@Query(value = "page") page: Int, @Query(value = "client_id") clientId: String): Deferred<Response<List<ImageDataResponse>>>

    @GET("photos")
    fun getImageErrorsAsync(@Query(value = "page") page: Int, @Query(value = "client_id") clientId: String): Deferred<Response<ErrorsResponse>>

    @GET("/search/photos")
    fun getImageQueryAsync(@Query(value = "query") query: String, @Query(value = "client_id") clientId: String,
                           @Query(value = "page") page: Int): Deferred<Response<ImageDataQueryResponse>>
}
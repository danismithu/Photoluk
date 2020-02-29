package com.smith.photoluk.explore.services

import com.smith.photoluk.explore.response.ImageDataResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ExploreImageAPI {
    @GET("photos")
    fun getImageFeedAsync(@Query(value = "page") page: Int, @Query(value = "client_id") clientId: String): Deferred<Response<List<ImageDataResponse>>>
}
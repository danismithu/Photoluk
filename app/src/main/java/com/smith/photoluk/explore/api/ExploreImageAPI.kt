package com.smith.photoluk.explore.api

import com.smith.photoluk.explore.request.ImageDataRequest
import com.smith.photoluk.explore.response.ImageDataResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET

interface ExploreImageAPI {
    @GET
    suspend fun getImageFeed(@Body imageDataRequest: ImageDataRequest): Deferred<Response<ImageDataResponse>>
}
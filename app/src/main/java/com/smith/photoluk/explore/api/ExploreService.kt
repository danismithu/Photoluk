package com.smith.photoluk.explore.api

import com.smith.photoluk.explore.request.ImageDataRequest
import com.smith.photoluk.explore.response.ImageDataResponse
import retrofit2.Response
import retrofit2.http.GET

interface ExploreService {

    suspend fun getImageListFeed(imageDataRequest: ImageDataRequest): Response<List<ImageDataResponse>>?
}
package com.smith.photoluk.explore.services

import com.smith.photoluk.explore.request.ImageDataRequest
import com.smith.photoluk.explore.response.ErrorsResponse
import com.smith.photoluk.explore.response.ImageDataQueryResponse
import com.smith.photoluk.explore.response.ImageDataResponse
import com.smith.photoluk.models.ImageData
import retrofit2.Response

interface ExploreService {
    suspend fun getImageListFeed(imageDataRequest: ImageDataRequest): Response<List<ImageDataResponse>>?

    suspend fun getImageErrors(imageDataRequest: ImageDataRequest): Response<ErrorsResponse>?

    suspend fun getImageQueryFeed(imageDataRequest: ImageDataRequest): Response<ImageDataQueryResponse>?

    suspend fun getImageByUnsplashId(id: String): List<ImageData>?
}
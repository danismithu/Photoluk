package com.smith.photoluk.explore.services

import com.smith.photoluk.explore.repository.ExploreRepository
import com.smith.photoluk.explore.request.ImageDataRequest
import com.smith.photoluk.explore.response.ImageDataResponse
import com.smith.photoluk.models.ImageData
import com.smith.photoluk.utils.api.BaseRepository
import retrofit2.Response

class ExploreServiceImpl(private val api: ExploreImageAPI,
                         private val repository: ExploreRepository): BaseRepository(), ExploreService {

    override suspend fun getImageListFeed(imageDataRequest: ImageDataRequest): Response<List<ImageDataResponse>>? {
        return safeApiResponseCall(
            call = {
                api.getImageFeedAsync(imageDataRequest.page!!, imageDataRequest.clientId!!).await()
            },
            errorMessage = "Error obteniendo las imágenes"
        )
    }

    override suspend fun getImageByUnsplashId(id: String): List<ImageData>? {
        return safeApiCall(
            call = { repository.findImageByUnsplashIdAsync(id).await() },
            errorMessage = "Error obteniendo la imagen"
        )
    }
}
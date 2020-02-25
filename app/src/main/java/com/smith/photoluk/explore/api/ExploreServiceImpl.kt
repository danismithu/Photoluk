package com.smith.photoluk.explore.api

import com.smith.photoluk.explore.request.ImageDataRequest
import com.smith.photoluk.explore.response.ImageDataResponse
import com.smith.photoluk.utils.api.BaseRepository

class ExploreServiceImpl(private val api: ExploreImageAPI): BaseRepository(), ExploreService {

    override suspend fun getImageListFeed(imageDataRequest: ImageDataRequest): ImageDataResponse? {
        return safeApiCall(
            call = {api.getImageFeed(imageDataRequest).await()},
            errorMessage = "Error obteniendo las imágenes"
        )
    }
}
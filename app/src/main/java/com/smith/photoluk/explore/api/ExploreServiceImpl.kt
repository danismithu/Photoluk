package com.smith.photoluk.explore.api

import com.smith.photoluk.explore.request.ImageDataRequest
import com.smith.photoluk.explore.response.ImageDataResponse
import com.smith.photoluk.utils.api.BaseRepository
import retrofit2.Response

class ExploreServiceImpl(private val api: ExploreImageAPI): BaseRepository(), ExploreService {

    override suspend fun getImageListFeed(imageDataRequest: ImageDataRequest): Response<List<ImageDataResponse>>? {
        return safeApiResponseCall(
            call = {api.getImageFeed(imageDataRequest.page!!, imageDataRequest.clientId!!).await()},
            errorMessage = "Error obteniendo las imágenes"
        )
    }
}
package com.smith.photoluk.explore.services

import com.smith.photoluk.explore.repository.ExploreRepository
import com.smith.photoluk.explore.request.ImageDataRequest
import com.smith.photoluk.explore.response.ErrorsResponse
import com.smith.photoluk.explore.response.ImageDataQueryResponse
import com.smith.photoluk.explore.response.ImageDataResponse
import com.smith.photoluk.models.ImageData
import com.smith.photoluk.utils.api.BaseRepository
import retrofit2.Response

class ExploreServiceImpl(private val api: ExploreImageAPI,
                         private val repository: ExploreRepository): BaseRepository(), ExploreService {

    override suspend fun getImageListFeed(imageDataRequest: ImageDataRequest): Response<List<ImageDataResponse>>? {
        return safeApiResponseCall(
            call = { api.getImageFeedAsync(imageDataRequest.page!!, imageDataRequest.clientId!!).await() },
            errorMessage = "Error obteniendo las imágenes"
        )
    }

    override suspend fun getImageErrors(imageDataRequest: ImageDataRequest): Response<ErrorsResponse>? {
        return safeApiResponseCall(
            call = { api.getImageErrorsAsync(imageDataRequest.page!!, imageDataRequest.clientId!!).await() },
            errorMessage = "Error obteniendo el lsitado de errores"
        )
    }

    override suspend fun getImageQueryFeed(imageDataRequest: ImageDataRequest): Response<ImageDataQueryResponse>? {
        return safeApiResponseCall(
            call = { api.getImageQueryAsync(imageDataRequest.query!!, imageDataRequest.clientId!!,
                imageDataRequest.page!!).await() },
            errorMessage = "Error obteniendo los resultados filtrados"
        )
    }

    override suspend fun getImageByUnsplashId(id: String): List<ImageData>? {
        return safeApiCall(
            call = { repository.findImageByUnsplashIdAsync(id).await() },
            errorMessage = "Error obteniendo la imagen"
        )
    }
}
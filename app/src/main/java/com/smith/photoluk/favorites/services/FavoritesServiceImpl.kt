package com.smith.photoluk.favorites.services

import com.smith.photoluk.models.ImageData
import com.smith.photoluk.favorites.repository.FavoritesRepository
import com.smith.photoluk.utils.api.BaseRepository

class FavoritesServiceImpl(private val favoritesRepository: FavoritesRepository): BaseRepository(), FavoritesService {
    override suspend fun getFavoriteImages(): List<ImageData>? {
        return safeApiCall(
            call = { favoritesRepository.getFavoriteImagesAsync().await() },
            errorMessage = "Error obteniendo las imágenes favoritas"
        )
    }

    override suspend fun getQueryFavorite(query: String): List<ImageData>? {
        return safeApiCall(
            call = { favoritesRepository.getQueryFavoriteAsync(query).await() },
            errorMessage = "Error obteniendo las imágenes favoritas filtradas"
        )
    }
}
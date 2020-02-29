package com.smith.photoluk.favorites.services

import com.smith.photoluk.models.ImageData

interface FavoritesService {
    suspend fun getFavoriteImages(): List<ImageData>?
}
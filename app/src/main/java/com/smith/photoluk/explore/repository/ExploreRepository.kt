package com.smith.photoluk.explore.repository

import android.util.Log
import com.orm.SugarRecord
import com.smith.photoluk.models.ImageData
import com.smith.photoluk.utils.models.Result
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class ExploreRepository {
    fun findImageByUnsplashIdAsync(id: String): Deferred<Result<List<ImageData>>> {
        return GlobalScope.async {
            try {
                // Buscar si existe imagen guardada para agregar ícono de favorito
                val query = "SELECT * FROM imageData WHERE unsplashId=?"
                val savedImages: List<ImageData> = SugarRecord.findWithQuery(ImageData::class.java, query, id).toList()
                Log.d("QUERY_FAV", "Favoritos recuperados: $savedImages")

                Result.Success(savedImages)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }
}
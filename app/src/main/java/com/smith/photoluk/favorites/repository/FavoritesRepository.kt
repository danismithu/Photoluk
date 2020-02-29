package com.smith.photoluk.favorites.repository

import android.util.Log
import com.orm.SugarRecord
import com.smith.photoluk.models.ImageData
import com.smith.photoluk.utils.models.Result
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.lang.Exception

class FavoritesRepository {
    fun getFavoriteImagesAsync(): Deferred<Result<List<ImageData>>> {
        return GlobalScope.async {
            try {
                // Obtener las imágenes guardadas, las cuales son las marcadas como favoritas
                val query = "SELECT * FROM imageData"
                val savedImages: List<ImageData> = SugarRecord.findWithQuery(ImageData::class.java, query).toList()
                Log.d("QUERY_FAV", "Favoritos recuperados: $savedImages")

                Result.Success(savedImages)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }
}
package com.smith.photoluk.favorites.viewModels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.smith.photoluk.favorites.fragments.FavoritesFrag
import com.smith.photoluk.favorites.repository.FavoritesRepository
import com.smith.photoluk.favorites.services.FavoritesServiceImpl
import com.smith.photoluk.models.ImageData
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class FavoritesViewModel(private val favoritesFrag: FavoritesFrag): ViewModel() {

    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.IO

    private val scope = CoroutineScope(coroutineContext)

    private var viewCallback: FavoritesFrag? = null

    private val favoritesService: FavoritesServiceImpl = FavoritesServiceImpl(FavoritesRepository())
    private lateinit var imagesLiveData: MutableLiveData<List<ImageData>>
    private var loading = false

    companion object {
        private const val FETCHING_FAV_ERROR: String = "Ocurrió un problema obteniendo las imágenes favoritas"
    }

    //Setup para realizar la búsqueda interna
    fun getFavoriteImages() {
        imagesLiveData = MutableLiveData()

        loading = true
        fetchFavoriteImages()
        imagesLiveData.observe(favoritesFrag, Observer {
            val response = imagesLiveData.value
            if (response != null) {
                fetchingComplete(response)
            } else {
                Log.e("FETCHING_ERROR", "$FETCHING_FAV_ERROR: La respuesta es null")
                fetchingFavoritesFailed(FETCHING_FAV_ERROR)
            }
        })
    }

    fun getFavoriteQuery(query: String) {
        imagesLiveData = MutableLiveData()

        loading = true
        fetchFavoriteQuery(query)
        imagesLiveData.observe(favoritesFrag, Observer {
            val response = imagesLiveData.value
            if (response != null) {
                fetchingComplete(response)
            } else {
                Log.e("FETCHING_ERROR", "$FETCHING_FAV_ERROR: La respuesta es null")
                fetchingFavoritesFailed(FETCHING_FAV_ERROR)
            }
        })
    }

    private fun fetchFavoriteImages() {
        scope.launch {
            try {
                val favoritesImages: List<ImageData>? = favoritesService.getFavoriteImages()
                if (favoritesImages != null) {
                    Log.d("FETCHING_SUCCESS", "Se han recuperado las imágenes favoritas satisfactoriamente.")
                    fetchingFavoritesSuccess(favoritesImages)
                } else {
                    Log.e("FETCHING_ERROR", "${FETCHING_FAV_ERROR}: La respuesta es null")
                    fetchingFavoritesFailed(FETCHING_FAV_ERROR)
                }
            } catch (e: Exception) {
                Log.e("FETCHING_ERROR", "${FETCHING_FAV_ERROR}: ${e.message}")
                fetchingFavoritesFailed(FETCHING_FAV_ERROR)
            }
        }
    }

    private fun fetchFavoriteQuery(query: String) {
        scope.launch {
            try {
                val queryFavoriteImages: List<ImageData>? = favoritesService.getQueryFavorite(query)
                if (queryFavoriteImages != null) {
                    Log.d("FETCHING_SUCCESS", "Se han recuperado las imágenes favoritas filtradas satisfactoriamente.")
                    fetchingFavoritesSuccess(queryFavoriteImages)
                } else {
                    Log.e("FETCHING_ERROR", "${FETCHING_FAV_ERROR}: La respuesta es null")
                    fetchingFavoritesFailed(FETCHING_FAV_ERROR)
                }
            } catch (e: Exception) {
                Log.e("FETCHING_ERROR", "${FETCHING_FAV_ERROR}: ${e.message}")
                fetchingFavoritesFailed(FETCHING_FAV_ERROR)
            }
        }
    }

    //Obtener la imagen a través de la URL de esta
    private fun fetchImageByURL(url: String): RequestCreator {
        val picasso = Picasso.get()
        return picasso.load(url)
    }

    private fun fetchingFavoritesSuccess(favoriteImages: List<ImageData>) {
        for (imgFavData in favoriteImages) {
            imgFavData.picassoImageRequest = fetchImageByURL(imgFavData.full!!)
            imgFavData.picassoProfileRequest = fetchImageByURL(imgFavData.medium!!)
        }

        imagesLiveData.postValue(favoriteImages)
    }

    private fun fetchingComplete(favoriteImages: List<ImageData>) {
        loading = false
        if (viewCallback != null) {
            viewCallback!!.populateFavorites(favoriteImages)
        }
    }

    private fun fetchingFavoritesFailed(msg: String) {
        loading = false
        viewCallback!!.showMessage(msg)
    }

    fun onViewAttached(viewCallback: FavoritesFrag) {
        this.viewCallback = viewCallback
    }
}
package com.smith.photoluk.explore.view_models

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.smith.photoluk.explore.fragments.ExploreFrag
import com.smith.photoluk.explore.repository.ExploreRepository
import com.smith.photoluk.explore.request.ImageDataRequest
import com.smith.photoluk.explore.response.ImageDataResponse
import com.smith.photoluk.explore.services.ExploreServiceImpl
import com.smith.photoluk.models.ImageData
import com.smith.photoluk.models.ProfileImages
import com.smith.photoluk.models.Urls
import com.smith.photoluk.models.UserInfo
import com.smith.photoluk.utils.api.RestClient
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class ExploreViewModel(private val exploreFrag: ExploreFrag) : ViewModel() {

    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.IO

    private val scope = CoroutineScope(coroutineContext)

    private var viewCallback: ExploreFrag? = null

    private val exploreService: ExploreServiceImpl = ExploreServiceImpl(RestClient.retrofitServiceImageFeed(), ExploreRepository())
    private lateinit var imagesLiveData: MutableLiveData<List<ImageData>>

    private var loading = false

    companion object {
        private const val FETCHING_ERROR: String = "Ocurrió un problema recuperando las imágenes"
    }

    //Setup para realizar la búsqueda
    fun getImagesFeed(page: Int) {
        imagesLiveData = MutableLiveData()
        val clientId = "**********************" //Ingresar su propio unstash clientId

        val request = ImageDataRequest()
        request.clientId = clientId
        request.page = page

        loading = true
        fetchImagesFeed(request)
        imagesLiveData.observe(exploreFrag, Observer {
            val response = imagesLiveData.value
            if (response != null) {
                fetchingComplete(response)
            } else {
                Log.e("FETCHING_ERROR", "$FETCHING_ERROR: La respuesta es null")
                fetchingFeedFailed(FETCHING_ERROR)
            }
        })
    }

    fun isSomethingLoading(imageList: List<ImageData>): Boolean {
        val areImagesLoading: Boolean = imageList.any{ isLoading -> isLoading.isLoading }
        return areImagesLoading || loading
    }

    //Coroutine para obtener el listado de imágenes más recientes de Unsplash
    private fun fetchImagesFeed(request: ImageDataRequest) {
        scope.launch {
            try {
                val imageDataResponse: Response<List<ImageDataResponse>>? = exploreService.getImageListFeed(request)
                if (imageDataResponse != null) {
                    if (imageDataResponse.code() == 200) {
                        Log.d("FETCHING_SUCCESS", "Se han recuperado las imágenes satisfactoriamente.")

                        //Buscar si existen imágenes guardadas, lo que significa que están agregadas como favoritos para
                        // agregarle el corazón a la imagen en la pantalla de explorar
                        for (imageData in imageDataResponse.body()!!) {
                            val savedImages: List<ImageData>? = exploreService.getImageByUnsplashId(imageData.id)
                            if (savedImages != null && savedImages.isNotEmpty()) {
                                imageData.isFavorite = savedImages[0].isFavorite
                                imageData.internalId = savedImages[0].id
                            }
                        }
                        fetchingFeedSuccess(imageDataResponse.body()!!)
                    } else {
                        Log.e("FETCHING_ERROR", "$FETCHING_ERROR: La respuesta tiene código: " +
                                "${imageDataResponse.code()} - ${imageDataResponse.message()}")
                        fetchingFeedFailed(imageDataResponse.message())
                    }
                } else {
                    Log.e("FETCHING_ERROR", "$FETCHING_ERROR: La respuesta es null")
                    fetchingFeedFailed(FETCHING_ERROR)
                }
            } catch (e: Exception) {
                Log.e("FETCHING_ERROR", "$FETCHING_ERROR: ${e.message}")
                fetchingFeedFailed(FETCHING_ERROR)
            }
        }
    }

    //Obtener la imagen a través de su URL
    private fun fetchImageByURL(url: String): RequestCreator {
        val picasso = Picasso.get()
        return picasso.load(url)
    }

    private fun fetchingFeedSuccess(response: List<ImageDataResponse>) {

        val imageDataList: ArrayList<ImageData> = ArrayList()

        for (imgDataResponse in response) {
            val imageData = ImageData()
            imageData.unsplashId = imgDataResponse.id
            imageData.width = imgDataResponse.width
            imageData.height = imgDataResponse.height
            imageData.likes = imgDataResponse.likes

            imageData.full = imgDataResponse.urls.full

            imageData.username = imgDataResponse.user.username
            imageData.name = imgDataResponse.user.name

            imageData.linkProfile = imgDataResponse.user.links.html
            imageData.medium = imgDataResponse.user.profileImage.medium
            imageData.large = imgDataResponse.user.profileImage.large

            imageData.picassoImageRequest = fetchImageByURL(imageData.full!!)
            imageData.picassoProfileRequest = fetchImageByURL(imageData.medium!!)

            imageData.isFavorite = imgDataResponse.isFavorite

            imageData.id = if (imgDataResponse.internalId == 0L) {
                null
            } else {
                imgDataResponse.internalId
            }

            imageDataList.add(imageData)
        }

        imagesLiveData.postValue(imageDataList)
    }

    private fun fetchingComplete(imageList: List<ImageData>) {
        loading = false
        if (viewCallback != null) {
            viewCallback!!.populateFeed(imageList)
        }
    }

    private fun fetchingFeedFailed(message: String) {
        loading = false
        viewCallback!!.showMessage(message)
    }

    private fun fetchingFeedFailed(message: List<String>?) {

    }

    fun onViewAttached(viewCallback: ExploreFrag) {
        this.viewCallback = viewCallback
    }
}
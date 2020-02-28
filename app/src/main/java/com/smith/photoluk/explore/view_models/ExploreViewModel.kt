package com.smith.photoluk.explore.view_models

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.smith.photoluk.explore.api.ExploreServiceImpl
import com.smith.photoluk.explore.fragments.ExploreFrag
import com.smith.photoluk.explore.models.ImageData
import com.smith.photoluk.explore.request.ImageDataRequest
import com.smith.photoluk.explore.response.ImageDataResponse
import com.smith.photoluk.utils.api.RestClient
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class ExploreViewModel(private val exploreFrag: ExploreFrag) : ViewModel() {

    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.IO

    private val scope = CoroutineScope(coroutineContext)

    private var viewCallBack: ExploreFrag? = null

    private val exploreService: ExploreServiceImpl = ExploreServiceImpl(RestClient.retrofitServiceImageFeed())
    private lateinit var imagesLiveData: MutableLiveData<List<ImageData>>
    private var lastPage = 0
    private var loading = false

    companion object {
        private const val FETCHING_ERROR: String = "Ocurrió un problema recuperando las imágenes"
    }

    //Setup para realizar la búsqueda
    fun getImagesFeed(page: Int) {
        imagesLiveData = MutableLiveData()
        val clientId = "*******************" //Ingresar su propio unstash clientId

        val request = ImageDataRequest()
        request.clientId = clientId
        request.page = page
        lastPage = page

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

    //Obtener la imagen a través de la URL de esta
    private fun fetchImageByURL(url: String): RequestCreator {
        val picasso = Picasso.get()
        return picasso.load(url)
    }

    private fun fetchingFeedSuccess(response: List<ImageDataResponse>) {

        val imageDataList: ArrayList<ImageData> = ArrayList()

        for (imgDataResponse in response) {
            val imageData = ImageData()
            imageData.width = imgDataResponse.width
            imageData.height = imgDataResponse.height
            imageData.urls = imgDataResponse.urls
            imageData.likes = imgDataResponse.likes
            imageData.user = imgDataResponse.user

            imageData.picassoImageRequest = fetchImageByURL(imageData.urls!!.full)
            imageData.picassoProfileRequest = fetchImageByURL(imageData.user!!.profileImage.medium)

            imageDataList.add(imageData)
        }

        imagesLiveData.postValue(imageDataList)
    }

    private fun fetchingComplete(imageList: List<ImageData>) {
        loading = false
        if (viewCallBack != null) {
            viewCallBack!!.populateFeed(imageList, lastPage)
        }
    }

    private fun fetchingFeedFailed(message: String) {
        loading = false
    }

    private fun fetchingFeedFailed(message: List<String>?) {

    }

    fun onViewAttached(viewCallBack: ExploreFrag) {
        this.viewCallBack = viewCallBack
    }
}
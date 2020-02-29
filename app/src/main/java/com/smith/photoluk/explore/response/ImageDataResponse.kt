package com.smith.photoluk.explore.response

import com.smith.photoluk.utils.models.GenericResponse

data class ImageDataResponse (

    var id: String,

    var width: Int,

    var height: Int,

    var urls: UrlsResponse,

    var likes: Int,

    var user: UserInfoResponse,

    //Uso interno de favoritos
    var isFavorite: Boolean,

    //Uso interno del id guardado en la DB
    var internalId: Long

) : GenericResponse()
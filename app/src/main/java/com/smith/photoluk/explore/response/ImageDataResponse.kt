package com.smith.photoluk.explore.response

import com.smith.photoluk.utils.models.GenericResponse
import com.smith.photoluk.utils.models.Urls
import com.smith.photoluk.utils.models.UserInfo

data class ImageDataResponse (

    var width: Int,

    var height: Int,

    var urls: Urls,

    var likes: Int,

    var user: UserInfo

) : GenericResponse()
package com.smith.photoluk.explore.response

import com.smith.photoluk.utils.models.GenericResponse

data class ImageDataQueryResponse (
    var results: List<ImageDataResponse>
): GenericResponse()
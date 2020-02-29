package com.smith.photoluk.explore.response

import com.smith.photoluk.utils.models.GenericResponse

data class ErrorsResponse (
    var errors: List<String>
): GenericResponse()
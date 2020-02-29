package com.smith.photoluk.explore.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.smith.photoluk.utils.models.LinksResponse

data class UserInfoResponse (
    var username: String,

    var name: String,

    var links: LinksResponse,

    @SerializedName("profile_image")
    @Expose
    var profileImage: ProfileImagesResponse
)
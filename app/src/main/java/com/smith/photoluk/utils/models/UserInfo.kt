package com.smith.photoluk.utils.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserInfo (

    var userName: String,

    var name: String,

    @SerializedName("profile_image")
    @Expose
    var profileImage: ProfileImages
)
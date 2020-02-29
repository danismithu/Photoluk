package com.smith.photoluk.models

import com.orm.SugarRecord
import com.orm.dsl.Column
import com.orm.dsl.Ignore
import com.orm.dsl.Table
import com.squareup.picasso.RequestCreator
import java.io.Serializable

@Table(name = "imageData")
class ImageData: SugarRecord(), Serializable {

    @Column(name = "unsplashId")
    var unsplashId: String? = null

    @Column(name = "width")
    var width: Int? = null

    @Column(name = "height")
    var height: Int? = null

    @Column(name = "likes")
    var likes: Int? = null

    //@Ignore
    //var urls: Urls? = null
    @Column(name = "full")
    var full: String? = null

    //@Ignore
    //var user: UserInfo? = null
    @Column(name = "userName")
    var username: String? = null

    @Column(name = "medium")
    var medium: String? = null

    @Column(name = "large")
    var large: String? = null

    @Column(name = "name")
    var name: String? = null

    @Column(name = "linkProfile")
    var linkProfile: String? = null

    @Ignore
    @Transient
    var picassoImageRequest: RequestCreator? = null

    @Ignore
    @Transient
    var picassoProfileRequest: RequestCreator? = null

    @Column(name = "isLoading")
    var isLoading: Boolean = false

    @Column(name = "isFavorite")
    var isFavorite: Boolean = false
}
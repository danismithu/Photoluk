package com.smith.photoluk.explore.models

import com.orm.SugarRecord
import com.orm.dsl.Column
import com.orm.dsl.Table
import com.smith.photoluk.utils.models.Urls
import com.smith.photoluk.utils.models.UserInfo
import com.squareup.picasso.RequestCreator
import java.io.Serializable

@Table
class ImageData: SugarRecord(), Serializable {

    var width: Int? = null

    var height: Int? = null

    var urls: Urls? = null

    var likes: Int? = null

    var user: UserInfo? = null

    var picassoImageRequest: RequestCreator? = null

    var picassoProfileRequest: RequestCreator? = null

    var isLoading: Boolean = false
}
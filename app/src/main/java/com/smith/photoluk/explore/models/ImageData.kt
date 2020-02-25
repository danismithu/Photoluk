package com.smith.photoluk.explore.models

import com.orm.SugarRecord
import com.orm.dsl.Column
import com.orm.dsl.Table
import java.io.Serializable

@Table
class ImageData: SugarRecord(), Serializable {

    @Column(name = "url")
    var url: String? = null


}
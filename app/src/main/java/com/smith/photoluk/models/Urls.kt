package com.smith.photoluk.models

import com.orm.SugarRecord
import com.orm.dsl.Column
import com.orm.dsl.Table
import java.io.Serializable

@Table(name = "urls")
class Urls: SugarRecord(), Serializable {
    @Column(name = "full")
    var full: String? = null
}
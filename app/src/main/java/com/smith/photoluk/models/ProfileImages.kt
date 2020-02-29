package com.smith.photoluk.models

import com.orm.SugarRecord
import com.orm.dsl.Column
import com.orm.dsl.Table
import java.io.Serializable

@Table(name = "profilesImages")
class ProfileImages: SugarRecord(), Serializable {

    @Column(name = "medium")
    var medium: String? = null
}
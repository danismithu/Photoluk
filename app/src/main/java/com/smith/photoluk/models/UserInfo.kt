package com.smith.photoluk.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.orm.SugarRecord
import com.orm.dsl.Column
import com.orm.dsl.Ignore
import com.orm.dsl.Table
import java.io.Serializable

@Table(name = "userInfo")
class UserInfo: SugarRecord(), Serializable {

    @Column(name = "userName")
    var userName: String? = null

    @Column(name = "name")
    var name: String? = null

    @Ignore
    var profileImage: ProfileImages? = null
}
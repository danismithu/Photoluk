package com.smith.photoluk

import android.app.Application
import com.orm.SugarContext

class Photoluk: Application() {
    override fun onCreate() {
        super.onCreate()
        SugarContext.init(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        SugarContext.terminate()
    }
}
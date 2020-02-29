package com.smith.photoluk.detailImage

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.smith.photoluk.R
import com.smith.photoluk.detailImage.fragments.DetailImageFrag
import com.smith.photoluk.models.ImageData

class DetailImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.image_detail_activity)

        val imageData: ImageData? = intent.getSerializableExtra("imageData") as ImageData

        if (imageData == null) finish()

        val detailImageFrag = DetailImageFrag.newInstance(imageData!!)
        val ft = supportFragmentManager.beginTransaction()

        ft.add(R.id.image_detail_frag, detailImageFrag)
        ft.addToBackStack(null)
        ft.commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
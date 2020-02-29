package com.smith.photoluk.detailImage.fragments

import android.graphics.Point
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smith.photoluk.databinding.ImageDetailFragBinding
import com.smith.photoluk.models.ImageData
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class DetailImageFrag: Fragment() {

    private lateinit var imageDetailBinding: ImageDetailFragBinding
    private var imageData: ImageData? = null

    companion object {
        fun newInstance(imageData: ImageData): DetailImageFrag {
            val detailImageFrag = DetailImageFrag()
            val args = Bundle()

            args.putSerializable("imageData", imageData)
            detailImageFrag.arguments = args

            return detailImageFrag
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        imageDetailBinding = ImageDetailFragBinding.inflate(inflater, container, false)

        arguments!!.getSerializable("imageData").let {
            imageData = if (it == null) null else it as ImageData
        }

        if (imageData == null) {
            activity!!.finish()
        }

        imageDetailBinding.image = imageData
        initUser(imageData!!)

        return imageDetailBinding.root
    }

    private fun initUser(imageData: ImageData) {
        setUserImage(imageData.large!!, imageData.full!!)
    }

    private fun setUserImage(imgUser: String, url: String) {
        val display = activity!!.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)

        Picasso.get().load(imgUser).into(imageDetailBinding.userImg)

        Picasso.get().load(url).resize(size.x, size.y).centerCrop().into(imageDetailBinding.imgBkg, object: Callback {
            override fun onSuccess() {
                imageDetailBinding.progressLoading.visibility = View.GONE
            }

            override fun onError(e: Exception?) {
                //Aquí se debería de implementar imagen de broken link o similar
            }
        })
    }
}
package com.smith.photoluk.explore.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.smith.photoluk.databinding.ImageItemBinding
import com.smith.photoluk.explore.models.ImageData

class ImageFeedAdapter(/*private val imageList: List<ImageData>,*/
                       private val screenWidth: Int): RecyclerView.Adapter<ImageFeedHolder>() {

    private lateinit var imageItemBinding: ImageItemBinding
    private var imageList: List<ImageData> = ArrayList()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ImageFeedHolder {
        imageItemBinding = ImageItemBinding.inflate(LayoutInflater.from(p0.context), p0, false)
        val holder  = ImageFeedHolder(imageItemBinding, screenWidth)

        return holder
    }

    override fun onBindViewHolder(p0: ImageFeedHolder, p1: Int) {
        val image = imageList[p1]
        p0.setImageItem(image)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    fun setImageList(imageList: List<ImageData>) {
        this.imageList = imageList
    }
}
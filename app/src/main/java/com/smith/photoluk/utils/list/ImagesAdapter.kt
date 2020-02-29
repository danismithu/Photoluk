package com.smith.photoluk.utils.list

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.smith.photoluk.R
import com.smith.photoluk.databinding.ImageItemBinding
import com.smith.photoluk.models.ImageData
import com.smith.photoluk.utils.CustomClickListener
import java.lang.ref.WeakReference

class ImagesAdapter(private val screenWidth: Int, private val context: Context, private val isExploreFrag: Boolean,
                           private val detailListener: CustomClickListener): RecyclerView.Adapter<ImagesHolder>() {

        private lateinit var imageItemBinding: ImageItemBinding
        private var imageList: ArrayList<ImageData> = ArrayList()

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ImagesHolder {
            imageItemBinding = ImageItemBinding.inflate(LayoutInflater.from(p0.context), p0, false)
            val holder  = ImagesHolder(imageItemBinding, screenWidth)

            holder.imageItemBinding.favIcon.setOnClickListener {
                updateFav(holder.layoutPosition)
            }

            holder.imageItemBinding.imgFeed.setOnClickListener {
                val clickListener = WeakReference<CustomClickListener>(detailListener)
                clickListener.get()!!.onClick(holder.imageItemBinding.imgCardView, holder.layoutPosition)
            }

            return holder
        }

        override fun onBindViewHolder(p0: ImagesHolder, p1: Int) {
            val image = imageList[p1]
            p0.setImageItem(image)

            val isFav = imageList[p1].isFavorite
            drawFav(p0, isFav)
        }

        private fun drawFav(p0: ImagesHolder, isFavorite: Boolean) {
            p0.imageItemBinding.favIcon.background = if (isFavorite) ContextCompat.getDrawable(context, R.drawable.ic_favorite_24px) else
                ContextCompat.getDrawable(context, R.drawable.ic_favorite_border_24px)
        }

        private fun updateFav(position: Int) {
            val imageData: ImageData = imageList[position]

            imageData.isFavorite = !imageData.isFavorite

            if (imageData.isFavorite) {
                imageData.save()
                notifyItemChanged(position)
            } else {
                if (isExploreFrag) {
                    imageData.delete()
                    notifyItemChanged(position)
                } else {
                    imageList.remove(imageData)
                    imageData.delete()
                    notifyDataSetChanged()
                }
            }
        }

        fun setImageList(imageList: ArrayList<ImageData>) {
            this.imageList = imageList
        }

        fun getImageList(): ArrayList<ImageData> {
            return imageList
        }

        override fun getItemCount(): Int {
            return imageList.size
        }
    }

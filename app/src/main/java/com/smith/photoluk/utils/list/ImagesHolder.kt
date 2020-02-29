package com.smith.photoluk.utils.list

import android.support.v7.widget.RecyclerView
import android.view.View
import com.smith.photoluk.databinding.ImageItemBinding
import com.smith.photoluk.models.ImageData
import com.squareup.picasso.Callback
import java.lang.Exception
import kotlin.math.roundToInt

class ImagesHolder(val imageItemBinding: ImageItemBinding,
                          private val screenWidth: Int): RecyclerView.ViewHolder(imageItemBinding.root) {

        fun setImageItem(image: ImageData) {
            imageItemBinding.image = image

            //Obtener el porcentaje del ancho de la imagen relativamente a la mitad del ancho de la pantalla
            val imgXPerc: Double = screenWidth.toDouble() / image.width!!
            val newImgX = (image.width!! * imgXPerc).roundToInt()

            image.isLoading = true

            //Cargar la imagen obtenida del feed
            image.picassoImageRequest!!.resize(newImgX, 0).into(imageItemBinding.imgFeed, object: Callback {
                override fun onSuccess() {
                    imageItemBinding.progressLoading.visibility = View.GONE
                    image.isLoading = false
                }

                override fun onError(e: Exception?) {
                    //Aquí se debería de implementar imagen de broken link o similar
                }
            })

            //Cargar la imagen de perfil del usuario
            image.picassoProfileRequest!!.into(imageItemBinding.userImg)
        }
    }
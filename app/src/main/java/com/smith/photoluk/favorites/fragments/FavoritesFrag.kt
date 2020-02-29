package com.smith.photoluk.favorites.fragments

import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.smith.photoluk.databinding.FavoritesFragBinding
import com.smith.photoluk.detailImage.DetailImageActivity
import com.smith.photoluk.favorites.viewModels.FavoritesViewModel
import com.smith.photoluk.models.ImageData
import com.smith.photoluk.utils.CustomClickListener
import com.smith.photoluk.utils.list.ImagesAdapter

class FavoritesFrag : Fragment() {

    private lateinit var favoritesFragBinding: FavoritesFragBinding
    private lateinit var viewModel: FavoritesViewModel
    private var favoritesImageList: ArrayList<ImageData> = ArrayList()

    private lateinit var manager: StaggeredGridLayoutManager
    private lateinit var recyclerView: RecyclerView
    private var adapter: ImagesAdapter? = null

    private var width: Int = 0

    companion object {
        fun newInstance(): FavoritesFrag {
            return FavoritesFrag()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        favoritesFragBinding = FavoritesFragBinding.inflate(inflater, container, false)

        viewModel = FavoritesViewModel(this)
        viewModel.onViewAttached(this)

        viewModel.getFavoriteImages()

        val display = activity!!.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        width = (size.x) / 2

        manager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        manager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE

        recyclerView = favoritesFragBinding.favsList
        recyclerView.itemAnimator = DefaultItemAnimator()

        recyclerView.layoutManager = manager

        favoritesFragBinding.progressLoading.visibility = View.VISIBLE
        favoritesFragBinding.emptyTxt.visibility = View.GONE
        favoritesFragBinding.favsList.visibility = View.GONE

        getImages()

        return favoritesFragBinding.root
    }

    private fun getImages() {
        if (adapter != null) {
            viewModel.getFavoriteImages()
        } else {
            setAdapter()
        }
    }

    fun queryImageByUser(query: String) {
        if (adapter != null) {
            favoritesImageList = ArrayList()
            viewModel.getFavoriteQuery(query)
        } else {
            setAdapter()
        }
    }

    private fun setAdapter() {
        adapter = ImagesAdapter(width, context!!, false, object : CustomClickListener {
            override fun onClick(view: View, position: Int) { //Al momento de hacer click sobre la imagen
                launchImageDetail((favoritesFragBinding.favsList.adapter as ImagesAdapter).getImageList()[position])
            }
        })
        adapter!!.setImageList(favoritesImageList)
        recyclerView.adapter = adapter
    }

    private fun launchImageDetail(imageData: ImageData) {
        val intent = Intent(activity, DetailImageActivity::class.java)
        intent.putExtra("imageData", imageData)
        startActivity(intent)
    }

    fun populateFavorites(imageList: List<ImageData>) {
        favoritesFragBinding.progressLoading.visibility = View.GONE
        if (imageList.isEmpty()) {
            favoritesFragBinding.emptyTxt.visibility = View.VISIBLE
            favoritesFragBinding.favsList.visibility = View.GONE
        } else {
            favoritesFragBinding.emptyTxt.visibility = View.GONE
            favoritesFragBinding.favsList.visibility = View.VISIBLE
            favoritesImageList.addAll(imageList)
            adapter!!.setImageList(favoritesImageList)
            adapter!!.notifyDataSetChanged()
        }
    }

    fun showMessage(msg: String) {
        activity!!.runOnUiThread {
            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
        }
    }
}
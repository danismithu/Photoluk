package com.smith.photoluk.explore.fragments

import android.graphics.Point
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.smith.photoluk.databinding.ExploreActivityBinding
import com.smith.photoluk.explore.list.ImageFeedAdapter
import com.smith.photoluk.explore.models.ImageData
import com.smith.photoluk.explore.view_models.ExploreViewModel
import com.smith.photoluk.utils.models.EndlessRecyclerViewScrollListener
import kotlin.math.roundToInt

class ExploreFrag : Fragment() {

    private lateinit var exploreFragBinding: ExploreActivityBinding
    private lateinit var viewModel: ExploreViewModel
    private val globalImageList: ArrayList<ImageData> = ArrayList()

    private lateinit var manager: StaggeredGridLayoutManager
    private lateinit var recyclerView: RecyclerView
    private var width: Int = 0

    /****** ENDLESS ******/
    var page = 1
    var isLoading = false
    var limit = 10

    private var adapter: ImageFeedAdapter? = null


    private val numberList: ArrayList<String> = ArrayList()
    /*********************/

    companion object {
        fun newInstance() : ExploreFrag {
            return ExploreFrag()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        exploreFragBinding = ExploreActivityBinding.inflate(inflater, container, false)

        viewModel = ExploreViewModel(this)
        viewModel.onViewAttached(this)
        viewModel.getImagesFeed(1)

        val display = activity!!.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        width = (size.x) / 2

        manager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        manager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE

        recyclerView = exploreFragBinding.imageFeedList
        recyclerView.itemAnimator = DefaultItemAnimator()

        recyclerView.layoutManager = manager

        getPage()

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    val visibleItemCount = manager.childCount
                    val lastVisibleItemPosition: Int

                    val lastVisibleItemsPositions: IntArray = manager.findLastCompletelyVisibleItemPositions(null)
                    lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemsPositions)

                    val totalItem = adapter!!.itemCount

                    Log.d("SCROLLDATA", "totalItem $totalItem")
                    Log.d("SCROLLDATA", "lastVisibleItemPosition $lastVisibleItemPosition")
                    Log.d("SCROLLDATA", "visibleItemCount $visibleItemCount")

                    if (!viewModel.isSomethingLoading(globalImageList))  {
                        if ((lastVisibleItemPosition + 1) >= totalItem) {
                            page++
                            getPage()
                        }
                    }
                }
            }
        })
        return exploreFragBinding.root
    }

    private fun getLastVisibleItem(lastVisibleItems: IntArray): Int {
        var maxSize = 0

        for (i in 0 until lastVisibleItems.size) {
            if (i == 0) {
                maxSize = lastVisibleItems[i]
            } else if (lastVisibleItems[i] > maxSize) {
                maxSize = lastVisibleItems[i]
            }
        }
        return maxSize
    }

    private fun getPage() {
        if (adapter != null) {
            viewModel.getImagesFeed(page)
        } else {
            adapter = ImageFeedAdapter(width)
            adapter!!.setImageList(globalImageList)
            recyclerView.adapter = adapter
        }
    }

    fun populateFeed(imageList: List<ImageData>, lastPage: Int) {

        globalImageList.addAll(imageList)

        adapter!!.setImageList(globalImageList)
        adapter!!.notifyDataSetChanged()
    }


}
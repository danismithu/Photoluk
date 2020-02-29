package com.smith.photoluk.utils.models

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import com.smith.photoluk.models.ImageData
import com.smith.photoluk.explore.view_models.ExploreViewModel

abstract class EndlessRecyclerViewScrollListener(private val layourManager: StaggeredGridLayoutManager,
                                                 private val viewModel: ExploreViewModel,
                                                 private val imageList: List<ImageData>): RecyclerView.OnScrollListener() {

    private val visibleThreshold = 10

    private var currentPage = 1

    private var previousTotalItemCount = 0

    private val startingPage = 1

    private var recyclerView: RecyclerView? = null

    abstract fun onLoadMore(page: Int)

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

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

        this.recyclerView = recyclerView
        val lastVisibleItemPosition: Int
        val totalItemCount = layourManager.itemCount

        val lastVisibleItemsPositions: IntArray = layourManager.findLastCompletelyVisibleItemPositions(null)
        lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemsPositions)

        Log.d("SCROLLDATA", "loading ${isSomethingLoading()}")
        Log.d("SCROLLDATA", "totalItemCount $totalItemCount")
        Log.d("SCROLLDATA", "previousTotalItemCount $previousTotalItemCount")
        Log.d("SCROLLDATA", "lastVisibleItemPosition $lastVisibleItemPosition")
        Log.d("SCROLLDATA", "visibleThreshold $visibleThreshold")
        Log.d("SCROLLDATA", "itemCount ${recyclerView.adapter!!.itemCount}")

        if ((totalItemCount > previousTotalItemCount) && isSomethingLoading()) {
            previousTotalItemCount = totalItemCount
        }

        if ((lastVisibleItemPosition + 1) >= totalItemCount && !isSomethingLoading() /*&&
            (recyclerView.adapter!!.itemCount > visibleThreshold)*/) {

             currentPage++
            onLoadMore(currentPage)
        }
    }

    fun resetState() {
        currentPage = startingPage
        previousTotalItemCount = 0
    }

    private fun isSomethingLoading(): Boolean {
        return viewModel.isSomethingLoading(imageList)
    }
}
package com.smith.photoluk.explore.fragments

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
import com.smith.photoluk.R
import com.smith.photoluk.databinding.ExploreFragBinding
import com.smith.photoluk.detailImage.DetailImageActivity
import com.smith.photoluk.explore.view_models.ExploreViewModel
import com.smith.photoluk.models.ImageData
import com.smith.photoluk.utils.CustomClickListener
import com.smith.photoluk.utils.list.ImagesAdapter

class ExploreFrag : Fragment() {

    private lateinit var exploreFragBinding: ExploreFragBinding
    private lateinit var viewModel: ExploreViewModel
    private var feedImageList: ArrayList<ImageData> = ArrayList()

    private lateinit var manager: StaggeredGridLayoutManager
    private lateinit var recyclerView: RecyclerView
    private var adapter: ImagesAdapter? = null

    private var width: Int = 0
    private var isFiltered = false
    private var query = ""

    /****** ENDLESS ******/
    var page = 1
    /*********************/

    companion object {
        fun newInstance() : ExploreFrag {
            return ExploreFrag()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        exploreFragBinding = ExploreFragBinding.inflate(inflater, container, false)

        viewModel = ExploreViewModel(this, resources.getString(R.string.clientId))
        viewModel.onViewAttached(this)
        viewModel.getImagesFeed(1)

        val display = activity!!.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        width = (size.x) / 2

        isFiltered = false

        manager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        manager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE

        recyclerView = exploreFragBinding.imageFeedList
        recyclerView.itemAnimator = DefaultItemAnimator()

        recyclerView.layoutManager = manager

        exploreFragBinding.progressLoading.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE

        getPage()

        exploreFragBinding.refreshFeed.setOnRefreshListener {
            page = 1
            feedImageList = ArrayList()
            getPage()
            exploreFragBinding.refreshFeed.isRefreshing = false
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    val lastVisibleItemPosition: Int

                    val lastVisibleItemsPositions: IntArray = manager.findLastCompletelyVisibleItemPositions(null)
                    lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemsPositions)

                    val totalItem = adapter!!.itemCount

                    if (!viewModel.isSomethingLoading(feedImageList))  {
                        if ((lastVisibleItemPosition + 1) >= totalItem) {
                            page++

                            if (isFiltered) {
                                queryImages(query, false)
                            } else {
                                getPage()
                            }
                        }
                    }
                }
            }
        })
        return exploreFragBinding.root
    }

    private fun getLastVisibleItem(lastVisibleItems: IntArray): Int {
        var maxSize = 0

        for (i in lastVisibleItems.indices) {
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
            setAdapter()
        }
    }

    fun queryImages(query: String, hitSearch: Boolean) {
        this.query = query
        isFiltered = true
        if (adapter != null) {
            if (hitSearch) feedImageList = ArrayList()
            viewModel.queryImagesFeed(page, query)
        } else {
            setAdapter()
        }
    }

    private fun setAdapter() {
        adapter = ImagesAdapter(width, context!!, true, object : CustomClickListener {
            override fun onClick(view: View, position: Int) { //Al momento de hacer click sobre la imagen
                launchImageDetail((exploreFragBinding.imageFeedList.adapter as ImagesAdapter).getImageList()[position])
            }
        })
        adapter!!.setImageList(feedImageList)
        recyclerView.adapter = adapter
    }

    private fun launchImageDetail(imageData: ImageData) {
        val intent = Intent(activity, DetailImageActivity::class.java)
        intent.putExtra("imageData", imageData)
        startActivity(intent)
    }

    fun populateFeed(imageList: List<ImageData>) {
        exploreFragBinding.progressLoading.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE

        feedImageList.addAll(imageList)

        adapter!!.setImageList(feedImageList)
        adapter!!.notifyDataSetChanged()
    }

    fun showMessage(msg: String) {
        exploreFragBinding.progressLoading.visibility = View.GONE
        activity!!.runOnUiThread {
            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
        }
    }
}
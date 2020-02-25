package com.smith.photoluk.explore.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smith.photoluk.databinding.ExploreActivityBinding

class ExploreFrag : Fragment() {

    private lateinit var exploreFragBinding: ExploreActivityBinding

    companion object {
        fun newInstance() : ExploreFrag {
            return ExploreFrag()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        exploreFragBinding = ExploreActivityBinding.inflate(inflater, container, false)

        return exploreFragBinding.root
    }
}
package com.smith.photoluk

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import com.smith.photoluk.explore.fragments.ExploreFrag

class MainActivity : AppCompatActivity() {

    private lateinit var exploreFrag: ExploreFrag
    private lateinit var ft : FragmentTransaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        exploreFrag = ExploreFrag.newInstance()

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.setOnNavigationItemReselectedListener { item ->
            ft = supportFragmentManager.beginTransaction()
            when (item.itemId) {
                R.id.explore -> {
                    ft.replace(R.id.main_frag, exploreFrag)
                }
                R.id.favorites -> {

                }
            }
            ft.addToBackStack(null)
            ft.commit()
        }
    }

    override fun onStart() {
        super.onStart()
    }
}
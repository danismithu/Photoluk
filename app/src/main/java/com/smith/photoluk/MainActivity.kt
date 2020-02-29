package com.smith.photoluk

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.smith.photoluk.explore.fragments.ExploreFrag
import com.smith.photoluk.favorites.fragments.FavoritesFrag

class MainActivity : AppCompatActivity() {

    private var exploreFrag: ExploreFrag? = null
    private var favoritesFrag: FavoritesFrag? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        supportActionBar!!.setDisplayShowTitleEnabled(false)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                // Para futuro revisar estas implementaciones para que no se vuelva a crear el fragmento si no que se mantenga
                R.id.explore -> {
                    exploreFrag = ExploreFrag.newInstance()
                    openFragment(exploreFrag!!)
                }
                R.id.favorites -> {
                    favoritesFrag = FavoritesFrag.newInstance()
                    openFragment(favoritesFrag!!)
                }
            }
            true
        }

        bottomNavigation.selectedItemId = R.id.explore
    }

    private fun openFragment(frag: Fragment) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.main_frag, frag)
        ft.addToBackStack(null)
        ft.commit()
    }

    //Menú superior

}
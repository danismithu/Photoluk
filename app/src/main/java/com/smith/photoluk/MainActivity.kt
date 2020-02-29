package com.smith.photoluk

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.smith.photoluk.explore.fragments.ExploreFrag
import com.smith.photoluk.favorites.fragments.FavoritesFrag

class MainActivity : AppCompatActivity() {

    private var exploreFrag: ExploreFrag? = null
    private var favoritesFrag: FavoritesFrag? = null
    private var activeFrag: String? = null

    companion object {
        private const val EXPLORE = "EXPLORE"
        private const val FAVS = "FAVS"
    }

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
                    activeFrag = EXPLORE
                }
                R.id.favorites -> {
                    favoritesFrag = FavoritesFrag.newInstance()
                    openFragment(favoritesFrag!!)
                    activeFrag = FAVS
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.actionbar_menu, menu)
        val item: MenuItem = menu!!.findItem(R.id.filter)
        val searchView: SearchView = item.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                if (exploreFrag != null && p0 != null && activeFrag.equals(EXPLORE)) {
                    exploreFrag!!.queryImages(p0, true)
                } else if (favoritesFrag != null && p0 != null && activeFrag.equals(FAVS)) {
                    favoritesFrag!!.queryImageByUser(p0)
                } else {
                    Toast.makeText(this@MainActivity, "Ocurrió un error, por favor vuelva a intentarlo", Toast.LENGTH_SHORT).show()
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }
        })

        val searchManager: SearchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        return super.onCreateOptionsMenu(menu)
    }
}
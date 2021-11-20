package com.example.proyectomoviles.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.proyectomoviles.R
import com.example.proyectomoviles.ui.*

private val TAB_TITLES = arrayOf(
    R.string.artist_tab1,
    R.string.artist_tab2,
)

class ArtistSectionsPagerAdapter(fragment: Fragment, private val artistId: Int) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        // Return a NEW fragment instance in createFragment(int)
        val fragment = when(position){
            0 -> ArtistAlbumsFragment()
            1 -> ArtistPrizesFragment()
            else -> throw Exception("No deberia pasar aca")
        }

        fragment.arguments = Bundle().apply {
            // Our object is just an integer :-P
            putInt("artistId", artistId) // Enviar IdDelArtista
        }
        return fragment
    }
}
package com.example.proyectomoviles.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.proyectomoviles.R
import com.example.proyectomoviles.ui.*

private val TAB_TITLES = arrayOf(
    R.string.tab_text_1,
    R.string.tab_text_2
)

class SectionsPagerAdapter(fragment: Fragment, private val albumId: Int) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        // Return a NEW fragment instance in createFragment(int)
        val fragment = when(position){
            0 -> CommentAlbumFragment()
            1 -> TrackAlbumFragment()
            else -> throw Exception("No deberia pasar aca")
        }

        fragment.arguments = Bundle().apply {
            // Our object is just an integer :-P
            putInt("albumId", albumId) // Enviar IdDelAlbum
        }
        return fragment
    }
}
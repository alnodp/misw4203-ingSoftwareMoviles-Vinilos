package com.example.proyectomoviles.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.proyectomoviles.R
import com.example.proyectomoviles.ui.*

private val TAB_TITLES = arrayOf(
    R.string.collector_tab1,
    R.string.collector_tab2,
    R.string.collector_tab3
)

class CollectorSectionsPagerAdapter(fragment: Fragment, private val collectorId: Int) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        // Return a NEW fragment instance in createFragment(int)
        val fragment = when(position){
            0 -> CollectorAlbumsFragment()
            1 -> CollectorPerformersFragment()
            2 -> CollectorCommentsFragment()
            else -> throw Exception("No deberia pasar aca")
        }

        fragment.arguments = Bundle().apply {
            // Our object is just an integer :-P
            putInt("collectorId", collectorId) // Enviar collectorId
        }
        return fragment
    }
}
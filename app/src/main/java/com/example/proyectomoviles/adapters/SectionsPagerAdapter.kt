package com.example.proyectomoviles.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.proyectomoviles.R
import com.example.proyectomoviles.ui.*

private val TAB_TITLES = arrayOf(
    R.string.tab_text_1,
    R.string.tab_text_2
)

class SectionsPagerAdapter(private val context: FragmentActivity?, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {





    override fun getPageTitle(position: Int): CharSequence? {
        return context!!.resources.getString(TAB_TITLES[position])
    }



    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = CommentAlbumFragment()
            1 -> fragment = TrackAlbumFragment()
//            2 -> fragment = DashboardFragment()
        }
        return fragment!!
    }

    override fun getCount(): Int {
        // Show 3 total pages.
        return 2
    }
}
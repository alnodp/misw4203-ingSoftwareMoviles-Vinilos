package com.example.proyectomoviles.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.proyectomoviles.R
import com.example.proyectomoviles.adapters.SectionsPagerAdapter
import com.example.proyectomoviles.adapters.ComentariosAdapter
import com.example.proyectomoviles.adapters.PerformersAdapter
import com.example.proyectomoviles.adapters.TracksAdapter
import com.example.proyectomoviles.databinding.AlbumFragmentBinding
import com.example.proyectomoviles.models.Album
import com.google.android.material.tabs.TabLayout
import com.example.proyectomoviles.viewmodels.AlbumViewModel
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso

class AlbumFragment : Fragment() {
    private var _binding: AlbumFragmentBinding? = null
    private val binding get() = _binding!!
    private val args: AlbumFragmentArgs by navArgs()

    private lateinit var sectionsCollectionAdapter: SectionsPagerAdapter
    private lateinit var viewPager: ViewPager2

    private lateinit var viewModel: AlbumViewModel
    private var commentViewModelAdapter: ComentariosAdapter? = null
    private var performerViewModelAdapter: PerformersAdapter? = null
    private var trackViewModelAdapter: TracksAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AlbumFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        commentViewModelAdapter = ComentariosAdapter()
        performerViewModelAdapter = PerformersAdapter()
        trackViewModelAdapter = TracksAdapter()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sectionsCollectionAdapter = SectionsPagerAdapter(this, args.albumId)
        viewPager = binding.pager
        viewPager.adapter = sectionsCollectionAdapter

        val tabNames = arrayListOf<String>("Comentarios","Canciones")
        val tabLayout = binding.tabLayout
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabNames[position]
        }.attach()

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        viewModel = ViewModelProvider(this, AlbumViewModel.Factory(activity.application, args.albumId)).get(
            AlbumViewModel::class.java)

        viewModel.album.observe(viewLifecycleOwner,     Observer<Album> {

            it.apply {
                binding.album = this
                Picasso.get()
                    .load(it.cover)
                    .placeholder(R.drawable.ic_album)
                    .error(R.drawable.ic_artist)
                    .into(binding.adCover);
                /*
                    Cuando se actualice el album, quiero actualizar los viewpager o puede que ya lo haga porque escuchan del mismo LiveData
                 */
            }
        })

        viewModel.eventNetworkError.observe(viewLifecycleOwner, Observer<Boolean> { isNetworkError ->
            if (isNetworkError) onNetworkError()
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onNetworkError() {
        if(!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }
}
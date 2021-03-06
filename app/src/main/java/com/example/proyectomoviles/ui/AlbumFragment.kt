package com.example.proyectomoviles.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.proyectomoviles.R
import com.example.proyectomoviles.adapters.AlbumSectionsPagerAdapter
import com.example.proyectomoviles.databinding.AlbumFragmentBinding
import com.example.proyectomoviles.models.Album
import com.example.proyectomoviles.viewmodels.AlbumViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayoutMediator

class AlbumFragment : Fragment() {
    private var _binding: AlbumFragmentBinding? = null
    private val binding get() = _binding!!
    private val args: AlbumFragmentArgs by navArgs()

    private lateinit var sectionsCollectionAdapter: AlbumSectionsPagerAdapter
    private lateinit var viewPager: ViewPager2

    private lateinit var viewModel: AlbumViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AlbumFragmentBinding.inflate(inflater, container, false)
        val view = binding.root

        val button = view.findViewById<FloatingActionButton>(R.id.addAlbumTrackButton)
        button.setOnClickListener{
            val action = AlbumFragmentDirections.actionAlbumFragmentToNewAlbumTrackFragment(args.albumId)
            binding.root.findNavController().navigate(action)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sectionsCollectionAdapter = AlbumSectionsPagerAdapter(this, args.albumId)
        viewPager = binding.pager
        viewPager.adapter = sectionsCollectionAdapter

        val tabNames = arrayListOf<String>("Canciones", "Int??rpretes", "Comentarios")
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
                Glide.with(this@AlbumFragment)
                    .load(it.cover.toUri().buildUpon().scheme("https").build())
                    .apply(
                        RequestOptions().placeholder(R.drawable.ic_album)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .error(R.drawable.ic_artist)
                    ).into(binding.adCover)
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

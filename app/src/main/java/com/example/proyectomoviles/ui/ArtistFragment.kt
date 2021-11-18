package com.example.proyectomoviles.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.example.proyectomoviles.R
import com.example.proyectomoviles.adapters.ArtistSectionsPagerAdapter
import com.example.proyectomoviles.databinding.ArtistFragmentBinding
import com.example.proyectomoviles.models.Artist
import com.example.proyectomoviles.viewmodels.ArtistViewModel
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso

class ArtistFragment : Fragment() {
    private var _binding: ArtistFragmentBinding? = null
    private val binding get() = _binding!!
    private val args: ArtistFragmentArgs by navArgs()

    private lateinit var artistSectionsPagerAdapter: ArtistSectionsPagerAdapter
    private lateinit var viewPager: ViewPager2

    private lateinit var viewModel: ArtistViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ArtistFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        artistSectionsPagerAdapter = ArtistSectionsPagerAdapter(this, args.artistId)
        viewPager = binding.pager
        viewPager.adapter = artistSectionsPagerAdapter

        val tabNames = arrayListOf<String>("Álbumes", "Premios")
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
        viewModel = ViewModelProvider(this, ArtistViewModel.Factory(activity.application, args.artistId)).get(
            ArtistViewModel::class.java)

        viewModel.artist.observe(viewLifecycleOwner,     Observer<Artist> {

            it.apply {
                binding.artist = this
                Picasso.get()
                    .load(it.image)
                    .placeholder(R.drawable.ic_artist)
                    .error(R.drawable.ic_artist)
                    .into(binding.adCover);
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
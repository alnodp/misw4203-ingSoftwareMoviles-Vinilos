package com.example.proyectomoviles.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.databinding.DataBindingUtil
import com.example.proyectomoviles.R
import com.example.proyectomoviles.databinding.ListItemArtistsBinding
import com.example.proyectomoviles.viewmodels.ArtistsViewModel

class ArtistFragment2 : Fragment() {

    private lateinit var artistViewModel: ArtistsViewModel
    private var _binding: ListItemArtistsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = DataBindingUtil.setContentView(
            this, R.layout.fragment_artist)

        artistViewModel =
            ViewModelProvider(this, ArtistsViewModel.Factory(application)).get(ArtistsViewModel::class.java)

        artistViewModel.artist.observe(this, {
            binding.artist = it
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
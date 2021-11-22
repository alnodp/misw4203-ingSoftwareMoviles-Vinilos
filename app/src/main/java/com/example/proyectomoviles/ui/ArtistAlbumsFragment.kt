package com.example.proyectomoviles.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectomoviles.adapters.ArtistAlbumsAdapter
import com.example.proyectomoviles.databinding.AlbumsContentTabFragmentBinding
import com.example.proyectomoviles.models.Album
import com.example.proyectomoviles.viewmodels.ArtistAlbumsViewModel

class ArtistAlbumsFragment : Fragment() {
    private var viewModelAdapter: ArtistAlbumsAdapter? = null
    private lateinit var albumRecyclerView: RecyclerView
    private var _binding: AlbumsContentTabFragmentBinding? = null
    private val binding get() = _binding
    private lateinit var viewModel: ArtistAlbumsViewModel

    private var artistId : Int? = null
    private val ARTIST_ID_ARG = "artistId"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AlbumsContentTabFragmentBinding.inflate(inflater, container, false)
        val view = binding?.root
        viewModelAdapter = ArtistAlbumsAdapter()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        albumRecyclerView = binding!!.albumesRV
        albumRecyclerView.layoutManager = LinearLayoutManager(context)
        albumRecyclerView.adapter = viewModelAdapter

        arguments?.takeIf { it.containsKey(ARTIST_ID_ARG) }?.apply {
            artistId = getInt(ARTIST_ID_ARG)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (artistId == null)
            throw Exception("artistId no esta inicializado en ArtistAlbums")

        viewModel = ViewModelProvider(this, ArtistAlbumsViewModel.Factory(activity?.application!!,
            artistId!!))[ArtistAlbumsViewModel::class.java]

        viewModel.albumes.observe(viewLifecycleOwner, Observer<List<Album>> {
            it.apply {
                viewModelAdapter!!.albums = this
            }
        })

        viewModel.eventNetworkError.observe(viewLifecycleOwner, Observer<Boolean> { isNetworkError ->
            if (isNetworkError) onNetworkError()
        })
    }
    private fun onNetworkError() {
        if(!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }

}
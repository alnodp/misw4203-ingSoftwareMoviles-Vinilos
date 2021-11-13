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
import com.example.proyectomoviles.adapters.AlbumesAdapter
import com.example.proyectomoviles.adapters.CollectorAlbumsAdapter
import com.example.proyectomoviles.databinding.AlbumsContentTabFragmentBinding
import com.example.proyectomoviles.models.Album
import com.example.proyectomoviles.viewmodels.CollectorAlbumsViewModel

class CollectorAlbumsFragment : Fragment() {
    private var viewModelAdapter: CollectorAlbumsAdapter? = null
    private lateinit var albumRecyclerView: RecyclerView
    private var _binding: AlbumsContentTabFragmentBinding? = null
    private val binding get() = _binding
    private lateinit var viewModel: CollectorAlbumsViewModel

    private var collectorId : Int? = null
    private val COLLECTORID_ARG = "collectorId"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AlbumsContentTabFragmentBinding.inflate(inflater, container, false)
        val view = binding?.root
        viewModelAdapter = CollectorAlbumsAdapter()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        albumRecyclerView = binding!!.albumesRV
        albumRecyclerView.layoutManager = LinearLayoutManager(context)
        albumRecyclerView.adapter = viewModelAdapter

        arguments?.takeIf { it.containsKey(COLLECTORID_ARG) }?.apply {
            collectorId = getInt(COLLECTORID_ARG)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (collectorId == null)
            throw Exception("collectorId no esta inicializado en CollectorAlbums")

        viewModel = ViewModelProvider(this, CollectorAlbumsViewModel.Factory(activity?.application!!,
            collectorId!!))[CollectorAlbumsViewModel::class.java]

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
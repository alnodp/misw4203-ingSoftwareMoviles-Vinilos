package com.example.proyectomoviles.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectomoviles.R
import com.example.proyectomoviles.adapters.TracksAdapter
import com.example.proyectomoviles.databinding.TracksFragmentBinding
import com.example.proyectomoviles.viewmodels.AlbumTracksViewModel

class AlbumTracksFragment : Fragment() {
    private var viewModelAdapter: TracksAdapter? = null
    private lateinit var trackRecyclerView: RecyclerView
    private var _binding: TracksFragmentBinding? = null
    private val binding get() = _binding
    private lateinit var viewModel: AlbumTracksViewModel

    private var albumId : Int? = null
    private val ALBUMID_ARG = "albumId"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = TracksFragmentBinding.inflate(inflater, container, false)
        val view = binding?.root
        viewModelAdapter = TracksAdapter()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        trackRecyclerView = binding!!.tracksRV
        trackRecyclerView.layoutManager = LinearLayoutManager(context)
        trackRecyclerView.adapter = viewModelAdapter

        arguments?.takeIf { it.containsKey(ALBUMID_ARG) }?.apply {
            albumId = getInt(ALBUMID_ARG)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (albumId == null)
            throw Exception("albumId no esta inicializado en CommentAlbum")

        viewModel = ViewModelProvider(this, AlbumTracksViewModel.Factory(activity?.application!!, albumId!!)).get(
            AlbumTracksViewModel::class.java)

        viewModel.album.observe(viewLifecycleOwner, {
            it.apply {
                val values = this.tracks
                viewModelAdapter!!.tracks = values

                if(it.tracks.isNotEmpty()){
                    binding!!.tracksRV.visibility = View.VISIBLE
                }else{
                    binding!!.tracksRV.visibility = View.GONE
                }
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
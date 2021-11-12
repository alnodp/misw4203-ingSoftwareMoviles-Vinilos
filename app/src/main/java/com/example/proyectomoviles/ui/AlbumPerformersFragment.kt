package com.example.proyectomoviles.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectomoviles.adapters.PerformersAdapter
import com.example.proyectomoviles.databinding.PerformerAlbumFragmentBinding
import com.example.proyectomoviles.models.Album
import com.example.proyectomoviles.viewmodels.AlbumPerformersViewModel

class AlbumPerformersFragment : Fragment() {

    private var viewModelAdapter: PerformersAdapter? = null
    private lateinit var performerRecyclerView: RecyclerView
    private var _binding: PerformerAlbumFragmentBinding? = null
    private val binding get() = _binding
    private lateinit var viewModel: AlbumPerformersViewModel

    private var albumId : Int? = null
    private val ALBUMID_ARG = "albumId"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PerformerAlbumFragmentBinding.inflate(inflater, container, false)
        val view = binding?.root
        viewModelAdapter = PerformersAdapter()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        performerRecyclerView = binding!!.performersRV
        performerRecyclerView.layoutManager = LinearLayoutManager(context)
        performerRecyclerView.adapter = viewModelAdapter

        arguments?.takeIf { it.containsKey(ALBUMID_ARG) }?.apply {
            albumId = getInt(ALBUMID_ARG)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (albumId == null)
            throw Exception("albumId no esta inicializado en CommentAlbum")

        viewModel = ViewModelProvider(this, AlbumPerformersViewModel.Factory(activity?.application!!, albumId!!)).get(
            AlbumPerformersViewModel::class.java)

        viewModel.album.observe(viewLifecycleOwner, Observer<Album> {
            it.apply {
                binding!!.album = this

                val values = this.performers
                viewModelAdapter!!.performers = values

                if(it.performers.isNotEmpty()){
                    binding!!.performersRV.visibility = View.VISIBLE
                }else{
                    binding!!.performersRV.visibility = View.GONE
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
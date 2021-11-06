package com.example.proyectomoviles.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectomoviles.R
import com.example.proyectomoviles.adapters.ComentariosAdapter
import com.example.proyectomoviles.adapters.TracksAdapter
import com.example.proyectomoviles.databinding.CommentAlbumFragmentBinding
import com.example.proyectomoviles.databinding.TrackAlbumFragmentBinding
import com.example.proyectomoviles.models.Album
import com.example.proyectomoviles.viewmodels.CommentAlbumViewModel
import com.example.proyectomoviles.viewmodels.TrackAlbumViewModel
import com.squareup.picasso.Picasso

class TrackAlbumFragment : Fragment() {
    private var viewModelAdapter: TracksAdapter? = null
    private lateinit var trackRecyclerView: RecyclerView
    private var _binding: TrackAlbumFragmentBinding? = null
    private val binding get() = _binding
    private lateinit var viewModel: TrackAlbumViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = TrackAlbumFragmentBinding.inflate(inflater, container, false)
        val view = binding?.root
        viewModelAdapter = TracksAdapter()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        trackRecyclerView = binding!!.tracksRV
        trackRecyclerView.layoutManager = LinearLayoutManager(context)
        trackRecyclerView.adapter = viewModelAdapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, TrackAlbumViewModel.Factory(activity?.application!!, 100)).get(
            TrackAlbumViewModel::class.java)

        viewModel.album.observe(viewLifecycleOwner, Observer<Album> {

            Log.d("COMMENTS", it.comments.toString())
            Log.d("PERFORMERS", it.performers.toString())
            Log.d("TRACKS", it.tracks.toString())

            it.apply {
                binding!!.album = this
                Picasso.get()
                    .load(it.cover)
                    .placeholder(R.drawable.ic_album)
                    .error(R.drawable.ic_artist)



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
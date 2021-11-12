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
import com.example.proyectomoviles.R
import com.example.proyectomoviles.adapters.ComentariosAdapter
import com.example.proyectomoviles.databinding.CommentAlbumFragmentBinding
import com.example.proyectomoviles.models.Album
import com.example.proyectomoviles.viewmodels.AlbumCommentsViewModel
import com.squareup.picasso.Picasso

class AlbumCommentsFragment : Fragment() {
    private var viewModelAdapter: ComentariosAdapter? = null
    private lateinit var recyclerView: RecyclerView
    private var _binding: CommentAlbumFragmentBinding? = null
    private val binding get() = _binding
    private lateinit var viewModel: AlbumCommentsViewModel

    private var albumId : Int? = null
    private val ALBUMID_ARG = "albumId"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CommentAlbumFragmentBinding.inflate(inflater, container, false)
        val view = binding?.root
        viewModelAdapter = ComentariosAdapter()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding!!.comentariosRV
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = viewModelAdapter

        arguments?.takeIf { it.containsKey(ALBUMID_ARG) }?.apply {
            albumId = getInt(ALBUMID_ARG)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (albumId == null)
            throw Exception("albumId no esta inicializado en CommentAlbum")

        viewModel = ViewModelProvider(this, AlbumCommentsViewModel.Factory(activity?.application!!, albumId!!)).get(
            AlbumCommentsViewModel::class.java)

        viewModel.album.observe(viewLifecycleOwner, Observer<Album> {
            it.apply {
                binding!!.album = this
                Picasso.get()
                    .load(it.cover)
                    .placeholder(R.drawable.ic_album)
                    .error(R.drawable.ic_artist)
                val values = this.comments
                viewModelAdapter!!.comments = values

                if(it.comments.isNotEmpty()){
                    binding!!.comentariosRV.visibility = View.VISIBLE
                }else{
                    binding!!.comentariosRV.visibility = View.GONE
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
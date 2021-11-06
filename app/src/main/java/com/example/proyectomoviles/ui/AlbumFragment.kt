package com.example.proyectomoviles.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectomoviles.R
import com.example.proyectomoviles.adapters.AlbumesAdapter
import com.example.proyectomoviles.adapters.ComentariosAdapter
import com.example.proyectomoviles.databinding.AlbumFragmentBinding
import com.example.proyectomoviles.databinding.AlbumesFragmentBinding
import com.example.proyectomoviles.models.Album
import com.example.proyectomoviles.models.Comment
import com.example.proyectomoviles.viewmodels.AlbumViewModel
import com.example.proyectomoviles.viewmodels.AlbumesViewModel
import com.squareup.picasso.Picasso

class AlbumFragment : Fragment() {
    private var _binding: AlbumFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: AlbumViewModel
    private var viewModelAdapter: ComentariosAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AlbumFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModelAdapter = ComentariosAdapter()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.comentariosRV
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.adapter = viewModelAdapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        viewModel = ViewModelProvider(this, AlbumViewModel.Factory(activity.application, 100)).get(
            AlbumViewModel::class.java)


        viewModel.album.observe(viewLifecycleOwner, Observer<Album> {

            Log.d("COMMENTS", it.comments.toString())
            Log.d("PERFORMERS", it.performers.toString())
            Log.d("TRACKS", it.tracks.toString())

            it.apply {
                binding.album = this
                Picasso.get()
                    .load(it.cover)
                    .placeholder(R.drawable.ic_album)
                    .error(R.drawable.ic_artist)
                    .into(binding.adCover);


                val values = this.comments + this.comments + this.comments
                viewModelAdapter!!.comments = values

                if(it.comments.isNotEmpty()){
                    binding.comentariosRV.visibility = View.VISIBLE
                }else{
                    binding.comentariosRV.visibility = View.GONE
                }


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
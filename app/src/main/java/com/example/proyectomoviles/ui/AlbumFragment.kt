package com.example.proyectomoviles.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.proyectomoviles.R
import com.example.proyectomoviles.adapters.SectionsPagerAdapter
import com.example.proyectomoviles.adapters.ComentariosAdapter
import com.example.proyectomoviles.adapters.PerformersAdapter
import com.example.proyectomoviles.adapters.TracksAdapter
import com.example.proyectomoviles.databinding.AlbumFragmentBinding
import com.example.proyectomoviles.models.Album
import com.google.android.material.tabs.TabLayout
import com.example.proyectomoviles.viewmodels.AlbumViewModel
import com.squareup.picasso.Picasso

class AlbumFragment : Fragment() {
    private var _binding: AlbumFragmentBinding? = null
    private val binding get() = _binding!!

//    private lateinit var commentRecyclerView: RecyclerView
    private lateinit var performerRecyclerView: RecyclerView
    private lateinit var trackRecyclerView: RecyclerView

    private lateinit var viewModel: AlbumViewModel
    private var commentViewModelAdapter: ComentariosAdapter? = null
    private var performerViewModelAdapter: PerformersAdapter? = null
    private var trackViewModelAdapter: TracksAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AlbumFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        commentViewModelAdapter = ComentariosAdapter()
        performerViewModelAdapter = PerformersAdapter()
        trackViewModelAdapter = TracksAdapter()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        commentRecyclerView = binding.comentariosRV
//        commentRecyclerView.layoutManager = LinearLayoutManager(context)
//        commentRecyclerView.adapter = commentViewModelAdapter

        performerRecyclerView = binding.performersRV
        performerRecyclerView.layoutManager = LinearLayoutManager(context)
        performerRecyclerView.adapter = performerViewModelAdapter

        trackRecyclerView = binding.tracksRV
        trackRecyclerView.layoutManager = LinearLayoutManager(context)
        trackRecyclerView.adapter = trackViewModelAdapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        viewModel = ViewModelProvider(this, AlbumViewModel.Factory(activity.application, 4)).get(
            AlbumViewModel::class.java)

        viewModel.album.observe(viewLifecycleOwner,     Observer<Album> {

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


                commentViewModelAdapter!!.comments = this.comments
                performerViewModelAdapter!!.performers = this.performers
                trackViewModelAdapter!!.tracks = this.tracks

                if(it.comments.isNotEmpty()){
//                    binding.comentariosRV.visibility = View.VISIBLE
                    binding.tracksRV.visibility = View.VISIBLE
                }else{
//                    binding.comentariosRV.visibility = View.GONE
                    binding.tracksRV.visibility = View.GONE
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
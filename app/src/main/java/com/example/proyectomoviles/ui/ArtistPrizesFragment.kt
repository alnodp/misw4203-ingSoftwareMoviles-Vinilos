package com.example.proyectomoviles.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectomoviles.adapters.ArtistPrizesAdapter
import com.example.proyectomoviles.databinding.ArtistPrizesFragmentBinding
import com.example.proyectomoviles.models.Prize
import com.example.proyectomoviles.viewmodels.ArtistPrizesViewModel

class ArtistPrizesFragment : Fragment() {
    private var viewModelAdapter: ArtistPrizesAdapter? = null
    private lateinit var prizesRecyclerView: RecyclerView
    private var _binding: ArtistPrizesFragmentBinding? = null
    private val binding get() = _binding
    private lateinit var viewModel: ArtistPrizesViewModel

    private var artistId : Int? = null
    private val ARTIST_ID_ARG = "artistId"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ArtistPrizesFragmentBinding.inflate(inflater, container, false)
        val view = binding?.root
        viewModelAdapter = ArtistPrizesAdapter()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        prizesRecyclerView = binding!!.prizesRV
        prizesRecyclerView.layoutManager = LinearLayoutManager(context)
        prizesRecyclerView.adapter = viewModelAdapter

        arguments?.takeIf { it.containsKey(ARTIST_ID_ARG) }?.apply {
            artistId = getInt(ARTIST_ID_ARG)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (artistId == null)
            throw Exception("artistId no esta inicializado en ArtistPrizes")

        viewModel = ViewModelProvider(this, ArtistPrizesViewModel.Factory(activity?.application!!,
            artistId!!))[ArtistPrizesViewModel::class.java]

        viewModel.prizes.observe(viewLifecycleOwner, Observer<List<Prize>> {
            it.apply {
                viewModelAdapter!!.prizes = this
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
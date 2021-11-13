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
import com.example.proyectomoviles.adapters.PerformersAdapter
import com.example.proyectomoviles.databinding.PerformersFragmentBinding
import com.example.proyectomoviles.models.Collector
import com.example.proyectomoviles.viewmodels.CollectorPerformersViewModel

class CollectorPerformersFragment : Fragment() {
    private var viewModelAdapter: PerformersAdapter? = null
    private lateinit var performerRecyclerView: RecyclerView
    private var _binding: PerformersFragmentBinding? = null
    private val binding get() = _binding
    private lateinit var viewModel: CollectorPerformersViewModel

    private var collectorId : Int? = null
    private val COLLECTORID_ARG = "collectorId"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PerformersFragmentBinding.inflate(inflater, container, false)
        val view = binding?.root
        viewModelAdapter = PerformersAdapter()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        performerRecyclerView = binding!!.performersRV
        performerRecyclerView.layoutManager = LinearLayoutManager(context)
        performerRecyclerView.adapter = viewModelAdapter

        arguments?.takeIf { it.containsKey(COLLECTORID_ARG) }?.apply {
            collectorId = getInt(COLLECTORID_ARG)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (collectorId == null)
            throw Exception("collectorId no esta inicializado en CollectorPerformers")

        viewModel = ViewModelProvider(this, CollectorPerformersViewModel.Factory(activity?.application!!,
            collectorId!!))[CollectorPerformersViewModel::class.java]

        viewModel.collector.observe(viewLifecycleOwner, Observer<Collector> {
            it.apply {
                val values = this.favoritePerformers
                viewModelAdapter!!.performers = values

                if(it.favoritePerformers.isNotEmpty()){
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
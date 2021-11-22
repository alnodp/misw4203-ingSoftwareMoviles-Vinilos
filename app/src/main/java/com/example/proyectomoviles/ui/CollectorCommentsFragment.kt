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
import com.example.proyectomoviles.adapters.ComentariosAdapter
import com.example.proyectomoviles.databinding.CommentsFragmentBinding
import com.example.proyectomoviles.models.Collector
import com.example.proyectomoviles.viewmodels.CollectorCommentsViewModel

class CollectorCommentsFragment : Fragment() {
    private var viewModelAdapter: ComentariosAdapter? = null
    private lateinit var commentRecyclerView: RecyclerView
    private var _binding: CommentsFragmentBinding? = null
    private val binding get() = _binding
    private lateinit var viewModel: CollectorCommentsViewModel

    private var collectorId : Int? = null
    private val COLLECTORID_ARG = "collectorId"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CommentsFragmentBinding.inflate(inflater, container, false)
        val view = binding?.root
        viewModelAdapter = ComentariosAdapter()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        commentRecyclerView = binding!!.comentariosRV
        commentRecyclerView.layoutManager = LinearLayoutManager(context)
        commentRecyclerView.adapter = viewModelAdapter

        arguments?.takeIf { it.containsKey(COLLECTORID_ARG) }?.apply {
            collectorId = getInt(COLLECTORID_ARG)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (collectorId == null)
            throw Exception("collectorId no esta inicializado en CollectorComments")

        viewModel = ViewModelProvider(this, CollectorCommentsViewModel.Factory(activity?.application!!,
            collectorId!!))[CollectorCommentsViewModel::class.java]

        viewModel.collector.observe(viewLifecycleOwner, Observer<Collector> {
            it.apply {
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
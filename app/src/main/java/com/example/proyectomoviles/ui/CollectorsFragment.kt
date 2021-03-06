package com.example.proyectomoviles.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectomoviles.R
import com.example.proyectomoviles.databinding.CollectorsFragmentBinding
import com.example.proyectomoviles.viewmodels.CollectorsViewModel
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.proyectomoviles.adapters.CollectorsAdapter
import com.example.proyectomoviles.models.Collector

class CollectorsFragment : Fragment() {
    private var _binding: CollectorsFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: CollectorsViewModel
    private lateinit var etFilterCollector: EditText
    private var viewModelAdapter: CollectorsAdapter? = null
    companion object {
        fun newInstance() = CollectorsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CollectorsFragmentBinding.inflate(inflater, container, false)
        binding.isLoading = true

        val view = binding.root
        etFilterCollector = view.findViewById<EditText>(R.id.etCollectorSearch)

        viewModelAdapter = CollectorsAdapter()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.rvCollector
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.adapter = viewModelAdapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        viewModel = ViewModelProvider(this, CollectorsViewModel.Factory(activity.application)).get(
            CollectorsViewModel::class.java)
        viewModel.collectors.observe(viewLifecycleOwner, Observer<List<Collector>> {
            it.apply {
                if(it.isNotEmpty()) binding.isLoading = false
                viewModelAdapter!!.collector = this
            }
        })

        etFilterCollector.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                viewModelAdapter!!.filter.filter(charSequence)
            }
            override fun afterTextChanged(editable: Editable) {}
        })

        viewModel.eventNetworkError.observe(viewLifecycleOwner, Observer<Boolean> { isNetworkError ->
            if (isNetworkError) onNetworkError()
        })
    }

    override fun onResume() {
        viewModelAdapter!!.filter.filter("")
        etFilterCollector.setText("")
        super.onResume()
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
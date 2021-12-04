package com.example.proyectomoviles.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectomoviles.R
import com.example.proyectomoviles.adapters.AlbumesAdapter
import com.example.proyectomoviles.databinding.AlbumesFragmentBinding
import com.example.proyectomoviles.models.Album
import com.example.proyectomoviles.viewmodels.AlbumesViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AlbumesFragment : Fragment() {
    private var _binding: AlbumesFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: AlbumesViewModel
    private lateinit var filterAlbumET: EditText
    private var viewModelAdapter: AlbumesAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AlbumesFragmentBinding.inflate(inflater, container, false)
        binding.isLoading = true

        val view = binding.root
        filterAlbumET = view.findViewById<EditText>(R.id.AlbumSearchEt)
        viewModelAdapter = AlbumesAdapter()

        val button = view.findViewById<FloatingActionButton>(R.id.addAlbumButton)
        button.setOnClickListener{
            val action = AlbumesFragmentDirections.actionAlbumesFragmentToNewAlbumFragment()
            binding.root.findNavController().navigate(action)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.albumesRV
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.adapter = viewModelAdapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        viewModel = ViewModelProvider(this, AlbumesViewModel.Factory(activity.application))[AlbumesViewModel::class.java]
        viewModel.albumes.observe(viewLifecycleOwner, Observer<List<Album>> {
            it.apply {
                if(it.isNotEmpty()) binding.isLoading = false
                viewModelAdapter!!.albums = this
            }
        })

        filterAlbumET.addTextChangedListener(object: TextWatcher {
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
        filterAlbumET.setText("")
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
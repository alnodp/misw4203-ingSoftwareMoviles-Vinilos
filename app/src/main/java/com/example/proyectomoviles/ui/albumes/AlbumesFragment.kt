package com.example.proyectomoviles.ui.albumes

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.proyectomoviles.R

class AlbumesFragment : Fragment() {

    companion object {
        fun newInstance() = AlbumesFragment()
    }

    private lateinit var viewModel: AlbumesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.albumes_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AlbumesViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
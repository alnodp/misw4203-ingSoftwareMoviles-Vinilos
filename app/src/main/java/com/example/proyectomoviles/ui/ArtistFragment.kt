package com.example.proyectomoviles.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.proyectomoviles.databinding.FragmentArtistBinding
import com.example.proyectomoviles.viewmodels.ArtistViewModel

class ArtistFragment : Fragment() {

    private lateinit var artistViewModel: ArtistViewModel
    private var _binding: FragmentArtistBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        artistViewModel =
            ViewModelProvider(this).get(ArtistViewModel::class.java)

        _binding = FragmentArtistBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textArtist
//        artistViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
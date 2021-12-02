package com.example.proyectomoviles.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.proyectomoviles.R
import com.example.proyectomoviles.databinding.NewAlbumTrackFragmentBinding
import com.example.proyectomoviles.models.Album
import com.example.proyectomoviles.viewmodels.NewAlbumTrackViewModel

class NewAlbumTrackFragment : Fragment() {
    private var _binding: NewAlbumTrackFragmentBinding? = null
    private val binding get() = _binding!!
    private val args: AlbumFragmentArgs by navArgs()

    private lateinit var viewModel: NewAlbumTrackViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = NewAlbumTrackFragmentBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.nameInputEditText.doOnTextChanged { text, start, before, count ->
            if (text!!.length > 64){
                binding.nameTextInputLayout.error = "No puede exceder los 64 caractéres."
            }else{
                binding.nameTextInputLayout.error = null
            }
        }

        val timeFields = arrayOf(
            arrayOf(binding.minuteInputEditText, binding.minuteTextInputLayout),
            arrayOf(binding.secondInputEditText, binding.secondTextInputLayout))

        binding.minuteInputEditText.doOnTextChanged { text, start, before, count ->
            if (text.toString() == "") {
                binding.minuteTextInputLayout.error = null
            }else{
                if("^[0-9][0-9]?\$".toRegex().matches(text.toString())) {
                    val minutes: Int = text.toString().toInt()
                    if (minutes > 60){
                        binding.minuteTextInputLayout.error = "No puede ser un número mayor a 60."
                    }else{
                        binding.minuteTextInputLayout.error = null
                    }
                }else{
                    binding.minuteTextInputLayout.error = "No usar símbolos , + - . ó espacios en blanco."
                }
            }
        }

        binding.secondInputEditText.doOnTextChanged { text, start, before, count ->
            if (text.toString() == "") {
                binding.secondTextInputLayout.error = null
            }else{
                if("^[0-9][0-9]?\$".toRegex().matches(text.toString())) {
                    val minutes: Int = text.toString().toInt()
                    if (minutes > 60){
                        binding.secondTextInputLayout.error = "No puede ser un número mayor a 60."
                    }else{
                        binding.secondTextInputLayout.error = null
                    }
                }else{
                    binding.secondTextInputLayout.error = "No usar símbolos , + - . ó espacios en blanco."
                }
            }
        }
        
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        viewModel = ViewModelProvider(this, NewAlbumTrackViewModel.Factory(activity.application, args.albumId)).get(
            NewAlbumTrackViewModel::class.java)

        viewModel.album.observe(viewLifecycleOwner,     Observer<Album> {

            it.apply {
                binding.album = this
                Glide.with(this@NewAlbumTrackFragment)
                    .load(it.cover.toUri().buildUpon().scheme("https").build())
                    .apply(
                        RequestOptions().placeholder(R.drawable.ic_album)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .error(R.drawable.ic_artist)
                    ).into(binding.adCover)
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

package com.example.proyectomoviles.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.proyectomoviles.R
import com.example.proyectomoviles.databinding.NewAlbumTrackFragmentBinding
import com.example.proyectomoviles.models.Album
import com.example.proyectomoviles.models.Track
import com.example.proyectomoviles.viewmodels.NewAlbumTrackViewModel
import com.google.android.material.textfield.TextInputLayout

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
            validateName(text, binding.nameTextInputLayout)
        }

        binding.minuteInputEditText.doOnTextChanged { text, start, before, count ->
            validateMinutesOrSeconds(text, binding.minuteTextInputLayout)
        }

        binding.secondInputEditText.doOnTextChanged { text, start, before, count ->
            validateMinutesOrSeconds(text, binding.secondTextInputLayout)
        }

        binding.saveButton.setOnClickListener {
            val validateName = validateName(
                binding.nameInputEditText.text, binding.nameTextInputLayout
            )
            val minuteValidation = validateMinutesOrSeconds(
                binding.minuteInputEditText.text, binding.minuteTextInputLayout
            )
            val secondValidation = validateMinutesOrSeconds(
                binding.secondInputEditText.text, binding.secondTextInputLayout
            )
            if (validateName && minuteValidation && secondValidation) {
                addAlbumTrack()
            }
        }

        binding.cancelButton.setOnClickListener {
            navigateToAlbumDetail()
        }

        return view
    }

    private fun addAlbumTrack() {
        val track = Track(
            name = binding.nameInputEditText.text.toString().trim(),
            duration = binding.minuteInputEditText.text.toString().trim()+":"+binding.secondInputEditText.text.toString().trim()
        )
        if(viewModel.addNewAlbumTrack(track)){
            showMessage("La canción se registró correctamente.")
            navigateToAlbumDetail()
        }else{
            showMessage("Ocurrió un error en el registro de la canción.")
        }
    }

    private fun navigateToAlbumDetail() {
        val action = NewAlbumTrackFragmentDirections.actionNewAlbumTrackFragmentToAlbumFragment(args.albumId)
        binding.root.findNavController().navigate(action)
    }

    private fun showMessage(s: String) {
        Toast.makeText(activity, s, Toast.LENGTH_LONG).show()
    }

    private fun validateName(text: CharSequence?, layout: TextInputLayout): Boolean {
        return if (text!!.length > 64){
            layout.error = "No puede exceder los 64 caractéres."
            false
        }else{
            if (text.toString().trim().isEmpty()){
                layout.error = "No puede ser vacío."
                return false
            }else{
                layout.error = null
                true
            }
        }
    }

    private fun validateMinutesOrSeconds(text: CharSequence?, layout: TextInputLayout): Boolean {
        if (text.toString() == "") {
            layout.error = "No puede ser vacío."
            return false
        }else{
            return if("^[0-9][0-9]?\$".toRegex().matches(text.toString())) {
                val minutes: Int = text.toString().toInt()
                if (minutes > 60){
                    layout.error = "No puede ser un número mayor a 60."
                    false
                }else{
                    layout.error = null
                    true
                }
            }else{
                layout.error = "No usar símbolos , + - . ó espacios en blanco."
                false
            }
        }
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

package com.example.proyectomoviles.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.proyectomoviles.R
import com.example.proyectomoviles.databinding.NewAlbumFragmentBinding
import com.example.proyectomoviles.models.Album
import com.example.proyectomoviles.viewmodels.NewAlbumViewModel
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.time.Month
import java.util.*

class NewAlbumFragment : Fragment() {
    private var _binding: NewAlbumFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: NewAlbumViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = NewAlbumFragmentBinding.inflate(inflater, container, false)
        val view = binding.root

        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDate(calendar)
        }

        binding.releaseDateButton.setOnClickListener {
            DatePickerDialog(requireContext(), datePicker, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        val genres = resources.getStringArray(R.array.genres)
        val genreArrayAdapter = ArrayAdapter(requireContext(), R.layout.option_item, genres)
        binding.genreAutoCompleteTextView.setAdapter((genreArrayAdapter))

        val recordLabels = resources.getStringArray(R.array.record_labels)
        val recordLabelArrayAdapter = ArrayAdapter(requireContext(), R.layout.option_item, recordLabels)
        binding.recordLabelAutoCompleteTextView.setAdapter((recordLabelArrayAdapter))

        binding.nameInputEditText.doOnTextChanged { text, start, before, count ->
            validateText(text, binding.nameTextInputLayout, 128)
        }

        binding.descriptionInputEditText.doOnTextChanged { text, start, before, count ->
            validateText(text, binding.descriptionTextInputLayout, 256)
        }

        binding.coverInputEditText.doOnTextChanged { text, start, before, count ->
            validateCoverUrl(text, binding.coverTextInputLayout)
        }

        binding.releaseDateInputEditText.doOnTextChanged { text, start, before, count ->
            validateDropDown(text, binding.releaseDateTextInputLayout)
        }

        binding.genreAutoCompleteTextView.doOnTextChanged { text, start, before, count ->
            validateDropDown(text, binding.genreTextInputLayout)
        }

        binding.recordLabelAutoCompleteTextView.doOnTextChanged { text, start, before, count ->
            validateDropDown(text, binding.recordLabelTextInputLayout)
        }

        binding.coverInputEditText.doOnTextChanged { text, start, before, count ->
            validateCoverUrl(text, binding.coverTextInputLayout)
        }

        binding.saveButton.setOnClickListener {
            val nameValidation = validateText(
                binding.nameInputEditText.text, binding.nameTextInputLayout, 128
            )
            val descriptionValidation = validateText(
                binding.descriptionInputEditText.text, binding.descriptionTextInputLayout, 256
            )
            val releaseDateValidation = validateDropDown(
                binding.releaseDateInputEditText.text, binding.releaseDateTextInputLayout
            )
            val coverValidation = validateCoverUrl(
                binding.coverInputEditText.text, binding.coverTextInputLayout
            )
            val genreValidation = validateDropDown(
                binding.genreAutoCompleteTextView.text, binding.genreTextInputLayout
            )
            val recordLabelValidation = validateDropDown(
                binding.recordLabelAutoCompleteTextView.text, binding.recordLabelTextInputLayout
            )
            if (nameValidation && descriptionValidation && releaseDateValidation && coverValidation && genreValidation && recordLabelValidation) {
                addAlbumTrack()
            }
        }

        binding.cancelButton.setOnClickListener {
            navigateToAlbums()
        }

        return view
    }

    private fun updateDate(calendar: Calendar) {
        val formattedDate = "yyyy-MM-dd"
        val simpleDateFormat = SimpleDateFormat(formattedDate, Locale.UK)
        binding.releaseDateInputEditText.setText(simpleDateFormat.format(calendar.time))
    }

    private fun addAlbumTrack() {
        val album = Album(
            name = binding.nameInputEditText.text.toString().trim(),
            description = binding.descriptionInputEditText.text.toString().trim(),
            cover = binding.coverInputEditText.text.toString().trim(),
            genre = binding.genreAutoCompleteTextView.text.toString().trim(),
            releaseDate = binding.releaseDateInputEditText.text.toString().trim(),
            recordLabel = binding.recordLabelAutoCompleteTextView.text.toString().trim()
        )
        if(viewModel.addNewAlbum(album)){
            showMessage("El álbum se registró correctamente.")
            navigateToAlbums()
        }else{
            showMessage("Ocurrió un error en el registro del álbum.")
        }
    }

    private fun navigateToAlbums() {
        val action = NewAlbumFragmentDirections.actionNewAlbumFragmentToAlbumesFragment()
        binding.root.findNavController().navigate(action)
    }

    private fun showMessage(s: String) {
        Toast.makeText(activity, s, Toast.LENGTH_LONG).show()
    }

    private fun validateText(text: CharSequence?, layout: TextInputLayout, maxLength: Int): Boolean {
        return if (text!!.length > maxLength){
            layout.error = "No puede exceder los $maxLength caractéres."
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

    private fun validateDropDown(text: CharSequence?, layout: TextInputLayout): Boolean {
        return if (text.toString().trim().isEmpty()){
            layout.error = "No puede ser vacío."
            return false
        }else{
            layout.error = null
            true
        }
    }

    private fun validateCoverUrl(text: CharSequence?, layout: TextInputLayout): Boolean {
        return if (text.toString() == "") {
            layout.error = "No puede ser vacío."
            false
        }else{
            if(Patterns.WEB_URL.toRegex().matches(text.toString())){
                layout.error = null
                true
            }else{
                layout.error = "No tiene el formato de una url."
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
        viewModel = ViewModelProvider(this, NewAlbumViewModel.Factory(activity.application))[NewAlbumViewModel::class.java]

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

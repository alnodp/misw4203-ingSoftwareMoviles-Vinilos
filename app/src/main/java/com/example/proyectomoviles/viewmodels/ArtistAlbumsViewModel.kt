package com.example.proyectomoviles.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.proyectomoviles.models.Album
import com.example.proyectomoviles.models.ArtistRepository

class ArtistAlbumsViewModel (application: Application, artistId: Int) : AndroidViewModel(application){
    private val _albumes = MutableLiveData<List<Album>>()

    val albumes: LiveData<List<Album>>
        get() = _albumes

    val id:Int = artistId

    private var _eventNetworkError = MutableLiveData<Boolean>(false)

    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)

    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    init {
        getDataFromRepository()
    }

    private fun getDataFromRepository() {
        ArtistRepository.getInstance(getApplication()).getArtistAlbums(id, {
            val list = it

            _albumes.postValue(list)
            _eventNetworkError.value = false
            _isNetworkErrorShown.value = false
        },{
            _eventNetworkError.value = true
        })
    }

    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

    class Factory(val app: Application, val artistId: Int) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ArtistAlbumsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ArtistAlbumsViewModel(app, artistId) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
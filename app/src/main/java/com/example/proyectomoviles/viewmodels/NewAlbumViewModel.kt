package com.example.proyectomoviles.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.proyectomoviles.models.Album
import com.example.proyectomoviles.models.AlbumRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewAlbumViewModel(application: Application) : AndroidViewModel(application){
    var album = MutableLiveData<Album?>()

    private var _eventNetworkError = MutableLiveData<Boolean>(false)

    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)

    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    init {
        getDataFromRepository()
        album = MutableLiveData()
    }

    private fun getDataFromRepository() {}

    fun addNewAlbum(album: Album): Boolean {
        // Invalidate Synchronous the cache, lightweight process that just eliminate
        AlbumRepository.getInstance(getApplication()).resetCache()
        try {
            viewModelScope.launch (Dispatchers.Default){
                withContext(Dispatchers.IO){
                    AlbumRepository.getInstance(getApplication()).addAlbum(album)
                }
                _eventNetworkError.postValue(false)
                _isNetworkErrorShown.postValue(false)
            }
            return true
        }
        catch (e:Exception){
            _eventNetworkError.value = true
            return false
        }
    }

    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NewAlbumViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return NewAlbumViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
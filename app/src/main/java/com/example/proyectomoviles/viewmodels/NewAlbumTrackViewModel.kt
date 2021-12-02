package com.example.proyectomoviles.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.proyectomoviles.models.Album
import com.example.proyectomoviles.models.AlbumRepository
import com.example.proyectomoviles.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewAlbumTrackViewModel(application: Application, albumId: Int) : AndroidViewModel(application){
    private val _album = MutableLiveData<Album>()
    private val _track = MutableLiveData<Track>()

    val album: LiveData<Album>
        get() = _album

    val track: LiveData<Track>
        get() = _track

    val id:Int = albumId

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
        try {
            viewModelScope.launch (Dispatchers.Default){
                withContext(Dispatchers.IO){
                    val data = AlbumRepository.getInstance(getApplication()).getAlbum(id)
                    _album.postValue(data)
                }
                _eventNetworkError.postValue(false)
                _isNetworkErrorShown.postValue(false)
            }
        }
        catch (e:Exception){
            _eventNetworkError.value = true
        }
    }

    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

    class Factory(val app: Application, val albumId: Int) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NewAlbumTrackViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return NewAlbumTrackViewModel(app, albumId) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
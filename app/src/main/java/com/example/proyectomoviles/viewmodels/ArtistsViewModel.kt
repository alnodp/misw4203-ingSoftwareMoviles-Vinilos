package com.example.proyectomoviles.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.proyectomoviles.models.Album
import com.example.proyectomoviles.models.Artist

class ArtistsViewModel(application: Application) : AndroidViewModel(application){
    private val _artists = MutableLiveData<List<Artist>>()

    val artists: LiveData<List<Artist>>
        get() = _artists

    private var _eventNetworkError = MutableLiveData<Boolean>(false)

    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)

    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    init {
        refreshDataFromNetwork()
    }

    private fun refreshDataFromNetwork() {
        // TODO: Que use el repositorio para traer datos, y cambiar el nombre del metodo

        val tempList = mutableListOf<Artist>()
        val _artist = Artist(
            id = "1",
            name = "Metallica",
            image = "",
            description = "Metal band",
            creationDate = "1984-10-01"
        )

        tempList.add(_artist)
        tempList.add(_artist)
        tempList.add(_artist)

        _artists.postValue(
            tempList
        )

        /*
        NetworkServiceAdapter.getInstance(getApplication()).getAlbums({
            val list = it

            _albums.postValue(list)
            _eventNetworkError.value = false
            _isNetworkErrorShown.value = false
        },{
            _eventNetworkError.value = true
        })
         */
    }

    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ArtistsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ArtistsViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
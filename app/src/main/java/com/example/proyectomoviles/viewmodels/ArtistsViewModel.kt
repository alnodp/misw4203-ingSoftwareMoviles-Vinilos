package com.example.proyectomoviles.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.proyectomoviles.models.Artist
import com.example.proyectomoviles.models.ArtistRepository
import java.lang.Exception

class ArtistsViewModel(application: Application) : AndroidViewModel(application){

    private var artistRepository: ArtistRepository = ArtistRepository.getInstance(application)

    private val _artist = MutableLiveData<List<Artist>>()

    val artist: LiveData<List<Artist>>
        get() = _artist

    init {
        artistRepository.getArtist({
            _artist.value = it
        }, {
            throw Exception("Fallo el llamado getArtist al repo")
        })
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
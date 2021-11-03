package com.example.proyectomoviles.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.proyectomoviles.models.Album

class AlbumesViewModel(application: Application) : AndroidViewModel(application){
    private val _albumes = MutableLiveData<List<Album>>()

    val albumes: LiveData<List<Album>>
        get() = _albumes

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

        val tempList = mutableListOf<Album>()
        val _album = Album(cover="https://i.pinimg.com/564x/aa/5f/ed/aa5fed7fac61cc8f41d1e79db917a7cd.jpg",
            name = "Buscando América", id = 100, description = "Buscando América es el primer " +
                    "álbum de la banda de Rubén Blades y Seis del Solar lanzado en 1984.",
            genre = "Salsa", recordLabel = "Elektra", releaseDate = "1984-08-01T00:00:00.000Z")
        tempList.add(_album)
        tempList.add(_album)
        tempList.add(_album)

        _albumes.postValue(
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
            if (modelClass.isAssignableFrom(AlbumesViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AlbumesViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
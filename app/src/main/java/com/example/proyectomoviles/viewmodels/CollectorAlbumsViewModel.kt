package com.example.proyectomoviles.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.proyectomoviles.models.Album
import com.example.proyectomoviles.models.Collector
import com.example.proyectomoviles.models.CollectorRepository

class CollectorAlbumsViewModel (application: Application, collectorId: Int) : AndroidViewModel(application){
    private val _albumes = MutableLiveData<List<Album>>()

    val albumes: LiveData<List<Album>>
        get() = _albumes

    val id:Int = collectorId

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
        CollectorRepository.getInstance(getApplication()).getCollectorAlbums(id, {
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

    class Factory(val app: Application, val collectorId: Int) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CollectorAlbumsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CollectorAlbumsViewModel(app, collectorId) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
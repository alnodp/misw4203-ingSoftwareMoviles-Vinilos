package com.example.proyectomoviles.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.proyectomoviles.models.ArtistRepository
import com.example.proyectomoviles.models.Prize

class ArtistPrizesViewModel (application: Application, prizeId: Int) : AndroidViewModel(application){
    private val _prizes = MutableLiveData<List<Prize>>()

    val prizes: LiveData<List<Prize>>
        get() = _prizes

    val id:Int = prizeId

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
        ArtistRepository.getInstance(getApplication()).getArtistPrizes(id, {
            val list = it

            _prizes.postValue(list)
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
            if (modelClass.isAssignableFrom(ArtistPrizesViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ArtistPrizesViewModel(app, artistId) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
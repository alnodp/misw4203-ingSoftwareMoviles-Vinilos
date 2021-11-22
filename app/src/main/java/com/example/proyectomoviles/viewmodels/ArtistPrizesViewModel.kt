package com.example.proyectomoviles.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.proyectomoviles.models.ArtistRepository
import com.example.proyectomoviles.models.Prize
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        try {
            viewModelScope.launch (Dispatchers.Default){
                withContext(Dispatchers.IO){
                    val data = ArtistRepository.getInstance(getApplication()).getArtistPrizes(id)
                    val list2 = mutableListOf<Prize>()

                    for (prize in data) {
                        val prizeDetail = ArtistRepository.getInstance(getApplication()).getPrize(prize.id)
                        prize.name = prizeDetail.name
                        prize.description = prizeDetail.description
                        prize.organization = prizeDetail.organization

                        list2.add(prize)
                        _prizes.postValue(list2)
                    }
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
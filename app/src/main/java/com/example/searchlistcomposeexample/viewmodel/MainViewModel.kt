package com.example.searchlistcomposeexample.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.searchlistcomposeexample.viewmodel.model.ListData
import com.example.testapp.model.Resource
import com.example.searchlistcomposeexample.repository.MainRepository
import com.example.searchlistcomposeexample.viewmodel.base.BaseViewModel
import kotlinx.coroutines.*

class MainViewModel(
    private val mainRepository: MainRepository
) : BaseViewModel() {

    val listData = MutableLiveData<Resource<MutableList<ListData>?>>()

    fun getData() {
        viewModelScope.async(Dispatchers.IO) {
            try {
                val result = mainRepository.getData() //suspend
                if (result != null) {
                    listData.postValue(Resource.success(result))
                }
            }catch (e: Exception) {
                listData.postValue(Resource.networkFailed(throwable = e.cause))
            }
        }
    }


}
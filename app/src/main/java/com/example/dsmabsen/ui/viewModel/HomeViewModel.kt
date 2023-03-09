package com.example.dsmabsen.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dsmabsen.model.HomeResponse
import com.example.dsmabsen.repository.HomeRepository
import com.example.dsmabsen.repository.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import xyz.teamgravity.checkinternet.CheckInternet
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: HomeRepository) : ViewModel() {

    private val _homeLiveData = MutableLiveData<NetworkResult<HomeResponse>>()
    val homeLiveData: LiveData<NetworkResult<HomeResponse>>
        get() = _homeLiveData

    fun homeRequest(nip :String) {
        viewModelScope.launch {
            val connected = CheckInternet().check()
            if (connected) {
                _homeLiveData.postValue(NetworkResult.Loading())
                _homeLiveData.postValue(repository.homeUser(nip))
            } else
                _homeLiveData.postValue(NetworkResult.Error("No Internet Connection"))
        }

    }
}
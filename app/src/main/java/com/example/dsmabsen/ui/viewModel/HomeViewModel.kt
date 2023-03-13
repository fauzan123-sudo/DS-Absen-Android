package com.example.dsmabsen.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dsmabsen.model.HomeResponse
import com.example.dsmabsen.model.PresensiHariIni
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

    private val _getAbsen = MutableLiveData<NetworkResult<PresensiHariIni>>()
    val getAbsenLiveData: LiveData<NetworkResult<PresensiHariIni>>
        get() = _getAbsen

    fun homeRequest(nip: String) {
        viewModelScope.launch {
            _homeLiveData.postValue(NetworkResult.Loading())
            _homeLiveData.postValue(repository.homeUser(nip))
        }
    }

        fun getAbsenRequest(nip: String) {
            viewModelScope.launch {
                _getAbsen.postValue(NetworkResult.Loading())
                _getAbsen.postValue(repository.getAbsen(nip))
            }

        }
    }
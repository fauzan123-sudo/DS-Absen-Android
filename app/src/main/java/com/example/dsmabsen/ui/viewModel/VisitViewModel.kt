package com.example.dsmabsen.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dsmabsen.model.SpinnerVisit
import com.example.dsmabsen.model.VisitResponse
import com.example.dsmabsen.repository.NetworkResult
import com.example.dsmabsen.repository.VisitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import xyz.teamgravity.checkinternet.CheckInternet
import javax.inject.Inject

@HiltViewModel
class VisitViewModel @Inject constructor(private val repository: VisitRepository) :
    ViewModel() {
    private var _isDataLoaded = false

    private val _visit = MutableLiveData<NetworkResult<VisitResponse>>()
    val visitLiveData: LiveData<NetworkResult<VisitResponse>>
        get() = _visit

    private val _spinnerVisit = MutableLiveData<NetworkResult<SpinnerVisit>>()
    val spinnerVisitLiveData: LiveData<NetworkResult<SpinnerVisit>>
        get() = _spinnerVisit

    fun visitRequest(nip: String) {
        viewModelScope.launch {
            val connected = CheckInternet().check()
            if (connected) {
                _visit.value = NetworkResult.Loading()
                _visit.value = repository.getVisit(nip)
            } else {
                _visit.value = NetworkResult.Error("Tidak ada koneksi internet")
            }
        }
    }

    fun spinnerVisitRequest() {
        viewModelScope.launch {
            val connected = CheckInternet().check()
            if (connected) {
                _spinnerVisit.value = NetworkResult.Loading()
                _spinnerVisit.value = repository.spinnerVisit()
            } else {
                _spinnerVisit.value = NetworkResult.Error("Tidak ada koneksi internet")
            }
        }
    }
}
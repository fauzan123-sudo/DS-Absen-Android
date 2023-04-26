package com.infinity.dsmabsen.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinity.dsmabsen.model.ResponsePengajuanIzin
import com.infinity.dsmabsen.model.SpinnerVisit
import com.infinity.dsmabsen.model.VisitResponse
import com.infinity.dsmabsen.repository.NetworkResult
import com.infinity.dsmabsen.repository.VisitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    private val _sendDataVisit = MutableLiveData<NetworkResult<ResponsePengajuanIzin>>()
    val sendDataVisitLiveData: LiveData<NetworkResult<ResponsePengajuanIzin>>
        get() = _sendDataVisit


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

    fun sendDataVisitRequest(
        nip: RequestBody,
        kode_visit: RequestBody,
        kordinat: RequestBody,
        image: MultipartBody.Part
    ) {
        viewModelScope.launch {
            val connected = CheckInternet().check()
            if (connected) {
                _sendDataVisit.value = NetworkResult.Loading()
                _sendDataVisit.value = repository.sendDataVisit(nip, kode_visit, kordinat, image)
            } else {
                _sendDataVisit.value = NetworkResult.Error("Tidak ada koneksi internet")
            }
        }
    }
}
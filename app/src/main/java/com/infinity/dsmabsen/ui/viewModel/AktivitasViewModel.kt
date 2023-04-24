package com.infinity.dsmabsen.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinity.dsmabsen.model.*
import com.infinity.dsmabsen.repository.AktivitasRepository
import com.infinity.dsmabsen.repository.AttendanceRepository
import com.infinity.dsmabsen.repository.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import xyz.teamgravity.checkinternet.CheckInternet
import javax.inject.Inject

@HiltViewModel
class AktivitasViewModel @Inject constructor(private val repository: AktivitasRepository) :
    ViewModel() {
    private var _isDataLoaded = false

    private val _aktivitas = MutableLiveData<NetworkResult<AktivitasResponse>>()
    val aktivitasLiveData: LiveData<NetworkResult<AktivitasResponse>>
        get() = _aktivitas

    private val _sendAktivitas = MutableLiveData<NetworkResult<SendAktivitasResponse>>()
    val sendAktivitasLiveData: LiveData<NetworkResult<SendAktivitasResponse>>
        get() = _sendAktivitas


    fun aktivitasRequest(
        nip: String,
    ) {
        viewModelScope.launch {
            if (!_isDataLoaded) {
                val connected = CheckInternet().check()
                if (connected) {
                    _aktivitas.value = NetworkResult.Loading()
                    _aktivitas.value = repository.listAktivitas(nip)
                } else {
                    _aktivitas.value = NetworkResult.Error("Tidak ada koneksi internet")
                }
            }
        }
    }

    fun sendAktivitasRequest(
        nip: RequestBody,
        nama: RequestBody,
        koordinat: RequestBody,
        file: MultipartBody.Part
    ) {
        viewModelScope.launch {
            if (!_isDataLoaded) {
                val connected = CheckInternet().check()
                if (connected) {
                    _sendAktivitas.value = NetworkResult.Loading()
                    _sendAktivitas.value = repository.sendAktivitas(nip,nama,koordinat, file)
                } else {
                    _sendAktivitas.value = NetworkResult.Error("Tidak ada koneksi internet")
                }
            }
        }
    }


}
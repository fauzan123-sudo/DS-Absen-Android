package com.example.dsmabsen.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dsmabsen.model.*
import com.example.dsmabsen.repository.AttendanceRepository
import com.example.dsmabsen.repository.DataPerizinanRepository
import com.example.dsmabsen.repository.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import xyz.teamgravity.checkinternet.CheckInternet
import javax.inject.Inject

@HiltViewModel
class PerizinanViewModel @Inject constructor(private val repository: DataPerizinanRepository) :
    ViewModel() {
    private var _isDataLoaded = false

    private val _perizinan = MutableLiveData<NetworkResult<Perizinan>>()
    val perizinanLiveData: LiveData<NetworkResult<Perizinan>> = _perizinan

    private val _getSpinner = MutableLiveData<NetworkResult<SpinnerIzin>>()
    val getSpinnerLiveData: LiveData<NetworkResult<SpinnerIzin>> = _getSpinner

    private val _sendPermission = MutableLiveData<NetworkResult<ResponsePengajuanIzin>>()
    val sendPermissionLiveData: LiveData<NetworkResult<ResponsePengajuanIzin>> = _sendPermission


    fun requestPerizinan(nip: String) {
        viewModelScope.launch {
            _perizinan.postValue(NetworkResult.Loading())
            _perizinan.postValue(repository.dataPerizinan(nip))
        }
    }

    fun requestgetSpinner() {
        viewModelScope.launch {
            _getSpinner.postValue(NetworkResult.Loading())
            _getSpinner.postValue(repository.getSpinner())
        }
    }

    fun requestSendPermission(
        nip: RequestBody,
        kode_cuti: RequestBody,
        tanggal_mulai: RequestBody,
        tanggal_selesai: RequestBody,
        file: MultipartBody.Part,
        keterangan: RequestBody
    ) {
        viewModelScope.launch {
            _sendPermission.postValue(NetworkResult.Loading())
            _sendPermission.postValue(
                repository.sendPermission(
                    nip,
                    kode_cuti,
                    tanggal_mulai,
                    tanggal_selesai,
                    file,
                    keterangan
                )
            )
        }
    }
}
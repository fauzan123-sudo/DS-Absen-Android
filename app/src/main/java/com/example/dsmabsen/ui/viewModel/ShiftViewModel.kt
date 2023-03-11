package com.example.dsmabsen.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dsmabsen.model.*
import com.example.dsmabsen.repository.AttendanceRepository
import com.example.dsmabsen.repository.NetworkResult
import com.example.dsmabsen.repository.ReimbursementRepository
import com.example.dsmabsen.repository.ShiftRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import xyz.teamgravity.checkinternet.CheckInternet
import javax.inject.Inject

@HiltViewModel
class ShiftViewModel @Inject constructor(private val repository: ShiftRepository) :
    ViewModel() {

    private val _getShift = MutableLiveData<NetworkResult<Shift>>()
    val getShiftLiveData: LiveData<NetworkResult<Shift>> = _getShift

    private val _spinnerShift = MutableLiveData<NetworkResult<Shift>>()
    val spinnerShiftLiveData: LiveData<NetworkResult<Shift>> = _spinnerShift

    private val _getPengajuanShift = MutableLiveData<NetworkResult<SpinnerShift>>()
    val pengajuanShiftLiveData: LiveData<NetworkResult<SpinnerShift>> = _getPengajuanShift

    private var _isDataLoaded = false
    fun requestShift(nip: String) {
        viewModelScope.launch {
            val connected = CheckInternet().check()
            if (connected) {
                _getShift.postValue(NetworkResult.Loading())
                _getShift.postValue(repository.getListShift(nip))

            } else
                _getShift.postValue(NetworkResult.Error("No Internet Connection"))
        }
    }

    fun spinnerShift() {
        viewModelScope.launch {
            val connected = CheckInternet().check()
            if (connected) {
                _spinnerShift.postValue(NetworkResult.Loading())
//                _spinnerShift.postValue(repository.spinnerShift())

            } else
                _spinnerShift.postValue(NetworkResult.Error("No Internet Connection"))
        }
    }

    fun requestShiftPengajuan(
        nip: String,
        kode_cuti: String,
        file: String,
        keterangan: String
    ) {
//        viewModelScope.launch {
//            val connected = CheckInternet().check()
//            if (connected) {
//                _getPengajuanShift.postValue(NetworkResult.Loading())
//                _getPengajuanShift.postValue(
//                    repository.pengajuanShift(
//                        nip,
//                        kode_cuti,
//                        file,
//                        keterangan
//                    )
//                )
//
//            } else
//                _getPengajuanShift.postValue(NetworkResult.Error("No Internet Connection"))
//        }
    }
}
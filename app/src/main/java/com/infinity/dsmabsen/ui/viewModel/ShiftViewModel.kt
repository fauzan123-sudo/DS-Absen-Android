package com.infinity.dsmabsen.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinity.dsmabsen.model.PengajuanShift
import com.infinity.dsmabsen.model.Shift
import com.infinity.dsmabsen.model.SpinnerShift
import com.infinity.dsmabsen.repository.NetworkResult
import com.infinity.dsmabsen.repository.ShiftRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShiftViewModel @Inject constructor(private val repository: ShiftRepository) :
    ViewModel() {

    private val _getShift = MutableLiveData<NetworkResult<Shift>>()
    val getShiftLiveData: LiveData<NetworkResult<Shift>> = _getShift

    private val _spinnerShift = MutableLiveData<NetworkResult<SpinnerShift>>()
    val spinnerShiftLiveData: LiveData<NetworkResult<SpinnerShift>> = _spinnerShift

    private val _getPengajuanShift = MutableLiveData<NetworkResult<PengajuanShift>>()
    val pengajuanShiftLiveData: LiveData<NetworkResult<PengajuanShift>> = _getPengajuanShift

    private var _isDataLoaded = false
    fun requestShift(nip: String) {
        viewModelScope.launch {
            _getShift.postValue(NetworkResult.Loading())
            _getShift.postValue(repository.getListShift(nip))
        }
    }

    fun requestSpinnerShift() {
        viewModelScope.launch {
            _spinnerShift.postValue(NetworkResult.Loading())
            _spinnerShift.postValue(repository.spinnerShift())
        }
    }

    fun requestShiftPengajuan(
        nip: String,
        kode_cuti: String,
        file: String,
        keterangan: String
    ) {
        viewModelScope.launch {
            _getPengajuanShift.postValue(NetworkResult.Loading())
            _getPengajuanShift.postValue(
                repository.pengajuanShift(
                    nip,
                    kode_cuti,
                    file,
                    keterangan
                )
            )
        }
    }
}
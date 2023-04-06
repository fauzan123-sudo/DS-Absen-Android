package com.infinity.dsmabsen.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinity.dsmabsen.model.ListReimbursement
import com.infinity.dsmabsen.model.PengajuanReimbusement
import com.infinity.dsmabsen.model.SpinnerReimbursement
import com.infinity.dsmabsen.repository.NetworkResult
import com.infinity.dsmabsen.repository.ReimbursementRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import xyz.teamgravity.checkinternet.CheckInternet
import javax.inject.Inject

@HiltViewModel
class ReimbursementViewModel @Inject constructor(private val repository: ReimbursementRepository) :
    ViewModel() {

    private val _getReimbursement = MutableLiveData<NetworkResult<ListReimbursement>>()
    val getReimbursementLiveData: LiveData<NetworkResult<ListReimbursement>>
    get() = _getReimbursement

    private val _getSpinnerReimbursement = MutableLiveData<NetworkResult<SpinnerReimbursement>>()
    val getSpinnerReimbursementLiveData: LiveData<NetworkResult<SpinnerReimbursement>>
        get() = _getSpinnerReimbursement

    private val _getPengajuanReimbursement = MutableLiveData<NetworkResult<PengajuanReimbusement>>()
    val getPengajuanLiveData: LiveData<NetworkResult<PengajuanReimbusement>> =
        _getPengajuanReimbursement

    private var _isDataLoaded = false
    fun requestReimbursement(nip: String) {
        viewModelScope.launch {
            val connected = CheckInternet().check()
            if (connected) {
            _getReimbursement.value = NetworkResult.Loading()
            _getReimbursement.value = repository.getListReimbursement(nip)
            } else {
                _getReimbursement.value = NetworkResult.Error("Tidak ada koneksi internet")
            }
//            _getReimbursement.value = NetworkResult.Loading()
//            _getReimbursement.value = repository.getListReimbursement(nip)
//            _getReimbursement.postValue(NetworkResult.Loading())
//            _getReimbursement.postValue(repository.getListReimbursement(nip))
        }
    }

    fun requestSpinnerReimbursement() {
        viewModelScope.launch {
            _getSpinnerReimbursement.postValue(NetworkResult.Loading())
            _getSpinnerReimbursement.postValue(repository.spinnerReimbursement())
        }
    }

    fun requestPengajuanReimbursement(
        nip: RequestBody,
        kode_reimbursement: RequestBody,
        nilai: RequestBody,
        file: MultipartBody.Part,
        keterangan: RequestBody
    ) {
        viewModelScope.launch {
            _getPengajuanReimbursement.postValue(NetworkResult.Loading())
            _getPengajuanReimbursement.postValue(
                repository.pengajuanReimbursement(
                    nip,
                    kode_reimbursement,
                    nilai,
                    file,
                    keterangan
                )
            )
        }
    }
}
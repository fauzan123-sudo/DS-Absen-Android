package com.example.dsmabsen.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dsmabsen.model.AttendanceHistory
import com.example.dsmabsen.model.ListReimbursement
import com.example.dsmabsen.model.TotalAttendance
import com.example.dsmabsen.repository.AttendanceRepository
import com.example.dsmabsen.repository.NetworkResult
import com.example.dsmabsen.repository.ReimbursementRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import xyz.teamgravity.checkinternet.CheckInternet
import javax.inject.Inject

@HiltViewModel
class ReimbursementViewModel @Inject constructor(private val repository: ReimbursementRepository) :
    ViewModel() {

    private val _getReimbursement = MutableLiveData<NetworkResult<ListReimbursement>>()
    val getReimbursementLiveData: LiveData<NetworkResult<ListReimbursement>> = _getReimbursement

    private var _isDataLoaded = false
    fun requestReimbursement(nip: String) {
        viewModelScope.launch {
            val connected = CheckInternet().check()
            if (connected) {
                _getReimbursement.postValue(NetworkResult.Loading())
                _getReimbursement.postValue(repository.getListReimbursement(nip))

            } else
                _getReimbursement.postValue(NetworkResult.Error("No Internet Connection"))
        }
    }
}
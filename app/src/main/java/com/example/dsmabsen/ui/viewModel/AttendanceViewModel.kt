package com.example.dsmabsen.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dsmabsen.model.AttendanceHistory
import com.example.dsmabsen.model.TotalAttendance
import com.example.dsmabsen.repository.AttendanceRepository
import com.example.dsmabsen.repository.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import xyz.teamgravity.checkinternet.CheckInternet
import javax.inject.Inject

@HiltViewModel
class AttendanceViewModel @Inject constructor(private val repository: AttendanceRepository) :
    ViewModel() {

    private val _totalAttendance = MutableLiveData<NetworkResult<TotalAttendance>>()
    val totalAttendanceLiveData: LiveData<NetworkResult<TotalAttendance>> = _totalAttendance

    private val _attendanceHistory = MutableLiveData<NetworkResult<AttendanceHistory>>()
    val attendanceHistoryLiveData: LiveData<NetworkResult<AttendanceHistory>> = _attendanceHistory
    private var _isDataLoaded = false
    fun attendanceTotalRequest(nip: String) {
        viewModelScope.launch {
            val connected = CheckInternet().check()
            if (connected) {
                _totalAttendance.postValue(NetworkResult.Loading())
                _totalAttendance.postValue(repository.attendanceTotal(nip))

            } else
                _totalAttendance.postValue(NetworkResult.Error("No Internet Connection"))
        }
    }

    fun attendanceHistoryRequest(nip: String) {
        viewModelScope.launch {
//            val connected = CheckInternet().check()
//            if (connected) {
            if (!_isDataLoaded) {
                _attendanceHistory.postValue(repository.attendanceHistory(nip))
                _attendanceHistory.postValue(NetworkResult.Loading())
            }
//            } else
//                _attendanceHistory.postValue(NetworkResult.Error("No Internet Connection"))
        }

    }
}
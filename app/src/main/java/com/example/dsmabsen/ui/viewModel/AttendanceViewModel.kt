package com.example.dsmabsen.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dsmabsen.model.*
import com.example.dsmabsen.repository.AttendanceRepository
import com.example.dsmabsen.repository.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import xyz.teamgravity.checkinternet.CheckInternet
import javax.inject.Inject

@HiltViewModel
class AttendanceViewModel @Inject constructor(private val repository: AttendanceRepository) :
    ViewModel() {
    private var _isDataLoaded = false

    private val _totalAttendance = MutableLiveData<NetworkResult<TotalAttendance>>()
    val totalAttendanceLiveData: LiveData<NetworkResult<TotalAttendance>> = _totalAttendance

    private val _attendanceHistory = MutableLiveData<NetworkResult<List<DataXXXXXXXXXX>>>()
    val attendanceHistoryLiveData: LiveData<NetworkResult<List<DataXXXXXXXXXX>>> =
        _attendanceHistory

    private val _attendanceToday = MutableLiveData<NetworkResult<Presensi>>()
    val attendanceTodayLiveData: LiveData<NetworkResult<Presensi>> = _attendanceToday

    private val _getSallary = MutableLiveData<NetworkResult<DataSallary>>()
    val getSallaryLiveData: LiveData<NetworkResult<DataSallary>> = _getSallary

    private val _getListLembur = MutableLiveData<NetworkResult<DataLembur>>()
    val getListLemburLiveData: LiveData<NetworkResult<DataLembur>> = _getListLembur

    private val _pengajuanLembur = MutableLiveData<NetworkResult<PengajuanLembur>>()
    val pengajuanLemburLiveData: LiveData<NetworkResult<PengajuanLembur>> = _pengajuanLembur

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

    fun requestSallary(nip: String) {
        viewModelScope.launch {
            val connected = CheckInternet().check()
            if (connected) {
                _getSallary.postValue(NetworkResult.Loading())
                _getSallary.postValue(repository.getSallary(nip))

            } else
                _getSallary.postValue(NetworkResult.Error("No Internet Connection"))
        }
    }

    fun requestListLembur(nip: String) {
        viewModelScope.launch {
            val connected = CheckInternet().check()
            if (connected) {
                _getListLembur.postValue(NetworkResult.Loading())
                _getListLembur.postValue(repository.getListLembur(nip))

            } else
                _getListLembur.postValue(NetworkResult.Error("No Internet Connection"))
        }
    }

    fun requestPengajuanLembur(
        nip: String,
        jam_mulai: String,
        jam_selesai: String,
        file: String,
        tanggal: String,
        keterangan: String
    ) {
        viewModelScope.launch {
            val connected = CheckInternet().check()
            if (connected) {
                _pengajuanLembur.postValue(NetworkResult.Loading())
                _pengajuanLembur.postValue(
                    repository.pengajuanLembur(
                        nip,
                        jam_mulai,
                        jam_selesai,
                        file,
                        tanggal,
                        keterangan
                    )
                )

            } else
                _pengajuanLembur.postValue(NetworkResult.Error("No Internet Connection"))
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

    fun attendanceToday(
//        data:RequestAbsen
        image: MultipartBody.Part,
        nip: RequestBody,
        date: RequestBody,
        timezone: RequestBody,
        kordinat: RequestBody,
        kode_shift: RequestBody,
        kode_tingkat: RequestBody
    ) {
        viewModelScope.launch {
            if (!_isDataLoaded) {
                _attendanceToday.postValue(
                    repository.attendanceToday(
                        image, nip, date, timezone, kordinat, kode_shift, kode_tingkat
                    )
                )
                _attendanceToday.postValue(NetworkResult.Loading())
            }
        }
    }
}
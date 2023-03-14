package com.example.dsmabsen.ui.viewModel

import android.content.Context
import android.widget.Toast
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

    private val _presensi = MutableLiveData<NetworkResult<Presensi>>()
    val presensiLiveData: LiveData<NetworkResult<Presensi>> get() = _presensi

    private val _totalAttendance = MutableLiveData<NetworkResult<TotalAttendance>>()
    val totalAttendanceLiveData: LiveData<NetworkResult<TotalAttendance>> = _totalAttendance

    private val _attendanceHistory = MutableLiveData<NetworkResult<ResponseRiwayat>>()
    val attendanceHistoryLiveData: LiveData<NetworkResult<ResponseRiwayat>>
        get() = _attendanceHistory

    private val _attendanceHistoryLiveData = MutableLiveData<NetworkResult<ResponseRiwayat>>()
    val attendanceHistoryLiveData2: LiveData<NetworkResult<ResponseRiwayat>>
        get() = _attendanceHistoryLiveData

    private val _attendanceToday = MutableLiveData<NetworkResult<Presensi>>()
    val attendanceTodayLiveData: LiveData<NetworkResult<Presensi>>
        get() = _attendanceToday

    private val _getSallary = MutableLiveData<NetworkResult<DataSallary>>()
    val getSallaryLiveData: LiveData<NetworkResult<DataSallary>> = _getSallary

    private val _getListLembur = MutableLiveData<NetworkResult<DataLembur>>()
    val getListLemburLiveData: LiveData<NetworkResult<DataLembur>> = _getListLembur

    private val _pengajuanLembur = MutableLiveData<NetworkResult<PengajuanLembur>>()
    val pengajuanLemburLiveData: LiveData<NetworkResult<PengajuanLembur>> = _pengajuanLembur

    fun attendanceHistoryRequest2(nip: String) {
        viewModelScope.launch {
            val connected = CheckInternet().check()
            if (connected) {
                _attendanceHistoryLiveData.value = NetworkResult.Loading()
                _attendanceHistoryLiveData.value = repository.getAttendanceHistory(nip)
            } else {
                _attendanceHistoryLiveData.value = NetworkResult.Error("Tidak ada koneksi internet")
            }
        }
    }

    fun attendanceToday2(
        image: MultipartBody.Part,
        nip: RequestBody,
        date: RequestBody,
        timezone: RequestBody,
        kordinat: RequestBody,
        kode_shift: RequestBody,
        kode_tingkat: RequestBody
    ) = viewModelScope.launch {
        _presensi.value = repository.attendanceToday(
            image,
            nip,
            date,
            timezone,
            kordinat,
            kode_shift,
            kode_tingkat
        )
    }

    fun attendanceTotalRequest(nip: String) {
        viewModelScope.launch {
            _totalAttendance.postValue(NetworkResult.Loading())
            _totalAttendance.postValue(repository.attendanceTotal(nip))
        }
    }

    fun requestSallary(nip: String) {
        viewModelScope.launch {
            _getSallary.postValue(NetworkResult.Loading())
            _getSallary.postValue(repository.getSallary(nip))


        }
    }

    fun requestListLembur(nip: String) {
        viewModelScope.launch {
            _getListLembur.postValue(NetworkResult.Loading())
            _getListLembur.postValue(repository.getListLembur(nip))
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
        }
    }

    fun attendanceHistoryRequest(nip: String) {
        viewModelScope.launch {
            if (!_isDataLoaded) {
                _attendanceHistory.postValue(repository.attendanceHistory(nip))
                _attendanceHistory.postValue(NetworkResult.Loading())
            }
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
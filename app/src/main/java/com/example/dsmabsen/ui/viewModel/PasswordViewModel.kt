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
import com.example.dsmabsen.repository.PasswordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import xyz.teamgravity.checkinternet.CheckInternet
import javax.inject.Inject

@HiltViewModel
class PasswordViewModel @Inject constructor(private val repository: PasswordRepository) :
    ViewModel() {
    private var _isDataLoaded = false

    private val _ubahPasswordLiveData = MutableLiveData<NetworkResult<UbahPassword>>()
    val ubahPasswordLiveData: LiveData<NetworkResult<UbahPassword>> = _ubahPasswordLiveData

    private val _passwordCheckLiveData = MutableLiveData<NetworkResult<PasswordCheck>>()
    val passwordCheckLiveData: LiveData<NetworkResult<PasswordCheck>> = _passwordCheckLiveData

    fun ubahPasswordRequest(nip:String,
                            password_lama:String,
                            password_baru:String){
        viewModelScope.launch {
            _ubahPasswordLiveData.postValue(NetworkResult.Loading())
            _ubahPasswordLiveData.postValue(repository.ubahPassword(nip, password_lama, password_baru))
        }
    }

    fun passwordCheckRequest(nip: String){
        viewModelScope.launch {
            _passwordCheckLiveData.value = NetworkResult.Loading()
            _passwordCheckLiveData.value = repository.passwordCheck(nip)
//            _passwordCheckLiveData.postValue(NetworkResult.Loading())
//            _passwordCheckLiveData.postValue(repository.passwordCheck(nip))
        }
    }

}
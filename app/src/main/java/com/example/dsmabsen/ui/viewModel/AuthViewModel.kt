package com.example.dsmabsen.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dsmabsen.model.*
import com.example.dsmabsen.repository.AuthRepository
import com.example.dsmabsen.repository.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Response
import xyz.teamgravity.checkinternet.CheckInternet
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val repository: AuthRepository) : ViewModel() {

    private val _userResponseLiveData = MutableLiveData<NetworkResult<LoginResponse>>()
    val userResponseLiveData: LiveData<NetworkResult<LoginResponse>> = _userResponseLiveData

    private val _ubahPasswordLiveData = MutableLiveData<NetworkResult<UbahPassword>>()
    val ubahPasswordLiveData: LiveData<NetworkResult<UbahPassword>> = _ubahPasswordLiveData

    private val _passwordCheckLiveData = MutableLiveData<NetworkResult<ResponseBody>>()
    val passwordCheckLiveData: LiveData<NetworkResult<ResponseBody>> = _passwordCheckLiveData

    private val _logOutLiveData = MutableLiveData<NetworkResult<ResponseBody>>()
    val logOutLiveData: LiveData<NetworkResult<ResponseBody>> = _logOutLiveData

    fun login(email: String, password: String, imei: String) {
        viewModelScope.launch {
            _userResponseLiveData.postValue(NetworkResult.Loading())
            _userResponseLiveData.postValue(repository.loginUser(email, password, imei))
        }

    }

    fun requestLogout(nip: String) {
        viewModelScope.launch {
            _logOutLiveData.postValue(NetworkResult.Loading())
            _logOutLiveData.postValue(repository.logOut(nip))
        }
    }

    suspend fun logout(nip: String?) = withContext(Dispatchers.IO) {
        if (nip != null) {
            repository.logOuts(nip)
        }
    }

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
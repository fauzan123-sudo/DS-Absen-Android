package com.example.dsmabsen.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dsmabsen.model.LoginResponse
import com.example.dsmabsen.model.Logout
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

    private val _logOutLiveData = MutableLiveData<NetworkResult<Logout>>()
    val logOutLiveData: LiveData<NetworkResult<Logout>> = _logOutLiveData

    fun login(email: String, password: String, imei: String) {
        viewModelScope.launch {
            val connected = CheckInternet().check()
            if (connected) {
                _userResponseLiveData.postValue(NetworkResult.Loading())
                _userResponseLiveData.postValue(repository.loginUser(email, password, imei))
            } else
                _userResponseLiveData.postValue(NetworkResult.Error("No Internet Connection"))
        }

    }

    fun requestlogout(nip: String) {
        viewModelScope.launch {
            val connected = CheckInternet().check()
            if (connected) {
                _logOutLiveData.postValue(NetworkResult.Loading())
                _logOutLiveData.postValue(repository.logOut(nip))
            } else
                _logOutLiveData.postValue(NetworkResult.Error("No Internet Connection"))
        }
    }

    fun logout(nip: String) {

    }
}
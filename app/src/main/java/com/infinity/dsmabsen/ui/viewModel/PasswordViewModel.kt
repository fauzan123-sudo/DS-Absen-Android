package com.infinity.dsmabsen.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinity.dsmabsen.model.PasswordCheck
import com.infinity.dsmabsen.model.UbahPassword
import com.infinity.dsmabsen.repository.NetworkResult
import com.infinity.dsmabsen.repository.PasswordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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
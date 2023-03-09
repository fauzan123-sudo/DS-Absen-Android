package com.example.dsmabsen.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dsmabsen.model.ProfileDetail
import com.example.dsmabsen.model.ProfileUser
import com.example.dsmabsen.repository.NetworkResult
import com.example.dsmabsen.repository.UserProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import xyz.teamgravity.checkinternet.CheckInternet
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(private val repository: UserProfileRepository) :
    ViewModel() {

    private val _profileUser = MutableLiveData<NetworkResult<ProfileUser>>()
    val profileUserLivedata: LiveData<NetworkResult<ProfileUser>> = _profileUser

    private val _profileDetail = MutableLiveData<NetworkResult<ProfileDetail>>()
    val profileDetailLivedata: LiveData<NetworkResult<ProfileDetail>> = _profileDetail

    fun profileUserRequest(nip: String) {
        viewModelScope.launch {
            val connected = CheckInternet().check()
            if (connected) {
                _profileUser.postValue(NetworkResult.Loading())
                _profileUser.postValue(repository.getUserProfile(nip))
            } else
                _profileUser.postValue(NetworkResult.Error("No Internet Connection"))
        }

    }

    fun detailProfileRequest(nip: String) {
        viewModelScope.launch {
            val connected = CheckInternet().check()
            if (connected) {
                _profileDetail.postValue(NetworkResult.Loading())
                _profileDetail.postValue(repository.getDetailProfile(nip))
            } else
                _profileDetail.postValue(NetworkResult.Error("No Internet Connection"))
        }

    }
}
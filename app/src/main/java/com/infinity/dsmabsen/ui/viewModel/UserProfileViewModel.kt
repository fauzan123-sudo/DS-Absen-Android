package com.infinity.dsmabsen.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinity.dsmabsen.model.EditProfileResponse
import com.infinity.dsmabsen.model.ProfileDetail
import com.infinity.dsmabsen.model.ProfileUser
import com.infinity.dsmabsen.repository.NetworkResult
import com.infinity.dsmabsen.repository.UserProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(private val repository: UserProfileRepository) :
    ViewModel() {

    private val _profileUser = MutableLiveData<NetworkResult<ProfileUser>>()
    val profileUserLivedata: LiveData<NetworkResult<ProfileUser>>
    get() = _profileUser

    private val _profileDetail = MutableLiveData<NetworkResult<ProfileDetail>>()
    val profileDetailLivedata: LiveData<NetworkResult<ProfileDetail>>
    get() = _profileDetail

    private val _editProfile = MutableLiveData<NetworkResult<EditProfileResponse>>()
    val editProfileLivedata: LiveData<NetworkResult<EditProfileResponse>>
    get() = _editProfile

    fun profileUserRequest(nip: String) {
        viewModelScope.launch {
            _profileUser.postValue(NetworkResult.Loading())
            _profileUser.postValue(repository.getUserProfile(nip))
        }
    }

    fun detailProfileRequest(nip: String) {
        viewModelScope.launch {
            _profileDetail.postValue(NetworkResult.Loading())
            _profileDetail.postValue(repository.getDetailProfile(nip))
        }
    }

    fun editProfileRequest(nip: String, name: String, value: String) {
        viewModelScope.launch {
            _editProfile.postValue(NetworkResult.Loading())
            _editProfile.postValue(repository.editDataProfile(nip, name, value))
        }
    }
}
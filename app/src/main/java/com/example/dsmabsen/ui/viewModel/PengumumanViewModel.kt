package com.example.dsmabsen.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dsmabsen.model.Pengumuman
import com.example.dsmabsen.repository.NetworkResult
import com.example.dsmabsen.repository.PengumumanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PengumumanViewModel @Inject constructor(private val repository: PengumumanRepository) :
    ViewModel() {

    private val _pengumuman = MutableLiveData<NetworkResult<Pengumuman>>()
    val pengumumanLivedata: LiveData<NetworkResult<Pengumuman>> = _pengumuman

    fun pengumumanRequest() {
        viewModelScope.launch {
            _pengumuman.postValue(NetworkResult.Loading())
            _pengumuman.postValue(repository.getPengumuman())
        }
    }
}
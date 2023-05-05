package com.example.tubearhai.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tubearhai.api.ApiServices

class BearViewModelFactory(private val apiServices: ApiServices) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BearViewModel(apiServices) as T
    }
}
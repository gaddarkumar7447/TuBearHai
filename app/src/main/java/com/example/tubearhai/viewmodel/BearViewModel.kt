package com.example.tubearhai.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.tubearhai.api.ApiServices
import com.example.tubearhai.paging.PagingMain

class BearViewModel(private val apiServices: ApiServices) : ViewModel() {

    val listData = Pager(PagingConfig(pageSize = 1)){ PagingMain(apiServices) }.flow.cachedIn(viewModelScope)

}
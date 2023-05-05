package com.example.tubearhai.paging


import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.tubearhai.api.ApiServices
import com.example.tubearhai.model.BearDataItem

class PagingMain (private val apiServices: ApiServices) : PagingSource<Int, BearDataItem>(){
override fun getRefreshKey(state: PagingState<Int, BearDataItem>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BearDataItem> {
        return try {
            val currentPage = params.key ?: 1
            //Log.d("CurrentPage", currentPage.toString())
            val response = apiServices.getBearData(currentPage, 10)
            val responseData1: List<BearDataItem>? = response.body()?.map { it1 -> it1}

            LoadResult.Page(
                data = responseData1!!,
                prevKey = if (currentPage == 1) null else -1,
                nextKey = currentPage.plus(1)
            )
        }catch (e : Exception){
            LoadResult.Error(e.fillInStackTrace())
        }
    }

}

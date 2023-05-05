package com.example.tubearhai.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tubearhai.adapter.BearAdapter
import com.example.tubearhai.api.ApiIntence
import com.example.tubearhai.api.ApiServices
import com.example.tubearhai.databinding.ActivityMainBinding
import com.example.tubearhai.model.BearData
import com.example.tubearhai.model.BearDataItem
import com.example.tubearhai.viewmodel.BearViewModel
import com.example.tubearhai.viewmodel.BearViewModelFactory
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: BearAdapter
    private lateinit var bearDataItem: MutableList<BearDataItem>
    private lateinit var viewModel: BearViewModel
    private lateinit var binding : ActivityMainBinding
    private var isFirstTimeRefreshing = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        bearDataItem = mutableListOf()

        adapter = BearAdapter()

        initializeViewModel()

        loadData()

        setUpRecyclerView()

    }

    private fun setUpRecyclerView() {
        binding.recyclerViewVeil.apply {
            setAdapter(adapter)
            setLayoutManager(LinearLayoutManager(this@MainActivity))
            addVeiledItems(10)
        }
        adapter.addLoadStateListener { loadState ->
            // show empty list
            if (loadState.refresh is LoadState.Loading || loadState.append is LoadState.Loading){
                if (isFirstTimeRefreshing){
                    binding.recyclerViewVeil.veil()
                }else{
                    binding.progressBar.visibility = View.VISIBLE
                    binding.recyclerViewVeil.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                        this.setMargins(0,0,0,100)
                    }
                }
            }else{
                if (isFirstTimeRefreshing){
                    binding.recyclerViewVeil.postDelayed({
                        binding.recyclerViewVeil.unVeil()
                        isFirstTimeRefreshing = false
                    },2000)
                }
                binding.progressBar.visibility = View.INVISIBLE
                binding.recyclerViewVeil.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    setMargins(0,0,0,0)
                }
                // if an error then show toast
                val errorState = when{
                    loadState.append is LoadState.Error ->loadState.append as LoadState.Error
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }
                errorState?.apply {
                    Toast.makeText(this@MainActivity, "${this.error}", Toast.LENGTH_SHORT).show()
                    Log.d("DataCome", "${this.error}")
                }
            }
        }
    }

    private fun loadData() {
        lifecycleScope.launch{
            viewModel.listData.collect{
                val data: PagingData<BearDataItem> = it
                adapter.submitData(data)
            }
        }
    }

    private fun initializeViewModel() {
        val apiIntence = ApiIntence.getApiInstance(this).create(ApiServices::class.java)
        viewModel = ViewModelProvider(this, BearViewModelFactory(apiIntence))[BearViewModel::class.java]
    }
}
package com.sachin_singh_dighan.newsapp.ui.countryselection

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.sachin_singh_dighan.newsapp.AppConstant
import com.sachin_singh_dighan.newsapp.data.model.countryselection.CountrySelection
import com.sachin_singh_dighan.newsapp.databinding.ActivityCountrySelectionBinding
import com.sachin_singh_dighan.newsapp.ui.base.BaseActivity
import com.sachin_singh_dighan.newsapp.ui.common.UiState
import com.sachin_singh_dighan.newsapp.ui.dialog.ErrorDialog
import com.sachin_singh_dighan.newsapp.ui.news.NewsListActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CountrySelectionActivity :
    BaseActivity<CountrySelectionViewModel, ActivityCountrySelectionBinding>() {

    @Inject
    lateinit var adapter: CountrySelectionAdapter

    @Inject
    lateinit var errorDialog: ErrorDialog


    companion object {
        fun getInstance(context: Context): Intent {
            return Intent(context, CountrySelectionActivity::class.java)
        }
    }

    override fun setUpViewBinding(inflate: LayoutInflater): ActivityCountrySelectionBinding {
        return ActivityCountrySelectionBinding.inflate(layoutInflater)
    }

    override fun setUpViewModel() {
        viewModel = ViewModelProvider(this)[CountrySelectionViewModel::class.java]
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        val recyclerView = binding.mainRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                (recyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        recyclerView.adapter = adapter
        adapter.itemClickListener = { _, countryCode ->
            startActivity(
                NewsListActivity.getInstance(
                    this@CountrySelectionActivity,
                    newsType = AppConstant.NEWS_BY_COUNTRY,
                    newsCountry = countryCode,

                    )
            )
            finish()
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun setupObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    when (it) {
                        is UiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            it.data?.let { newsList ->
                                renderList(newsList as List<CountrySelection>)
                            }
                            binding.mainRecyclerView.visibility = View.VISIBLE
                        }

                        is UiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.mainRecyclerView.visibility = View.GONE
                        }

                        is UiState.Error -> {
                            //Handle Error
                            binding.progressBar.visibility = View.GONE
                            errorDialog.showResetPasswordDialog(
                                this@CountrySelectionActivity,
                                it.message,
                            )
                        }
                    }
                }
            }
        }
    }


    private fun renderList(countrySelection: List<CountrySelection>) {
        adapter.addData(countrySelection)
    }
}
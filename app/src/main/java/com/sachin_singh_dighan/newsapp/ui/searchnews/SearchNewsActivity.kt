package com.sachin_singh_dighan.newsapp.ui.searchnews

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.SearchView
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.sachin_singh_dighan.newsapp.data.model.topheadline.Article
import com.sachin_singh_dighan.newsapp.databinding.ActivitySearchNewsBinding
import com.sachin_singh_dighan.newsapp.ui.base.BaseActivity
import com.sachin_singh_dighan.newsapp.ui.common.UiState
import com.sachin_singh_dighan.newsapp.ui.dialog.ErrorDialog
import com.sachin_singh_dighan.newsapp.ui.news.NewsListAdapter
import com.sachin_singh_dighan.newsapp.utils.getQueryTextChangeStateFlow
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SearchNewsActivity : BaseActivity<SearchNewsViewModel, ActivitySearchNewsBinding>() {

    @Inject
    lateinit var adapter: NewsListAdapter

    @Inject
    lateinit var errorDialog: ErrorDialog

    override fun setUpViewBinding(inflate: LayoutInflater): ActivitySearchNewsBinding {
        return ActivitySearchNewsBinding.inflate(layoutInflater)
    }

    override fun setUpViewModel() {
        viewModel = ViewModelProvider(this)[SearchNewsViewModel::class.java]
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        binding.rvNewList.layoutManager = LinearLayoutManager(this)
        binding.rvNewList.addItemDecoration(
            DividerItemDecoration(
                binding.rvNewList.context,
                (binding.rvNewList.layoutManager as LinearLayoutManager).orientation
            )
        )
        binding.rvNewList.adapter = adapter
        binding.progressBar.visibility = View.GONE
        fetchSearchResultData()
        adapter.itemClickListener = { _, data ->
            val article = data as Article
            val builder = CustomTabsIntent.Builder()
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(this@SearchNewsActivity, Uri.parse(article.url))
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun setupObserver() {
        viewModel.setQueryTextChangeStateFlow(binding.svSearch.getQueryTextChangeStateFlow())
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    when (it) {
                        is UiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            it.data?.let { newsList ->
                                renderList(newsList as List<Article>)
                            }
                            binding.rvNewList.visibility = View.VISIBLE
                        }

                        is UiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.rvNewList.visibility = View.GONE
                        }

                        is UiState.Error -> {
                            binding.rvNewList.visibility = View.GONE
                            binding.progressBar.visibility = View.GONE
                            errorDialog.showResetPasswordDialog(
                                this@SearchNewsActivity,
                                it.message,
                            )
                        }
                    }
                }
            }
        }
    }

    private fun fetchSearchResultData() {
        binding.svSearch.setOnCloseListener {
            binding.progressBar.visibility = View.GONE
            binding.rvNewList.visibility = View.GONE
            true
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun renderList(articleList: List<Article>) {
        adapter.replaceData(articleList)
        adapter.notifyDataSetChanged()
    }
}
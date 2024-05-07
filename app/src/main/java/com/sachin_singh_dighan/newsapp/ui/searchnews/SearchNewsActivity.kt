package com.sachin_singh_dighan.newsapp.ui.searchnews

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.sachin_singh_dighan.newsapp.NewsApplication
import com.sachin_singh_dighan.newsapp.data.model.topheadline.Article
import com.sachin_singh_dighan.newsapp.databinding.ActivitySearchNewsBinding
import com.sachin_singh_dighan.newsapp.di.component.searchnews.DaggerSearchNewsComponent
import com.sachin_singh_dighan.newsapp.di.module.searchnews.SearchNewsModule
import com.sachin_singh_dighan.newsapp.ui.base.UiState
import com.sachin_singh_dighan.newsapp.ui.dialog.ErrorDialog
import com.sachin_singh_dighan.newsapp.ui.topheadline.TopHeadLineAdapter
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchNewsActivity : AppCompatActivity() {

    @Inject
    lateinit var searchNewsViewModel: SearchNewsViewModel

    @Inject
    lateinit var adapter: TopHeadLineAdapter

    @Inject
    lateinit var errorDialog: ErrorDialog

    private lateinit var binding: ActivitySearchNewsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        super.onCreate(savedInstanceState)
        binding = ActivitySearchNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUI()
        fetchSearchResultData()
        setupObserver()

    }

    private fun injectDependencies() {
        DaggerSearchNewsComponent.builder()
            .applicationComponent((application as NewsApplication).applicationComponent)
            .searchNewsModule(SearchNewsModule(this)).build().inject(this)

    }

    private fun setupUI() {
        binding.rvNewList.layoutManager = LinearLayoutManager(this)
        binding.rvNewList.addItemDecoration(
            DividerItemDecoration(
                binding.rvNewList.context,
                (binding.rvNewList.layoutManager as LinearLayoutManager).orientation
            )
        )
        binding.rvNewList.adapter = adapter
        binding.progressBar.visibility = View.GONE
    }

    private fun setupObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchNewsViewModel.uiState.collect {
                    when (it) {
                        is UiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            renderList(it.data)
                            binding.rvNewList.visibility = View.VISIBLE
                        }
                        is UiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.rvNewList.visibility = View.GONE
                        }
                        is UiState.Error -> {
                            binding.rvNewList.visibility = View.GONE
                            binding.progressBar.visibility = View.GONE
                            errorDialog.showResetPasswordDialog(this@SearchNewsActivity)
                        }
                    }
                }
            }
        }
    }


    private fun fetchSearchResultData() {
        binding.svSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                searchNewsViewModel.searchQuery(newText)
                return true
            }
        })

        binding.svSearch.setOnCloseListener {
            binding.progressBar.visibility = View.GONE
            binding.rvNewList.visibility = View.GONE
            true
        }

    }

    private fun renderList(articleList: List<Article>) {
        adapter.addData(articleList)
        adapter.notifyDataSetChanged()
    }
}
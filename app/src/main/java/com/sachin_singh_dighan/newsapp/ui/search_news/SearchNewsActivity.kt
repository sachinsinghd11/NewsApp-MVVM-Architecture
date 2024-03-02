package com.sachin_singh_dighan.newsapp.ui.search_news

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
import com.sachin_singh_dighan.newsapp.data.model.top_headline.Article
import com.sachin_singh_dighan.newsapp.databinding.ActivitySearchNewsBinding
import com.sachin_singh_dighan.newsapp.di.component.search_news.DaggerSearchNewsComponent
import com.sachin_singh_dighan.newsapp.di.module.search_news.SearchNewsModule
import com.sachin_singh_dighan.newsapp.ui.base.UiState
import com.sachin_singh_dighan.newsapp.ui.dialog.ErrorDialog
import com.sachin_singh_dighan.newsapp.ui.top_headline.TopHeadLineAdapter
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
        searchNewsViewModel.setIsSearchTextEntered(false)
        super.onCreate(savedInstanceState)
        binding = ActivitySearchNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fetchSearchResultData()
        setupUI()
        setupObserver()

    }

    private fun injectDependencies() {
        DaggerSearchNewsComponent.builder()
            .applicationComponent((application as NewsApplication).applicationComponent)
            .searchNewsModule(SearchNewsModule(this)).build().inject(this)

    }

    private fun setupUI() {
        val recyclerView = binding.rvNewList
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                (recyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        recyclerView.adapter = adapter
        binding.messageTextView.visibility = View.VISIBLE
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

                            if(searchNewsViewModel.getIsSearchTextEntered() == true){
                                binding.progressBar.visibility = View.VISIBLE
                                binding.rvNewList.visibility = View.GONE
                            }else{
                                binding.progressBar.visibility = View.GONE
                                binding.rvNewList.visibility = View.GONE
                            }
                        }
                        is UiState.Error -> {
                            if(searchNewsViewModel.getIsSearchTextEntered() == true){
                                binding.rvNewList.visibility = View.GONE
                                binding.progressBar.visibility = View.VISIBLE
                                errorDialog.showResetPasswordDialog(this@SearchNewsActivity)
                            }else{
                                binding.rvNewList.visibility = View.GONE
                                binding.progressBar.visibility = View.GONE
                            }
                        }
                    }
                }
            }
        }
    }


    private fun fetchSearchResultData(){
        binding.svSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                binding.messageTextView.visibility = View.GONE
                searchNewsViewModel.fetchSearchResult(query)
                adapter.notifyDataSetChanged()
                return false
            }
            override fun onQueryTextChange(newText: String): Boolean {
                if(newText.isEmpty()){
                    searchNewsViewModel.setIsSearchTextEntered(false)
                searchNewsViewModel.fetchSearchResult(newText)
                adapter.notifyDataSetChanged()
                    setupObserver()
                    binding.messageTextView.visibility = View.VISIBLE
                }else{
                    searchNewsViewModel.setIsSearchTextEntered(true)
                }

                return false
            }
        })

        binding.svSearch.setOnCloseListener {
            searchNewsViewModel.setIsSearchTextEntered(true)
            binding.svSearch.setQuery("",false)
            binding.messageTextView.visibility = View.GONE
            false
        }

    }

    private fun renderList(articleList:  List<Article>) {
        adapter.addData(articleList)
        adapter.notifyDataSetChanged()
    }
}
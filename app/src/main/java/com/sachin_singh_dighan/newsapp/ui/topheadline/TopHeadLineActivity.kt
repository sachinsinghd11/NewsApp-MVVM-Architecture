package com.sachin_singh_dighan.newsapp.ui.topheadline

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.sachin_singh_dighan.newsapp.AppConstant
import com.sachin_singh_dighan.newsapp.NewsApplication
import com.sachin_singh_dighan.newsapp.data.model.top_headline.Article
import com.sachin_singh_dighan.newsapp.databinding.ActivityTopHeadLineBinding
import com.sachin_singh_dighan.newsapp.di.component.topheadline.DaggerTopHeadLineComponent
import com.sachin_singh_dighan.newsapp.di.module.topheadline.TopHeadLineModule
import com.sachin_singh_dighan.newsapp.ui.base.UiState
import com.sachin_singh_dighan.newsapp.ui.dialog.ErrorDialog
import kotlinx.coroutines.launch
import javax.inject.Inject

class TopHeadLineActivity : AppCompatActivity() {

    @Inject
    lateinit var newsListViewModel: TopHeadLineViewModel

    @Inject
    lateinit var adapter: TopHeadLineAdapter

    @Inject
    lateinit var errorDialog: ErrorDialog

    private lateinit var binding: ActivityTopHeadLineBinding

    companion object {
        const val COUNTRY_CODE = "country code"
        const val LANGUAGE_CODE = "language code"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        super.onCreate(savedInstanceState)
        binding = ActivityTopHeadLineBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fetchNewsData()
        setupUI()
        setupObserver()
    }

    private fun fetchNewsData(){
        val bundle = intent.extras
        if (bundle != null){
            newsListViewModel.fetchNews(bundle.getString(COUNTRY_CODE)?:"", bundle.getString(LANGUAGE_CODE)?:"")
        }else{
            newsListViewModel.fetchNews(AppConstant.COUNTRY)
        }
    }

    private fun setupObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                newsListViewModel.uiState.collect {
                    when (it) {
                        is UiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            renderList(it.data)
                            binding.recyclerView.visibility = View.VISIBLE
                        }
                        is UiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.recyclerView.visibility = View.GONE
                        }
                        is UiState.Error -> {
                            //Handle Error
                            binding.progressBar.visibility = View.GONE
                            errorDialog.showResetPasswordDialog(this@TopHeadLineActivity)
                        }
                    }
                }
            }
        }
    }

    private fun setupUI() {
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                (recyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        recyclerView.adapter = adapter
    }

    private fun renderList(articleList:  List<Article>) {
        adapter.addData(articleList)
        adapter.notifyDataSetChanged()
    }

    private fun injectDependencies() {
        DaggerTopHeadLineComponent.builder()
            .applicationComponent((application as NewsApplication).applicationComponent)
            .topHeadLineModule(TopHeadLineModule(this)).build().inject(this)
    }
}
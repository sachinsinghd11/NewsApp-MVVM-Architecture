package com.sachin_singh_dighan.newsapp.ui.news

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.sachin_singh_dighan.newsapp.AppConstant
import com.sachin_singh_dighan.newsapp.NewsApplication
import com.sachin_singh_dighan.newsapp.data.model.topheadline.Article
import com.sachin_singh_dighan.newsapp.databinding.ActivityNewsListBinding
import com.sachin_singh_dighan.newsapp.di.component.news.DaggerNewsListComponent
import com.sachin_singh_dighan.newsapp.di.module.news.NewsListModule
import com.sachin_singh_dighan.newsapp.ui.base.UiState
import com.sachin_singh_dighan.newsapp.ui.countryselection.CountrySelectionActivity
import com.sachin_singh_dighan.newsapp.ui.dialog.ErrorDialog
import com.sachin_singh_dighan.newsapp.ui.languageselection.LanguageSelectionActivity
import com.sachin_singh_dighan.newsapp.ui.newsources.NewsSourcesActivity
import com.sachin_singh_dighan.newsapp.ui.topheadline.TopHeadLineAdapter
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewsListActivity : AppCompatActivity() {

    companion object {
        private const val NEWS_TYPE = "News Type"
        private const val NEWS_SOURCE = "News Source"
        private const val NEWS_COUNTRY = "News Country"
        private const val NEWS_LANGUAGE = "News Language"


        fun getInstance(
            context: Context,
            newsType: String,
            newsSource: String? = "",
            newsCountry: String? = "",
            newsLanguage: ArrayList<String> = arrayListOf(),
        ): Intent {
            return Intent(context, NewsListActivity::class.java).apply {
                putExtra(NEWS_TYPE, newsType)
                putExtra(NEWS_SOURCE, newsSource)
                putExtra(NEWS_COUNTRY, newsCountry)
                putStringArrayListExtra(NEWS_LANGUAGE, newsLanguage)
            }
        }
    }

    @Inject
    lateinit var newsListViewModel: NewsListViewModel

    @Inject
    lateinit var adapter: NewsListAdapter

    @Inject
    lateinit var errorDialog: ErrorDialog

    private lateinit var binding: ActivityNewsListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        super.onCreate(savedInstanceState)
        binding = ActivityNewsListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fetchNewsData()
        setupUI()
        setupObserver()
    }

    private fun fetchNewsData() {
        intent.extras?.apply {
            val newsType = getString(NEWS_TYPE)
            newsType?.let { type ->
                when (type) {
                    AppConstant.NEWS_BY_SOURCE -> {
                        val source = getString(NEWS_SOURCE)
                        source?.let {
                            newsListViewModel.fetchNewsByResource(it)
                        }
                    }

                    AppConstant.NEWS_BY_COUNTRY -> {
                        val country = getString(NEWS_COUNTRY)
                        country?.let {
                            newsListViewModel.fetchNewsByCountry(it)
                        }
                    }

                    AppConstant.NEWS_BY_LANGUAGE -> {
                        try{
                            val language = getStringArrayList(NEWS_LANGUAGE)
                            language?.let {
                                newsListViewModel.fetchNewsByLanguage(it)
                            }
                        }catch (e: Exception){
                            Log.d("NewsListActivity", e.toString())
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
                            errorDialog.showResetPasswordDialog(this@NewsListActivity, it.message,)
                        }
                    }
                }
            }
        }
    }

    private fun renderList(articleList: List<Article>) {
        adapter.addData(articleList)
        adapter.notifyDataSetChanged()
    }

    private fun injectDependencies() {
        DaggerNewsListComponent.builder()
            .applicationComponent((application as NewsApplication).applicationComponent)
            .newsListModule(NewsListModule(this)).build().inject(this)
    }


    override fun onBackPressed() {
        super.onBackPressed()
        intent.extras?.apply {
            val newsType = getString(NEWS_TYPE)
            newsType?.let { type ->
                when (type) {
                    AppConstant.NEWS_BY_SOURCE -> {
                        startActivity(NewsSourcesActivity.getInstance(this@NewsListActivity))
                    }
                    AppConstant.NEWS_BY_COUNTRY -> {
                        startActivity(CountrySelectionActivity.getInstance(this@NewsListActivity))
                    }
                    AppConstant.NEWS_BY_LANGUAGE -> {
                        startActivity(LanguageSelectionActivity.getInstance(this@NewsListActivity, true))
                    }
                }
            }
        }

    }
}
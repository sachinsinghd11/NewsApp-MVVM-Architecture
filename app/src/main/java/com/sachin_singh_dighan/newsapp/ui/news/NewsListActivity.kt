@file:Suppress("UNCHECKED_CAST")

package com.sachin_singh_dighan.newsapp.ui.news

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.sachin_singh_dighan.newsapp.AppConstant
import com.sachin_singh_dighan.newsapp.data.model.topheadline.Article
import com.sachin_singh_dighan.newsapp.databinding.ActivityNewsListBinding
import com.sachin_singh_dighan.newsapp.ui.base.BaseActivity
import com.sachin_singh_dighan.newsapp.ui.common.UiState
import com.sachin_singh_dighan.newsapp.ui.countryselection.CountrySelectionActivity
import com.sachin_singh_dighan.newsapp.ui.dialog.ErrorDialog
import com.sachin_singh_dighan.newsapp.ui.languageselection.LanguageSelectionActivity
import com.sachin_singh_dighan.newsapp.ui.newsources.NewsSourcesActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NewsListActivity : BaseActivity<NewsListViewModel, ActivityNewsListBinding>() {

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
    lateinit var adapter: NewsListAdapter

    @Inject
    lateinit var errorDialog: ErrorDialog

    override fun setUpViewBinding(inflate: LayoutInflater): ActivityNewsListBinding {
        return ActivityNewsListBinding.inflate(layoutInflater)
    }

    override fun setUpViewModel() {
        viewModel = ViewModelProvider(this)[NewsListViewModel::class.java]
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                (recyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        recyclerView.adapter = adapter
        fetchNewsData()
        adapter.itemClickListener = { _, data ->
            val article = data as Article
            val builder = CustomTabsIntent.Builder()
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(this@NewsListActivity, Uri.parse(article.url))
        }
    }

    override fun setupObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    when (it) {
                        is UiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            it.data?.let { newsList ->
                                renderList(newsList as List<Article>)
                            }
                            binding.recyclerView.visibility = View.VISIBLE
                        }

                        is UiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.recyclerView.visibility = View.GONE
                        }

                        is UiState.Error -> {
                            //Handle Error
                            binding.progressBar.visibility = View.GONE
                            errorDialog.showResetPasswordDialog(this@NewsListActivity, it.message)
                        }
                    }
                }
            }
        }
    }

    private fun fetchNewsData() {
        intent.extras?.apply {
            val newsType = getString(NEWS_TYPE)
            newsType?.let { type ->
                when (type) {
                    AppConstant.NEWS_BY_SOURCE -> {
                        val source = getString(NEWS_SOURCE)
                        source?.let {
                            viewModel.fetchNewsByResource(it)
                        }
                    }

                    AppConstant.NEWS_BY_COUNTRY -> {
                        val country = getString(NEWS_COUNTRY)
                        country?.let {
                            viewModel.fetchNewsByCountry(it)
                        }
                    }

                    AppConstant.NEWS_BY_LANGUAGE -> {
                        try {
                            val language = getStringArrayList(NEWS_LANGUAGE)
                            language?.let {
                                viewModel.fetchNewsByLanguage(it)
                            }
                        } catch (e: Exception) {
                            Log.d("NewsListActivity", e.toString())
                        }

                    }
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun renderList(articleList: List<Article>) {
        adapter.addData(articleList)
        adapter.notifyDataSetChanged()
    }


    @Deprecated("Deprecated in Java")
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
                        startActivity(
                            LanguageSelectionActivity.getInstance(
                                this@NewsListActivity, true
                            )
                        )
                    }
                }
            }
        }

    }
}
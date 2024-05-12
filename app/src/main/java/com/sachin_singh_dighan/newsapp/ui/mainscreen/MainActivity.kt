package com.sachin_singh_dighan.newsapp.ui.mainscreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.sachin_singh_dighan.newsapp.AppConstant
import com.sachin_singh_dighan.newsapp.data.model.mainscreen.MainSection
import com.sachin_singh_dighan.newsapp.databinding.ActivityMainBinding
import com.sachin_singh_dighan.newsapp.ui.base.BaseActivity
import com.sachin_singh_dighan.newsapp.ui.common.UiState
import com.sachin_singh_dighan.newsapp.ui.countryselection.CountrySelectionActivity
import com.sachin_singh_dighan.newsapp.ui.languageselection.LanguageSelectionActivity
import com.sachin_singh_dighan.newsapp.ui.newsources.NewsSourcesActivity
import com.sachin_singh_dighan.newsapp.ui.searchnews.SearchNewsActivity
import com.sachin_singh_dighan.newsapp.ui.topheadline.TopHeadLineActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    @Inject
    lateinit var adapter: MainActivityAdapter

    override fun setUpViewBinding(inflate: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun setUpViewModel() {
        viewModel = ViewModelProvider(this@MainActivity)[MainViewModel::class.java]
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

        adapter.itemClickListener = { _, sectionClicked ->
            when (sectionClicked.sectionName) {
                AppConstant.TOP_HEADLINES -> {
                    startActivity(
                        TopHeadLineActivity.getInstance(
                            this@MainActivity,
                            AppConstant.NEWS_BY_DEFAULT
                        )
                    )
                }

                AppConstant.NEWS_SOURCES -> {
                    val intent = Intent(this@MainActivity, NewsSourcesActivity::class.java)
                    startActivity(intent)
                }

                AppConstant.COUNTRIES -> {
                    val intent = Intent(this@MainActivity, CountrySelectionActivity::class.java)
                    startActivity(intent)
                }

                AppConstant.LANGUAGES -> {
                    val intent = Intent(this@MainActivity, LanguageSelectionActivity::class.java)
                    startActivity(intent)
                }

                AppConstant.SEARCH -> {
                    val intent = Intent(this@MainActivity, SearchNewsActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }


    @Suppress("UNCHECKED_CAST")
    override fun setupObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect() {
                    when (it) {
                        is UiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            it.data?.let { newsList ->
                                renderList(newsList as List<MainSection>)
                            }
                            binding.mainRecyclerView.visibility = View.VISIBLE
                        }

                        is UiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.mainRecyclerView.visibility = View.GONE
                        }

                        is UiState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this@MainActivity, it.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun renderList(mainSection: List<MainSection>) {
        adapter.addData(mainSection)
        adapter.notifyDataSetChanged()
    }
}
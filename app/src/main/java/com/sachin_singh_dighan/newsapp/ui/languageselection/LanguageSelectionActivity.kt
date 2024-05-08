package com.sachin_singh_dighan.newsapp.ui.languageselection

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.sachin_singh_dighan.newsapp.AppConstant
import com.sachin_singh_dighan.newsapp.NewsApplication
import com.sachin_singh_dighan.newsapp.data.model.languageselection.LanguageData
import com.sachin_singh_dighan.newsapp.databinding.ActivityLanguageSelectionBinding
import com.sachin_singh_dighan.newsapp.di.component.languageselection.DaggerLanguageSelectionComponent
import com.sachin_singh_dighan.newsapp.di.module.languageselection.LanguageSelectionModule
import com.sachin_singh_dighan.newsapp.ui.base.UiState
import com.sachin_singh_dighan.newsapp.ui.news.NewsListActivity
import com.sachin_singh_dighan.newsapp.ui.topheadline.TopHeadLineActivity
import kotlinx.coroutines.launch
import javax.inject.Inject

class LanguageSelectionActivity : AppCompatActivity() {

    @Inject
    lateinit var languageSelectionViewModel: LanguageSelectionViewModel

    @Inject
    lateinit var adapter: LanguageSelectionAdapter

    private lateinit var binding: ActivityLanguageSelectionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        super.onCreate(savedInstanceState)
        binding = ActivityLanguageSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUi()
        setupObserver()
    }

    private fun injectDependencies() {
        DaggerLanguageSelectionComponent.builder()
            .applicationComponent((application as NewsApplication).applicationComponent)
            .languageSelectionModule(LanguageSelectionModule(this)).build().inject(this)
    }

    private fun setUi() {
        val recyclerView = binding.languageRecyclerView
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
            repeatOnLifecycle(Lifecycle.State.STARTED){
                languageSelectionViewModel.uiState.collect(){
                    when(it){
                        is UiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            renderList(it.data)
                            binding.languageRecyclerView.visibility = View.VISIBLE
                        }
                        is UiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.languageRecyclerView.visibility = View.GONE
                        }
                        is UiState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this@LanguageSelectionActivity, it.message, Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                }
            }
        }
    }

    private fun renderList(languageData: List<LanguageData>){
        adapter.addData(languageData)
    }
    fun onLanguageClick(languageCode: String) {
        startActivity(
            NewsListActivity.getInstance(
                this@LanguageSelectionActivity,
                newsType = AppConstant.NEWS_BY_LANGUAGE,
                newsLanguage = languageCode,
            )
        )
    }
}
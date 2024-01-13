package com.sachin_singh_dighan.newsapp.ui.language_selection

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
import com.sachin_singh_dighan.newsapp.NewsApplication
import com.sachin_singh_dighan.newsapp.R
import com.sachin_singh_dighan.newsapp.data.model.country_selection.CountrySelection
import com.sachin_singh_dighan.newsapp.data.model.language_selection.LanguageData
import com.sachin_singh_dighan.newsapp.databinding.ActivityLanguageSelectionBinding
import com.sachin_singh_dighan.newsapp.di.component.language_selection.DaggerLanguageSelectionComponent
import com.sachin_singh_dighan.newsapp.di.module.language_selection.LanguageSelectionModule
import com.sachin_singh_dighan.newsapp.ui.base.UiState
import com.sachin_singh_dighan.newsapp.ui.country_selection.CountrySelectionAdapter
import com.sachin_singh_dighan.newsapp.ui.country_selection.CountrySelectionViewModel
import com.sachin_singh_dighan.newsapp.ui.top_headline.TopHeadLineActivity
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
        val bundle = Bundle()
        bundle.putString(TopHeadLineActivity.LANGUAGE_CODE, languageCode)
        val intent = Intent(this, TopHeadLineActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
}
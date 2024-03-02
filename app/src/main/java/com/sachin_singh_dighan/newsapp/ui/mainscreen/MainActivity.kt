package com.sachin_singh_dighan.newsapp.ui.mainscreen

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.sachin_singh_dighan.newsapp.AppConstant
import com.sachin_singh_dighan.newsapp.NewsApplication
import com.sachin_singh_dighan.newsapp.data.model.main_screen.MainSection
import com.sachin_singh_dighan.newsapp.databinding.ActivityMainBinding
import com.sachin_singh_dighan.newsapp.di.component.mainscreen.DaggerMainActivityComponent
import com.sachin_singh_dighan.newsapp.di.module.mainscreen.MainActivityModule
import com.sachin_singh_dighan.newsapp.ui.base.UiState
import com.sachin_singh_dighan.newsapp.ui.countryselection.CountrySelectionActivity
import com.sachin_singh_dighan.newsapp.ui.languageselection.LanguageSelectionActivity
import com.sachin_singh_dighan.newsapp.ui.newsources.NewSourcesActivity
import com.sachin_singh_dighan.newsapp.ui.searchnews.SearchNewsActivity
import com.sachin_singh_dighan.newsapp.ui.topheadline.TopHeadLineActivity
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : AppCompatActivity(){

    @Inject
    lateinit var mainViewModel: MainViewModel

    @Inject
    lateinit var adapter: MainActivityAdapter

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUi()
        setupObserver()
    }

    private fun setUi() {
    val recyclerView = binding.mainRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                (recyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        recyclerView.adapter = adapter
    }

    private fun setupObserver(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                mainViewModel.uiState.collect(){
                    when(it){
                        is UiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            renderList(it.data)
                            binding.mainRecyclerView.visibility = View.VISIBLE
                        }
                        is UiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.mainRecyclerView.visibility = View.GONE
                        }
                        is UiState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this@MainActivity, it.message, Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                }
            }
        }
    }

    private fun renderList(mainSection: List<MainSection>){
        adapter.addData(mainSection)
    }

    private fun injectDependencies() {
        DaggerMainActivityComponent.builder()
            .applicationComponent((application as NewsApplication).applicationComponent)
            .mainActivityModule(MainActivityModule(this)).build().inject(this)
    }

    fun onMainSectionItemClick(sectionClicked: String){
        when (sectionClicked){
            AppConstant.TOP_HEADLINES ->{
                val intent = Intent(this, TopHeadLineActivity::class.java)
                startActivity(intent)
            }
            AppConstant.NEWS_SOURCES ->{
                val intent = Intent(this, NewSourcesActivity::class.java)
                startActivity(intent)
            }
            AppConstant.COUNTRIES ->{
                val intent = Intent(this, CountrySelectionActivity::class.java)
                startActivity(intent)
            }
            AppConstant.LANGUAGES ->{
                val intent = Intent(this, LanguageSelectionActivity::class.java)
                startActivity(intent)
            }
            AppConstant.SEARCH ->{
                val intent = Intent(this, SearchNewsActivity::class.java)
                startActivity(intent)
            }
        }

    }
}
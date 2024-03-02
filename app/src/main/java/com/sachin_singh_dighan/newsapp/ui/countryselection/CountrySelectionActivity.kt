package com.sachin_singh_dighan.newsapp.ui.countryselection

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
import com.sachin_singh_dighan.newsapp.data.model.country_selection.CountrySelection
import com.sachin_singh_dighan.newsapp.databinding.ActivityCountrySelectionBinding
import com.sachin_singh_dighan.newsapp.di.component.countryselection.DaggerCountrySelectionComponent
import com.sachin_singh_dighan.newsapp.di.module.countryselection.CountrySelectionModule
import com.sachin_singh_dighan.newsapp.ui.base.UiState
import com.sachin_singh_dighan.newsapp.ui.topheadline.TopHeadLineActivity
import kotlinx.coroutines.launch
import javax.inject.Inject

class CountrySelectionActivity : AppCompatActivity() {

    @Inject
    lateinit var countrySelectionViewModel: CountrySelectionViewModel

    @Inject
    lateinit var adapter: CountrySelectionAdapter

    private lateinit var binding: ActivityCountrySelectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        super.onCreate(savedInstanceState)
        binding = ActivityCountrySelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUi()
        setupObserver()
    }

    private fun injectDependencies() {
        DaggerCountrySelectionComponent.builder()
            .applicationComponent((application as NewsApplication).applicationComponent)
            .countrySelectionModule(CountrySelectionModule(this)).build().inject(this)
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

    private fun setupObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                countrySelectionViewModel.uiState.collect(){
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
                            Toast.makeText(this@CountrySelectionActivity, it.message, Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                }
            }
        }
    }


    private fun renderList(countrySelection: List<CountrySelection>){
        adapter.addData(countrySelection)
    }

    fun onCountryClick(countryCode: String) {
        val bundle = Bundle()
        bundle.putString(TopHeadLineActivity.COUNTRY_CODE, countryCode)
        val intent = Intent(this, TopHeadLineActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
}
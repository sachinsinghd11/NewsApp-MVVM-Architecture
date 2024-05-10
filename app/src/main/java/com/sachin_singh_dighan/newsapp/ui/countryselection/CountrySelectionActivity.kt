package com.sachin_singh_dighan.newsapp.ui.countryselection

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.sachin_singh_dighan.newsapp.AppConstant
import com.sachin_singh_dighan.newsapp.NewsApplication
import com.sachin_singh_dighan.newsapp.data.model.countryselection.CountrySelection
import com.sachin_singh_dighan.newsapp.databinding.ActivityCountrySelectionBinding
import com.sachin_singh_dighan.newsapp.di.component.countryselection.DaggerCountrySelectionComponent
import com.sachin_singh_dighan.newsapp.di.module.countryselection.CountrySelectionModule
import com.sachin_singh_dighan.newsapp.ui.base.BaseActivity
import com.sachin_singh_dighan.newsapp.ui.common.UiState
import com.sachin_singh_dighan.newsapp.ui.dialog.ErrorDialog
import com.sachin_singh_dighan.newsapp.ui.news.NewsListActivity
import kotlinx.coroutines.launch
import javax.inject.Inject

class CountrySelectionActivity : BaseActivity<ActivityCountrySelectionBinding>() {

    @Inject
    lateinit var countrySelectionViewModel: CountrySelectionViewModel

    @Inject
    lateinit var adapter: CountrySelectionAdapter

    @Inject
    lateinit var errorDialog: ErrorDialog


    companion object {
        fun getInstance(context: Context): Intent {
            return Intent(context, CountrySelectionActivity::class.java)
        }
    }

    override fun injectDependencies() {
        DaggerCountrySelectionComponent.builder()
            .applicationComponent((application as NewsApplication).applicationComponent)
            .countrySelectionModule(CountrySelectionModule(this)).build().inject(this)
    }

    override fun setUpViewBinding(inflate: LayoutInflater): ActivityCountrySelectionBinding {
        return ActivityCountrySelectionBinding.inflate(layoutInflater)
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
    }

    override fun setupObserver() {
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
                            //Handle Error
                            binding.progressBar.visibility = View.GONE
                            errorDialog.showResetPasswordDialog(this@CountrySelectionActivity, it.message,)
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
        startActivity(
            NewsListActivity.getInstance(
                this@CountrySelectionActivity,
                newsType = AppConstant.NEWS_BY_COUNTRY,
                newsCountry = countryCode,

            )
        )
        finish()
    }
}
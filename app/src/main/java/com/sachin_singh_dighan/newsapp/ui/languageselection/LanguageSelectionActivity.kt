package com.sachin_singh_dighan.newsapp.ui.languageselection

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
import com.sachin_singh_dighan.newsapp.data.model.languageselection.LanguageData
import com.sachin_singh_dighan.newsapp.databinding.ActivityLanguageSelectionBinding
import com.sachin_singh_dighan.newsapp.di.component.languageselection.DaggerLanguageSelectionComponent
import com.sachin_singh_dighan.newsapp.di.module.languageselection.LanguageSelectionModule
import com.sachin_singh_dighan.newsapp.ui.base.BaseActivity
import com.sachin_singh_dighan.newsapp.ui.common.UiState
import com.sachin_singh_dighan.newsapp.ui.dialog.ErrorDialog
import com.sachin_singh_dighan.newsapp.ui.news.NewsListActivity
import kotlinx.coroutines.launch
import javax.inject.Inject

class LanguageSelectionActivity : BaseActivity<ActivityLanguageSelectionBinding>() {

    @Inject
    lateinit var languageSelectionViewModel: LanguageSelectionViewModel

    @Inject
    lateinit var adapter: LanguageSelectionAdapter

    @Inject
    lateinit var errorDialog: ErrorDialog

    companion object {
        const val BACK_PRESSED = "Back Pressed"
        fun getInstance(context: Context, isBackPressed: Boolean = false): Intent{
            return Intent(context, LanguageSelectionActivity::class.java).apply {
                putExtra(BACK_PRESSED, isBackPressed)
            }

        }
    }

    override fun injectDependencies() {
        DaggerLanguageSelectionComponent.builder()
            .applicationComponent((application as NewsApplication).applicationComponent)
            .languageSelectionModule(LanguageSelectionModule(this)).build().inject(this)
    }

    override fun setUpViewBinding(inflate: LayoutInflater): ActivityLanguageSelectionBinding {
        return ActivityLanguageSelectionBinding.inflate(layoutInflater)
    }



    override fun setupUI(savedInstanceState: Bundle?) {
        clearLanguageSelectedList()
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

    override fun setupObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                languageSelectionViewModel.uiState.collect() {
                    when (it) {
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
                            //Handle Error
                            binding.progressBar.visibility = View.GONE
                            errorDialog.showResetPasswordDialog(
                                this@LanguageSelectionActivity,
                                it.message,
                            )
                        }
                    }
                }
            }
        }
    }

    private fun clearLanguageSelectedList(){
        intent.extras?.apply {
            val isBackPressed = getBoolean(BACK_PRESSED)
            if(isBackPressed){
                languageSelectionViewModel.languageCodeSet.clear()
            }
        }

    }

    private fun renderList(languageData: List<LanguageData>) {
        adapter.addData(languageData)
    }

    fun onLanguageClick(languageCode: String) {
        languageSelectionViewModel.languageCodeSet.add(languageCode)
        val languageCodeList = arrayListOf<String>()
        languageCodeList.addAll(languageSelectionViewModel.languageCodeSet)
        if (languageCodeList.size == 2) {
            startActivity(
                NewsListActivity.getInstance(
                    this@LanguageSelectionActivity,
                    newsType = AppConstant.NEWS_BY_LANGUAGE,
                    newsLanguage = languageCodeList,
                )
            )
            finish()
        }
    }
}
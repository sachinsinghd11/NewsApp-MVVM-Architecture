package com.sachin_singh_dighan.newsapp.ui.languageselection

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.sachin_singh_dighan.newsapp.AppConstant
import com.sachin_singh_dighan.newsapp.data.model.languageselection.LanguageData
import com.sachin_singh_dighan.newsapp.databinding.ActivityLanguageSelectionBinding
import com.sachin_singh_dighan.newsapp.ui.base.BaseActivity
import com.sachin_singh_dighan.newsapp.ui.common.UiState
import com.sachin_singh_dighan.newsapp.ui.dialog.ErrorDialog
import com.sachin_singh_dighan.newsapp.ui.news.NewsListActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LanguageSelectionActivity :
    BaseActivity<LanguageSelectionViewModel, ActivityLanguageSelectionBinding>() {

    @Inject
    lateinit var adapter: LanguageSelectionAdapter

    @Inject
    lateinit var errorDialog: ErrorDialog

    companion object {
        const val BACK_PRESSED = "Back Pressed"
        fun getInstance(context: Context, isBackPressed: Boolean = false): Intent {
            return Intent(context, LanguageSelectionActivity::class.java).apply {
                putExtra(BACK_PRESSED, isBackPressed)
            }

        }
    }

    override fun setUpViewBinding(inflate: LayoutInflater): ActivityLanguageSelectionBinding {
        return ActivityLanguageSelectionBinding.inflate(layoutInflater)
    }

    override fun setUpViewModel() {
        viewModel = ViewModelProvider(this)[LanguageSelectionViewModel::class.java]
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
        adapter.itemClickListener = { _, languageCode ->
            viewModel.languageCodeSet.add(languageCode)
            val languageCodeList = arrayListOf<String>()
            languageCodeList.addAll(viewModel.languageCodeSet)
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

    @Suppress("UNCHECKED_CAST")
    override fun setupObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    when (it) {
                        is UiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            it.data?.let { newsList ->
                                renderList(newsList as List<LanguageData>)
                            }
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

    private fun clearLanguageSelectedList() {
        intent.extras?.apply {
            val isBackPressed = getBoolean(BACK_PRESSED)
            if (isBackPressed) {
                viewModel.languageCodeSet.clear()
            }
        }

    }

    private fun renderList(languageData: List<LanguageData>) {
        adapter.addData(languageData)
    }
}
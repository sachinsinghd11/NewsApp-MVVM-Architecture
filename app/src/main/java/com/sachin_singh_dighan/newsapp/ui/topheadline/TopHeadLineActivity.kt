package com.sachin_singh_dighan.newsapp.ui.topheadline

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.sachin_singh_dighan.newsapp.NewsApplication
import com.sachin_singh_dighan.newsapp.data.model.topheadline.Article
import com.sachin_singh_dighan.newsapp.databinding.ActivityTopHeadLineBinding
import com.sachin_singh_dighan.newsapp.di.component.topheadline.DaggerTopHeadLineComponent
import com.sachin_singh_dighan.newsapp.di.module.topheadline.TopHeadLineModule
import com.sachin_singh_dighan.newsapp.ui.base.BaseActivity
import com.sachin_singh_dighan.newsapp.ui.common.UiState
import com.sachin_singh_dighan.newsapp.ui.dialog.ErrorDialog
import kotlinx.coroutines.launch
import javax.inject.Inject

class TopHeadLineActivity : BaseActivity<ActivityTopHeadLineBinding>() {


    companion object {
        private const val HEADLINE_TYPE = "Headline type"
        fun getInstance(context: Context, headLineSource: String): Intent{
            return Intent(context, TopHeadLineActivity::class.java)
                .apply {
                    putExtra(HEADLINE_TYPE, headLineSource)
                }
        }
    }

    @Inject
    lateinit var newsListViewModel: TopHeadLineViewModel

    @Inject
    lateinit var adapter: TopHeadLineAdapter

    @Inject
    lateinit var errorDialog: ErrorDialog

    override fun injectDependencies() {
        DaggerTopHeadLineComponent.builder()
            .applicationComponent((application as NewsApplication).applicationComponent)
            .topHeadLineModule(TopHeadLineModule(this)).build().inject(this)
    }

    override fun setUpViewBinding(inflate: LayoutInflater): ActivityTopHeadLineBinding {
        return ActivityTopHeadLineBinding.inflate(layoutInflater)
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
    }

    override fun setupObserver() {
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
                            errorDialog.showResetPasswordDialog(this@TopHeadLineActivity, it.message,)
                        }
                    }
                }
            }
        }
    }

    private fun renderList(articleList:  List<Article>) {
        adapter.addData(articleList)
        adapter.notifyDataSetChanged()
    }
}
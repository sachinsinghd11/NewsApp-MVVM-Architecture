package com.sachin_singh_dighan.newsapp.ui.newsources

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
import com.sachin_singh_dighan.newsapp.AppConstant
import com.sachin_singh_dighan.newsapp.NewsApplication
import com.sachin_singh_dighan.newsapp.data.model.newsources.Sources
import com.sachin_singh_dighan.newsapp.databinding.ActivityNewSourcesBinding
import com.sachin_singh_dighan.newsapp.di.component.newsources.DaggerNewSourcesComponent
import com.sachin_singh_dighan.newsapp.di.module.newsources.NewSourcesModule
import com.sachin_singh_dighan.newsapp.ui.base.BaseActivity
import com.sachin_singh_dighan.newsapp.ui.common.UiState
import com.sachin_singh_dighan.newsapp.ui.dialog.ErrorDialog
import com.sachin_singh_dighan.newsapp.ui.news.NewsListActivity
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewsSourcesActivity : BaseActivity<ActivityNewSourcesBinding>() {

    @Inject
    lateinit var newSourceListViewModel: NewsSourcesViewModel

    @Inject
    lateinit var adapter: NewsSourcesAdapter

    @Inject
    lateinit var errorDialog: ErrorDialog

    companion object {
        fun getInstance(context: Context): Intent {
            return Intent(context, NewsSourcesActivity::class.java)
        }
    }

    override fun injectDependencies() {
        DaggerNewSourcesComponent.builder()
            .applicationComponent((application as NewsApplication).applicationComponent)
            .newSourcesModule(NewSourcesModule(this)).build().inject(this)
    }

    override fun setUpViewBinding(inflate: LayoutInflater): ActivityNewSourcesBinding {
       return ActivityNewSourcesBinding.inflate(layoutInflater)
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
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                newSourceListViewModel.uiState.collect {
                    when (it) {
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
                            errorDialog.showResetPasswordDialog(this@NewsSourcesActivity, it.message,)
                        }
                    }
                }
            }
        }
    }

    private fun renderList(sourceList: List<Sources>) {
        adapter.addData(sourceList)
        adapter.notifyDataSetChanged()
    }

    fun onNewSourceItemClick(sourceClicked: Sources) {
        startActivity(
            NewsListActivity.getInstance(
                this@NewsSourcesActivity,
                newsType = AppConstant.NEWS_BY_SOURCE,
                newsSource = sourceClicked.id,
            )
        )
        finish()
    }
}
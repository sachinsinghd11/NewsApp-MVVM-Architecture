package com.sachin_singh_dighan.newsapp.ui.newsources

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
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
import com.sachin_singh_dighan.newsapp.ui.base.UiState
import com.sachin_singh_dighan.newsapp.ui.dialog.ErrorDialog
import com.sachin_singh_dighan.newsapp.ui.news.NewsListActivity
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewSourcesActivity : AppCompatActivity() {

    @Inject
    lateinit var newSourceListViewModel: NewSourcesViewModel

    @Inject
    lateinit var adapter: NewSourcesAdapter

    @Inject
    lateinit var errorDialog: ErrorDialog

    private lateinit var binding: ActivityNewSourcesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        super.onCreate(savedInstanceState)
        binding = ActivityNewSourcesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUI()
        setUpObserver()
    }

    private fun setUpObserver() {
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
                            errorDialog.showResetPasswordDialog(this@NewSourcesActivity, it.message,)
                        }
                    }
                }
            }
        }
    }

    private fun setupUI() {
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

    private fun injectDependencies() {
        DaggerNewSourcesComponent.builder()
            .applicationComponent((application as NewsApplication).applicationComponent)
            .newSourcesModule(NewSourcesModule(this)).build().inject(this)
    }

    private fun renderList(sourceList: List<Sources>) {
        adapter.addData(sourceList)
        adapter.notifyDataSetChanged()
    }

    fun onNewSourceItemClick(sourceClicked: Sources) {
        startActivity(
            NewsListActivity.getInstance(
                this@NewSourcesActivity,
                newsType = AppConstant.NEWS_BY_SOURCE,
                newsSource = sourceClicked.id,
            )
        )
    }
}
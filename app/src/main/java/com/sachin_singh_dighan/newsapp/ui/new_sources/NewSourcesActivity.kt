package com.sachin_singh_dighan.newsapp.ui.new_sources

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.sachin_singh_dighan.newsapp.NewsApplication
import com.sachin_singh_dighan.newsapp.data.model.new_sources.Sources
import com.sachin_singh_dighan.newsapp.databinding.ActivityNewSourcesBinding
import com.sachin_singh_dighan.newsapp.di.component.new_sources.DaggerNewSourcesComponent
import com.sachin_singh_dighan.newsapp.di.module.new_sources.NewSourcesModule
import com.sachin_singh_dighan.newsapp.ui.base.UiState
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewSourcesActivity : AppCompatActivity() {

    @Inject
    lateinit var newSourceListViewModel: NewSourcesViewModel

    @Inject
    lateinit var adapter: NewSourcesAdapter

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
                            Toast.makeText(this@NewSourcesActivity, it.message, Toast.LENGTH_LONG)
                                .show()
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

    fun onNewSourceItemClick(sourceClicked: Sources){
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(sourceClicked.url))
    }
}
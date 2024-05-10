package com.sachin_singh_dighan.newsapp.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<viewBinding: ViewBinding> : AppCompatActivity() {

    private var _binding: viewBinding? = null

    val binding get() = requireNotNull(_binding)
    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        super.onCreate(savedInstanceState)
        _binding = setUpViewBinding(layoutInflater)
        setContentView(requireNotNull(_binding).root)
        setupUI(savedInstanceState)
        setupObserver()
    }

    abstract fun setUpViewBinding(inflate: LayoutInflater) : viewBinding
    abstract fun injectDependencies()
    abstract fun setupUI(savedInstanceState: Bundle?)
    abstract fun setupObserver()
    override fun onDestroy() {
        _binding = null
        super.onDestroy()

    }
}
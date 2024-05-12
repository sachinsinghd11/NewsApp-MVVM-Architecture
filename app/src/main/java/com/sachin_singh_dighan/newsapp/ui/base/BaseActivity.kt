package com.sachin_singh_dighan.newsapp.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<T : BaseViewModel<*>, viewBinding : ViewBinding> :
    AppCompatActivity() {

    lateinit var viewModel: T

    private var _binding: viewBinding? = null

    val binding get() = requireNotNull(_binding)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = setUpViewBinding(layoutInflater)
        setContentView(requireNotNull(_binding).root)
        setUpViewModel()
        setupUI(savedInstanceState)
        setupObserver()
    }

    abstract fun setUpViewBinding(inflate: LayoutInflater): viewBinding
    abstract fun setUpViewModel()
    abstract fun setupUI(savedInstanceState: Bundle?)
    abstract fun setupObserver()
    override fun onDestroy() {
        _binding = null
        super.onDestroy()

    }
}
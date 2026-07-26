package com.sachin_singh_dighan.newsapp.utils.logger

interface Logger {

    fun d(tag: String, message: String)

    fun e(tag: String, message: String)

    fun w(tag: String, message: String)
}
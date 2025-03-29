package com.sachin_singh_dighan.newsapp.data.repository.languageselection

import com.sachin_singh_dighan.newsapp.utils.AppConstant
import com.sachin_singh_dighan.newsapp.data.model.languageselection.LanguageData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

import javax.inject.Inject

class LanguageSelectionRepository  @Inject constructor(){

    fun getLanguageData(): Flow<List<LanguageData>> {
        return flow {
            emit(AppConstant.LANGUAGE_LIST)
        }
    }
}
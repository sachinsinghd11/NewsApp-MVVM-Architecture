package com.sachin_singh_dighan.newsapp.data.repository.languageselection

import com.sachin_singh_dighan.newsapp.AppConstant
import com.sachin_singh_dighan.newsapp.data.model.languageselection.LanguageData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

import javax.inject.Inject

class LanguageSelectionRepository  @Inject constructor(){

    fun getLanguageData(): Flow<List<LanguageData>> {
        val languageDataList = mutableListOf<LanguageData>()
        return flow {
            languageDataList.add(LanguageData(AppConstant.ARABIC_CODE, AppConstant.ARABIC))
            languageDataList.add(LanguageData(AppConstant.GERMAN_CODE, AppConstant.GERMAN))
            languageDataList.add(LanguageData(AppConstant.ENGLISH_CODE, AppConstant.ENGLISH))
            languageDataList.add(LanguageData(AppConstant.SPANISH_CODE, AppConstant.SPANISH))
            languageDataList.add(LanguageData(AppConstant.FRENCH_CODE, AppConstant.FRENCH))
            languageDataList.add(LanguageData(AppConstant.HEBREW_CODE, AppConstant.HEBREW))
            languageDataList.add(LanguageData(AppConstant.ITALIAN_CODE, AppConstant.ITALIAN))
            languageDataList.add(LanguageData(AppConstant.DUTCH_CODE, AppConstant.DUTCH))
            languageDataList.add(LanguageData(AppConstant.NORWEGIAN_CODE, AppConstant.NORWEGIAN))
            languageDataList.add(LanguageData(AppConstant.PORTUGUESE_CODE, AppConstant.PORTUGUESE))
            languageDataList.add(LanguageData(AppConstant.RUSSIAN_CODE, AppConstant.RUSSIAN))
            languageDataList.add(LanguageData(AppConstant.SWEDISH_CODE, AppConstant.SWEDISH))
            languageDataList.add(LanguageData(AppConstant.UDMURT_CODE, AppConstant.UDMURT))
            languageDataList.add(LanguageData(AppConstant.CHINESE_CODE, AppConstant.CHINESE))
            emit(languageDataList)
        }
    }
}
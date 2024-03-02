package com.sachin_singh_dighan.newsapp.data.repository.mainscreen

import com.sachin_singh_dighan.newsapp.AppConstant
import com.sachin_singh_dighan.newsapp.data.model.mainscreen.MainSection
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MainRepository @Inject constructor(){
    fun getDataForMainSection(): Flow<List<MainSection>>{
        return flow {
            val mainSectionList = mutableListOf<MainSection>()
            mainSectionList.add(MainSection(AppConstant.TOP_HEADLINES))
            mainSectionList.add(MainSection(AppConstant.NEWS_SOURCES))
            mainSectionList.add(MainSection(AppConstant.COUNTRIES))
            mainSectionList.add(MainSection(AppConstant.LANGUAGES))
            mainSectionList.add(MainSection(AppConstant.SEARCH))
            emit(mainSectionList)
        }
    }
}
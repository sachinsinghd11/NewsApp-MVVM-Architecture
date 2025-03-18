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
            emit(AppConstant.MAIN_SECTION_LIST)
        }
    }
}
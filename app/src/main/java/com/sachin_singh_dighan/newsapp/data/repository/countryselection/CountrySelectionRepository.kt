package com.sachin_singh_dighan.newsapp.data.repository.countryselection

import com.sachin_singh_dighan.newsapp.AppConstant
import com.sachin_singh_dighan.newsapp.data.model.countryselection.CountrySelection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CountrySelectionRepository @Inject constructor(){

    fun getDataForCountry(): Flow<List<CountrySelection>> {
        return flow {
            emit(AppConstant.COUNTRY_LIST)
        }
    }
}
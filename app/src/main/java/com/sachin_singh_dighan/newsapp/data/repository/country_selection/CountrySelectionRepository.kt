package com.sachin_singh_dighan.newsapp.data.repository.country_selection

import com.sachin_singh_dighan.newsapp.AppConstant
import com.sachin_singh_dighan.newsapp.data.model.country_selection.CountrySelection
import com.sachin_singh_dighan.newsapp.data.model.main_screen.MainSection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CountrySelectionRepository @Inject constructor(){

    fun getDataForCountry(): Flow<List<CountrySelection>> {
        val countrySelectionList = mutableListOf<CountrySelection>()
        return flow {
            countrySelectionList.add(CountrySelection(AppConstant.UNITED_ARAB_EMIRATES_CODE, AppConstant.UNITED_ARAB_EMIRATES))
            countrySelectionList.add(CountrySelection(AppConstant.ARGENTINA_CODE, AppConstant.ARGENTINA))
            countrySelectionList.add(CountrySelection(AppConstant.AUSTRIA_CODE, AppConstant.AUSTRIA))
            countrySelectionList.add(CountrySelection(AppConstant.AUSTRALIA_CODE, AppConstant.AUSTRALIA))
            countrySelectionList.add(CountrySelection(AppConstant.BELGIUM_CODE, AppConstant.BELGIUM))
            countrySelectionList.add(CountrySelection(AppConstant.BULGARIA_CODE, AppConstant.BULGARIA))
            countrySelectionList.add(CountrySelection(AppConstant.BRAZIL_CODE, AppConstant.BRAZIL))
            countrySelectionList.add(CountrySelection(AppConstant.CANADA_CODE, AppConstant.CANADA))
            countrySelectionList.add(CountrySelection(AppConstant.SWITZERLAND_CODE, AppConstant.SWITZERLAND))
            countrySelectionList.add(CountrySelection(AppConstant.CHINA_CODE, AppConstant.CHINA))
            countrySelectionList.add(CountrySelection(AppConstant.COLOMBIA_CODE, AppConstant.COLOMBIA))
            countrySelectionList.add(CountrySelection(AppConstant.CUBA_CODE, AppConstant.CUBA))
            countrySelectionList.add(CountrySelection(AppConstant.CZECH_REPUBLIC_CODE, AppConstant.CZECH_REPUBLIC))
            countrySelectionList.add(CountrySelection(AppConstant.GERMANY_CODE, AppConstant.GERMANY))
            countrySelectionList.add(CountrySelection(AppConstant.EGYPT_CODE, AppConstant.EGYPT))
            countrySelectionList.add(CountrySelection(AppConstant.FRANCE_CODE, AppConstant.FRANCE))
            countrySelectionList.add(CountrySelection(AppConstant.UNITED_KINGDOM_CODE, AppConstant.UNITED_KINGDOM))
            countrySelectionList.add(CountrySelection(AppConstant.GREECE_CODE, AppConstant.GREECE))
            countrySelectionList.add(CountrySelection(AppConstant.HONG_KONG_CODE, AppConstant.HONG_KONG))
            countrySelectionList.add(CountrySelection(AppConstant.HUNGARY_CODE, AppConstant.HUNGARY))
            countrySelectionList.add(CountrySelection(AppConstant.INDONESIA_CODE, AppConstant.INDONESIA))
            countrySelectionList.add(CountrySelection(AppConstant.IRELAND_CODE, AppConstant.IRELAND))
            countrySelectionList.add(CountrySelection(AppConstant.ISRAEL_CODE, AppConstant.ISRAEL))
            countrySelectionList.add(CountrySelection(AppConstant.INDIA_CODE, AppConstant.INDIA))
            countrySelectionList.add(CountrySelection(AppConstant.ITALY_CODE, AppConstant.ITALY))
            countrySelectionList.add(CountrySelection(AppConstant.JAPAN_CODE, AppConstant.JAPAN))
            countrySelectionList.add(CountrySelection(AppConstant.KOREA_REPUBLIC_CODE, AppConstant.KOREA_REPUBLIC))
            countrySelectionList.add(CountrySelection(AppConstant.LITHUANIA_CODE, AppConstant.LITHUANIA))
            countrySelectionList.add(CountrySelection(AppConstant.LATVIA_CODE, AppConstant.LATVIA))
            countrySelectionList.add(CountrySelection(AppConstant.MOROCCO_CODE, AppConstant.MOROCCO))
            countrySelectionList.add(CountrySelection(AppConstant.MEXICO_CODE, AppConstant.MEXICO))
            countrySelectionList.add(CountrySelection(AppConstant.MALAYSIA_CODE, AppConstant.MALAYSIA))
            countrySelectionList.add(CountrySelection(AppConstant.NIGERIA_CODE, AppConstant.NIGERIA))
            countrySelectionList.add(CountrySelection(AppConstant.NETHERLANDS_CODE, AppConstant.NETHERLANDS))
            countrySelectionList.add(CountrySelection(AppConstant.NORWAY_CODE, AppConstant.NORWAY))
            countrySelectionList.add(CountrySelection(AppConstant.NEW_ZEALAND_CODE, AppConstant.NEW_ZEALAND))
            countrySelectionList.add(CountrySelection(AppConstant.PHILIPPINES_CODE, AppConstant.PHILIPPINES))
            countrySelectionList.add(CountrySelection(AppConstant.POLAND_CODE, AppConstant.POLAND))
            countrySelectionList.add(CountrySelection(AppConstant.PORTUGAL_CODE, AppConstant.PORTUGAL))
            countrySelectionList.add(CountrySelection(AppConstant.ROMANIA_CODE, AppConstant.ROMANIA))
            countrySelectionList.add(CountrySelection(AppConstant.SERBIA_CODE, AppConstant.SERBIA))
            countrySelectionList.add(CountrySelection(AppConstant.RUSSIAN_FEDERATION_CODE, AppConstant.RUSSIAN_FEDERATION))
            countrySelectionList.add(CountrySelection(AppConstant.SAUDI_ARABIA_CODE, AppConstant.SAUDI_ARABIA))
            countrySelectionList.add(CountrySelection(AppConstant.SWEDEN_CODE, AppConstant.SWEDEN))
            countrySelectionList.add(CountrySelection(AppConstant.SINGAPORE_CODE, AppConstant.SINGAPORE))
            countrySelectionList.add(CountrySelection(AppConstant.SLOVENIA_CODE, AppConstant.SLOVENIA))
            countrySelectionList.add(CountrySelection(AppConstant.SLOVAKIA_SLOVAK_REPUBLIC_CODE, AppConstant.SLOVAKIA_SLOVAK_REPUBLIC))
            countrySelectionList.add(CountrySelection(AppConstant.THAILAND_CODE, AppConstant.THAILAND))
            countrySelectionList.add(CountrySelection(AppConstant.TURKEY_CODE, AppConstant.TURKEY))
            countrySelectionList.add(CountrySelection(AppConstant.TAIWAN_CODE, AppConstant.TAIWAN))
            countrySelectionList.add(CountrySelection(AppConstant.UKRAINE_CODE, AppConstant.UKRAINE))
            countrySelectionList.add(CountrySelection(AppConstant.UNITED_STATES_CODE, AppConstant.UNITED_STATES))
            countrySelectionList.add(CountrySelection(AppConstant.VENEZUELA_CODE, AppConstant.VENEZUELA))
            countrySelectionList.add(CountrySelection(AppConstant.SOUTH_AFRICA_CODE, AppConstant.SOUTH_AFRICA))
            emit(countrySelectionList)
        }
    }
}
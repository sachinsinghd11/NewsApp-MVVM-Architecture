package com.sachin_singh_dighan.newsapp

import com.sachin_singh_dighan.newsapp.data.model.countryselection.CountrySelection
import com.sachin_singh_dighan.newsapp.data.model.languageselection.LanguageData

object AppConstant {

    //for main screen
    const val TOP_HEADLINES = "Top Headlines"
    const val NEWS_SOURCES = "News Sources"
    const val CHANNELS = "Channels"
    const val COUNTRIES = "Countries"
    const val LANGUAGES = "Languages"
    const val SEARCH = "Search"
    const val NEWS_LIST = "news_list"

    //for network service
    const val API_KEY = "5e59e08053df4d13a5d4ff6fb1b228d0"

    //keys for distinguishing between news type
    const val NEWS_BY_DEFAULT = "us"
    const val NEWS_BY_CATEGORY_VALUE = "category"
    const val NEWS_BY_CHANNEL_VALUE = "channel"
    const val NEWS_BY_COUNTRY_VALUE = "country"
    const val NEWS_BY_LANGUAGE_VALUE = "language"

    const val NEWS_TYPE_CODE = "news_type_code"

    //Network error msg
    const val NETWORK_ERROR = "Please Check Network Connection"

    const val NO_DATA_FOUND = "No Data Found"

    //argument for newsList
    const val ARGUMENT_VALUE = "argument_value"

    //argument for countrycode
    const val NEW_BY_COUNTRY_CODE = "news_by_country_code"

    //argument for newssourcecode
    const val NEW_BY_NEWS_SOURCE_CODE = "news_by_news_source_code"

    //argument for launguage
    const val NEW_BY_LANGUAGE_CODE = "news_by_language_code"

    //Country name with code

    private const val UNITED_ARAB_EMIRATES = "United Arab Emirates"
    private const val UNITED_ARAB_EMIRATES_CODE = "ae"
    private const val ARGENTINA = "Argentina"
    private const val ARGENTINA_CODE = "ar"
    private const val AUSTRIA = "Austria"
    private const val AUSTRIA_CODE = "at"
    private const val AUSTRALIA = "Australia"
    private const val AUSTRALIA_CODE = "au"
    private const val BELGIUM = "Belgium"
    private const val BELGIUM_CODE = "be"
    private const val BULGARIA = "Bulgaria"
    private const val BULGARIA_CODE = "bg"
    private const val BRAZIL = "Brazil"
    private const val BRAZIL_CODE = "br"
    private const val CANADA = "Canada"
    private const val CANADA_CODE = "ca"
    private const val SWITZERLAND = "Switzerland"
    private const val SWITZERLAND_CODE = "ch"
    private const val CHINA = "China"
    private const val CHINA_CODE = "cn"
    private const val COLOMBIA = "Colombia"
    private const val COLOMBIA_CODE = "co"
    private const val CUBA = "Cuba"
    private const val CUBA_CODE = "cu"
    private const val CZECH_REPUBLIC = "Czech Republic"
    private const val CZECH_REPUBLIC_CODE = "cz"
    private const val GERMANY = "Germany"
    private const val GERMANY_CODE = "de"
    private const val EGYPT = "Egypt"
    private const val EGYPT_CODE = "eg"
    private const val FRANCE = "France"
    private const val FRANCE_CODE = "fr"
    private const val UNITED_KINGDOM = "United Kingdom"
    private const val UNITED_KINGDOM_CODE = "gb"
    private const val GREECE = "Greece"
    private const val GREECE_CODE = "gr"
    private const val HONG_KONG = "Hong Kong"
    private const val HONG_KONG_CODE = "hk"
    private const val HUNGARY = "Hungary"
    private const val HUNGARY_CODE = "hu"
    private const val INDONESIA = "Indonesia"
    private const val INDONESIA_CODE = "id"
    private const val IRELAND = "Ireland"
    private const val IRELAND_CODE = "ie"
    private const val ISRAEL = "Israel"
    private const val ISRAEL_CODE = "il"
    private const val INDIA = "India"
    private const val INDIA_CODE = "in"
    private const val ITALY = "Italy"
    private const val ITALY_CODE = "it"
    private const val JAPAN = "Japan"
    private const val JAPAN_CODE = "jp"
    private const val KOREA_REPUBLIC = "Korea, Republic"
    private const val KOREA_REPUBLIC_CODE = "kr"
    private const val LITHUANIA = "Lithuania"
    private const val LITHUANIA_CODE = "lt"
    private const val LATVIA = "Latvia"
    private const val LATVIA_CODE = "lv"
    private const val MOROCCO = "Morocco"
    private const val MOROCCO_CODE = "ma"
    private const val MEXICO = "Mexico"
    private const val MEXICO_CODE = "mx"
    private const val MALAYSIA = "Malaysia"
    private const val MALAYSIA_CODE = "my"
    private const val NIGERIA = "Nigeria"
    private const val NIGERIA_CODE = "ng"
    private const val NETHERLANDS = "Netherlands"
    private const val NETHERLANDS_CODE = "nl"
    private const val NORWAY = "Norway"
    private const val NORWAY_CODE = "no"
    private const val NEW_ZEALAND = "New Zealand"
    private const val NEW_ZEALAND_CODE = "nz"
    private const val PHILIPPINES = "Philippines"
    private const val PHILIPPINES_CODE = "ph"
    private const val POLAND = "Poland"
    private const val POLAND_CODE = "pl"
    private const val PORTUGAL = "Portugal"
    private const val PORTUGAL_CODE = "pt"
    private const val ROMANIA = "Romania"
    private const val ROMANIA_CODE = "ro"
    private const val SERBIA = "Serbia"
    private const val SERBIA_CODE = "rs"
    private const val RUSSIAN_FEDERATION = "Russian Federation"
    private const val RUSSIAN_FEDERATION_CODE = "ru"
    private const val SAUDI_ARABIA = "Saudi Arabia"
    private const val SAUDI_ARABIA_CODE = "sa"
    private const val SWEDEN = "Sweden"
    private const val SWEDEN_CODE = "se"
    private const val SINGAPORE = "Singapore"
    private const val SINGAPORE_CODE = "sg"
    private const val SLOVENIA = "Slovenia"
    private const val SLOVENIA_CODE = "si"
    private const val SLOVAKIA_SLOVAK_REPUBLIC = "Slovakia (Slovak Republic)"
    private const val SLOVAKIA_SLOVAK_REPUBLIC_CODE = "sk"
    private const val THAILAND = "Thailand"
    private const val THAILAND_CODE = "th"
    private const val TURKEY = "Turkey"
    private const val TURKEY_CODE = "tr"
    private const val TAIWAN = "Taiwan"
    private const val TAIWAN_CODE = "tw"
    private const val UKRAINE = "Ukraine"
    private const val UKRAINE_CODE = "ua"
    private const val UNITED_STATES = "United States"
    private const val UNITED_STATES_CODE = "us"
    private const val VENEZUELA = "Venezuela"
    private const val VENEZUELA_CODE = "ve"
    private const val SOUTH_AFRICA = "South Africa"
    private const val SOUTH_AFRICA_CODE = "za"

    val COUNTRY_LIST = listOf(
        CountrySelection(UNITED_ARAB_EMIRATES_CODE, UNITED_ARAB_EMIRATES),
        CountrySelection(ARGENTINA_CODE, ARGENTINA),
        CountrySelection(AUSTRIA_CODE, AUSTRIA),
        CountrySelection(AUSTRALIA_CODE, AUSTRALIA),
        CountrySelection(BELGIUM_CODE, BELGIUM),
        CountrySelection(BULGARIA_CODE, BULGARIA),
        CountrySelection(BRAZIL_CODE, BRAZIL),
        CountrySelection(CANADA_CODE, CANADA),
        CountrySelection(SWITZERLAND_CODE, SWITZERLAND),
        CountrySelection(CHINA_CODE, CHINA),
        CountrySelection(COLOMBIA_CODE, COLOMBIA),
        CountrySelection(CUBA_CODE, CUBA),
        CountrySelection(CZECH_REPUBLIC_CODE, CZECH_REPUBLIC),
        CountrySelection(GERMANY_CODE, GERMANY),
        CountrySelection(EGYPT_CODE, EGYPT),
        CountrySelection(FRANCE_CODE, FRANCE),
        CountrySelection(UNITED_KINGDOM_CODE, UNITED_KINGDOM),
        CountrySelection(GREECE_CODE, GREECE),
        CountrySelection(HONG_KONG_CODE, HONG_KONG),
        CountrySelection(HUNGARY_CODE, HUNGARY),
        CountrySelection(INDONESIA_CODE, INDONESIA),
        CountrySelection(IRELAND_CODE, IRELAND),
        CountrySelection(ISRAEL_CODE, ISRAEL),
        CountrySelection(INDIA_CODE, INDIA),
        CountrySelection(ITALY_CODE, ITALY),
        CountrySelection(JAPAN_CODE, JAPAN),
        CountrySelection(KOREA_REPUBLIC_CODE, KOREA_REPUBLIC),
        CountrySelection(LITHUANIA_CODE, LITHUANIA),
        CountrySelection(LATVIA_CODE, LATVIA),
        CountrySelection(MOROCCO_CODE, MOROCCO),
        CountrySelection(MEXICO_CODE, MEXICO),
        CountrySelection(MALAYSIA_CODE, MALAYSIA),
        CountrySelection(NIGERIA_CODE, NIGERIA),
        CountrySelection(NETHERLANDS_CODE, NETHERLANDS),
        CountrySelection(NORWAY_CODE, NORWAY),
        CountrySelection(NEW_ZEALAND_CODE, NEW_ZEALAND),
        CountrySelection(PHILIPPINES_CODE, PHILIPPINES),
        CountrySelection(POLAND_CODE, POLAND),
        CountrySelection(PORTUGAL_CODE, PORTUGAL),
        CountrySelection(ROMANIA_CODE, ROMANIA),
        CountrySelection(SERBIA_CODE, SERBIA),
        CountrySelection(RUSSIAN_FEDERATION_CODE, RUSSIAN_FEDERATION),
        CountrySelection(SAUDI_ARABIA_CODE, SAUDI_ARABIA),
        CountrySelection(SWEDEN_CODE, SWEDEN),
        CountrySelection(SINGAPORE_CODE, SINGAPORE),
        CountrySelection(SLOVENIA_CODE, SLOVENIA),
        CountrySelection(SLOVAKIA_SLOVAK_REPUBLIC_CODE, SLOVAKIA_SLOVAK_REPUBLIC),
        CountrySelection(THAILAND_CODE, THAILAND),
        CountrySelection(TURKEY_CODE, TURKEY),
        CountrySelection(TAIWAN_CODE, TAIWAN),
        CountrySelection(UKRAINE_CODE, UKRAINE),
        CountrySelection(UNITED_STATES_CODE, UNITED_STATES),
        CountrySelection(VENEZUELA_CODE, VENEZUELA),
        CountrySelection(SOUTH_AFRICA_CODE, SOUTH_AFRICA),
    )


    //Languages
    private const val ARABIC = "Arabic"
    private const val ARABIC_CODE = "ar"
    private const val GERMAN = "German"
    private const val GERMAN_CODE = "de"
    private const val ENGLISH = "English"
    private const val ENGLISH_CODE = "en"
    private const val SPANISH = "Spanish"
    private const val SPANISH_CODE = "es"
    private const val FRENCH = "French"
    private const val FRENCH_CODE = "fr"
    private const val HEBREW = "Hebrew"
    private const val HEBREW_CODE = "he"
    private const val ITALIAN = "Italian"
    private const val ITALIAN_CODE = "it"
    private const val DUTCH = "Dutch"
    private const val DUTCH_CODE = "nl"
    private const val NORWEGIAN = "Norwegian"
    private const val NORWEGIAN_CODE = "no"
    private const val PORTUGUESE = "Portuguese"
    private const val PORTUGUESE_CODE = "pt"
    private const val RUSSIAN = "Russian"
    private const val RUSSIAN_CODE = "ru"
    private const val SWEDISH = "Swedish"
    private const val SWEDISH_CODE = "sv"
    private const val UDMURT = "Udmurt"
    private const val UDMURT_CODE = "ud"
    private const val CHINESE = "Chinese"
    private const val CHINESE_CODE = "zh"

    val LANGUAGE_LIST = listOf(
        LanguageData(ARABIC_CODE, ARABIC),
        LanguageData(GERMAN_CODE, GERMAN),
        LanguageData(ENGLISH_CODE, ENGLISH),
        LanguageData(SPANISH_CODE, SPANISH),
        LanguageData(FRENCH_CODE, FRENCH),
        LanguageData(HEBREW_CODE, HEBREW),
        LanguageData(ITALIAN_CODE, ITALIAN),
        LanguageData(DUTCH_CODE, DUTCH),
        LanguageData(NORWEGIAN_CODE, NORWEGIAN),
        LanguageData(PORTUGUESE_CODE, PORTUGUESE),
        LanguageData(RUSSIAN_CODE, RUSSIAN),
        LanguageData(SWEDISH_CODE, SWEDISH),
        LanguageData(UDMURT_CODE, UDMURT),
        LanguageData(CHINESE_CODE, CHINESE),
    )
}
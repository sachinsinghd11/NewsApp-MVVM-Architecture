package com.sachin_singh_dighan.newsapp.ui.base


import android.content.Context
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sachin_singh_dighan.newsapp.utils.AppConstant
import com.sachin_singh_dighan.newsapp.ui.countryselection.CountrySelectionRoute
import com.sachin_singh_dighan.newsapp.ui.languageselection.LanguageSelectionRoute
import com.sachin_singh_dighan.newsapp.ui.mainscreen.MainScreenRoute
import com.sachin_singh_dighan.newsapp.ui.news.NewsListRoute
import com.sachin_singh_dighan.newsapp.ui.newsources.NewsSourceRoute
import com.sachin_singh_dighan.newsapp.ui.offlinearticle.OfflineArticleRoute
import com.sachin_singh_dighan.newsapp.ui.searchnews.SearchNewsScreenRoute
import com.sachin_singh_dighan.newsapp.ui.topheadline.TopHeadlinesRoute
import com.sachin_singh_dighan.newsapp.ui.topheadlinebypaging.TopHeadlinePaginationRoute

sealed class Route(val name: String) {
    data object MainScreen : Route("MainScreen")
    data object TopHeadlines : Route("TopHeadlines")
    data object NewsSources : Route("NewsSources")
    data object CountrySelection : Route("CountrySelection")
    data object LanguageSelection : Route("LanguageSelection")
    data object SearchNews : Route("SearchNews")
    data object NewsList : Route("NewsList/{value}/{valueList}/{type}") {
        fun createRoute(value: String = "", valueList: String = "", type: String) =
            "NewsList/$value/${valueList}/$type"
    }

    data object OfflineArticle : Route("OfflineArticle")
    data object TopHeadlinesPagination : Route("TopHeadlinesPagination")
    //data object WorkMangerOneTimeRequest : Route("WMOneTimeRequest")
    //data object WorkMangerPeriodicRequest : Route("WMPeriodicRequest")
}

@Composable
fun NewsNavHost() {
    val context = LocalContext.current
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.MainScreen.name
    ) {
        composable(route = Route.MainScreen.name) {
            MainScreenRoute(
                onClick = { destination ->
                    when (destination) {
                        AppConstant.TOP_HEADLINES -> {
                            navController.navigate(route = Route.TopHeadlines.name)
                        }

                        AppConstant.NEWS_SOURCES -> {
                            navController.navigate(route = Route.NewsSources.name)
                        }

                        AppConstant.COUNTRIES -> {
                            navController.navigate(route = Route.CountrySelection.name)
                        }

                        AppConstant.LANGUAGES -> {
                            navController.navigate(route = Route.LanguageSelection.name)
                        }

                        AppConstant.SEARCH -> {
                            navController.navigate(route = Route.SearchNews.name)
                        }

                        AppConstant.OFFLINE_ARTICLE -> {
                            navController.navigate(route = Route.OfflineArticle.name)
                        }

                        AppConstant.PAGINATION_SCREEN -> {
                            navController.navigate(route = Route.TopHeadlinesPagination.name)
                        }
                        /*AppConstant.WORK_MANAGER_ONE_TIME_REQUEST -> {
                            viewModel.scheduleOneTimeSync()
                            //navController.navigate(route = Route.WorkMangerOneTimeRequest.name)
                        }
                        AppConstant.WORK_MANAGER_PERIODIC_REQUEST -> {
                            viewModel.scheduleDailySync()
                            //navController.navigate(route = Route.WorkMangerPeriodicRequest.name)
                        }*/
                    }
                }
            )
        }
        composable(route = Route.TopHeadlines.name) {
            TopHeadlinesRoute(
                onNewsClick = { url ->
                    opeCustomChromeTab(context, url)
                }
            )
        }
        composable(route = Route.NewsSources.name) {
            NewsSourceRoute(
                onClick = { source ->
                    navController.navigate(
                        Route.NewsList.createRoute(
                            value = source,
                            type = AppConstant.NEWS_BY_CATEGORY_VALUE
                        )
                    )

                }
            )
        }
        composable(route = Route.CountrySelection.name) {
            CountrySelectionRoute(
                onClick = { country ->
                    navController.navigate(
                        Route.NewsList.createRoute(
                            value = country,
                            type = AppConstant.NEWS_BY_COUNTRY_VALUE
                        )
                    )
                }
            )
        }
        composable(route = Route.LanguageSelection.name) {
            LanguageSelectionRoute(
                onClick = { language ->
                    val itemsString = language.joinToString(",")
                    navController.navigate(
                        Route.NewsList.createRoute(
                            valueList = itemsString,
                            type = AppConstant.NEWS_BY_LANGUAGE_VALUE
                        )
                    )
                }
            )
        }
        composable(route = Route.SearchNews.name) {
            SearchNewsScreenRoute(
                onNewsClick = { url ->
                    opeCustomChromeTab(context, url)
                }
            )

        }
        composable(
            route = Route.NewsList.name,
            arguments = listOf(
                navArgument("value") { type = NavType.StringType },
                navArgument("valueList") { type = NavType.StringType },
                navArgument("type") { type = NavType.StringType }
            )) {
            val fetchNewByType = it.arguments?.getString("value") ?: ""
            val valueListString = it.arguments?.getString("valueList") ?: ""
            val selectedLanguages = valueListString.split(",")
            val newType = it.arguments?.getString("type") ?: ""
            NewsListRoute(
                fetchNewByType,
                selectedLanguages,
                newType,
                onNewsClick = { url ->
                    opeCustomChromeTab(context, url)
                }
            )
        }
        composable(route = Route.OfflineArticle.name) {
            OfflineArticleRoute(
                onNewsClick = { url ->
                    opeCustomChromeTab(context, url)
                }
            )
        }
        composable(route = Route.TopHeadlinesPagination.name) {
            TopHeadlinePaginationRoute(
                onNewsClick = { url ->
                    opeCustomChromeTab(context, url)
                }
            )
        }
    }
}

fun opeCustomChromeTab(context: Context, url: String) {
    val builder = CustomTabsIntent.Builder()
    val customTabsIntent = builder.build()
    customTabsIntent.launchUrl(context, url.toUri())
}

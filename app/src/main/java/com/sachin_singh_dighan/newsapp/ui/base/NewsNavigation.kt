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
import com.sachin_singh_dighan.newsapp.AppConstant
import com.sachin_singh_dighan.newsapp.ui.countryselection.CountrySelectionRoute
import com.sachin_singh_dighan.newsapp.ui.languageselection.LanguageSelectionRoute
import com.sachin_singh_dighan.newsapp.ui.mainscreen.MainScreenRoute
import com.sachin_singh_dighan.newsapp.ui.news.NewsListRoute
import com.sachin_singh_dighan.newsapp.ui.newsources.NewsSourceRoute
import com.sachin_singh_dighan.newsapp.ui.searchnews.SearchNewsScreenRoute
import com.sachin_singh_dighan.newsapp.ui.topheadline.TopHeadlinesRoute

sealed class Route(val name: String) {
    data object MainScreen : Route("MainScreen")
    data object TopHeadlines : Route("TopHeadlines")
    data object NewsSources : Route("NewsSources")
    data object CountrySelection : Route("CountrySelection")
    data object LanguageSelection : Route("LanguageSelection")
    data object SearchNews : Route("SearchNews")
    data object NewsList : Route("NewsList/{value}/{type}") {
        fun createRoute(value: String, type: String) = "NewsList/$value/$type"
    }
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
                            source,
                            AppConstant.NEWS_BY_CATEGORY_VALUE
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
                            country,
                            AppConstant.NEWS_BY_COUNTRY_VALUE
                        )
                    )
                }
            )
        }
        composable(route = Route.LanguageSelection.name) {
            LanguageSelectionRoute(
                onClick = { language ->
                    navController.navigate(
                        Route.NewsList.createRoute(
                            language,
                            AppConstant.NEWS_BY_LANGUAGE_VALUE
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
                navArgument("type") { type = NavType.StringType }
            )) {
            val fetchNewByType = it.arguments?.getString("value") ?: ""
            val newType = it.arguments?.getString("type") ?: ""
            NewsListRoute(
                fetchNewByType,
                newType,
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

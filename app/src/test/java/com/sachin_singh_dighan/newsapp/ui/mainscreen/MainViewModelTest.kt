package com.sachin_singh_dighan.newsapp.ui.mainscreen

import app.cash.turbine.test
import com.sachin_singh_dighan.newsapp.AppConstant
import com.sachin_singh_dighan.newsapp.data.model.mainscreen.MainSection
import com.sachin_singh_dighan.newsapp.data.model.topheadline.Article
import com.sachin_singh_dighan.newsapp.data.repository.mainscreen.MainRepository
import com.sachin_singh_dighan.newsapp.ui.common.UiState
import com.sachin_singh_dighan.newsapp.ui.mainscreen.MainViewModel
import com.sachin_singh_dighan.newsapp.utils.DispatcherProvider
import com.sachin_singh_dighan.newsapp.utils.NetworkHelper
import com.sachin_singh_dighan.newsapp.utils.TestDispatcherProvider
import com.sachin_singh_dighan.newsapp.utils.logger.Logger
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @Mock
    private lateinit var mainRepository: MainRepository

    @Mock
    private lateinit var networkHelper: NetworkHelper

    @Mock
    private lateinit var logger: Logger

    private lateinit var dispatcherProvider: DispatcherProvider


    @Before
    fun setUp() {
        dispatcherProvider = TestDispatcherProvider()
    }

    @Test
    fun fetchMainSections_whenRepositoryResponseSuccess_shouldSetSuccessUiState() {
        runTest {
            val mainSectionList = mutableListOf<MainSection>()
            mainSectionList.add(MainSection(AppConstant.TOP_HEADLINES))
            mainSectionList.add(MainSection(AppConstant.NEWS_SOURCES))
            doReturn(flowOf(mainSectionList))
                .`when`(mainRepository)
                .getDataForMainSection()
            val viewModel = MainViewModel(mainRepository, networkHelper, logger, dispatcherProvider)
            viewModel.uiState.test {
                Assert.assertEquals(UiState.Success(mainSectionList), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
            verify(mainRepository, times(1)).getDataForMainSection()
        }
    }

    @Test
    fun fetchMainSections_whenRepositoryResponseError_shouldSetErrorUiState() {
        runTest {
            val errorMessage = "Error Message For You"
            doReturn(flow<List<Article>> {
                throw IllegalStateException(errorMessage)
            })
                .`when`(mainRepository)
                .getDataForMainSection()
            val viewModel = MainViewModel(mainRepository, networkHelper, logger, dispatcherProvider)
            viewModel.uiState.test {
                assertEquals(
                    UiState.Error(IllegalStateException(errorMessage).toString()),
                    awaitItem()
                )
                cancelAndIgnoreRemainingEvents()
            }
            verify(mainRepository, times(1)).getDataForMainSection()
        }
    }


    @After
    fun tearDown() {
        // Do something here
    }

}
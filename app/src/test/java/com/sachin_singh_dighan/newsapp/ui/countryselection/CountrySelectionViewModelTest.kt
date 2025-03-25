package com.sachin_singh_dighan.newsapp.ui.countryselection

import app.cash.turbine.test
import com.sachin_singh_dighan.newsapp.AppConstant
import com.sachin_singh_dighan.newsapp.data.model.countryselection.CountrySelection
import com.sachin_singh_dighan.newsapp.data.repository.countryselection.CountrySelectionRepository
import com.sachin_singh_dighan.newsapp.ui.common.UiState
import com.sachin_singh_dighan.newsapp.utils.NetworkHelper
import com.sachin_singh_dighan.newsapp.utils.TestDispatcherProvider
import com.sachin_singh_dighan.newsapp.utils.logger.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CountrySelectionViewModelTest {

    @Mock
    private lateinit var countrySelectionRepository: CountrySelectionRepository

    @Mock
    private lateinit var networkHelper: NetworkHelper

    @Mock
    private lateinit var logger: Logger

    private lateinit var viewModel: CountrySelectionViewModel

    private lateinit var dispatcherProvider: TestDispatcherProvider

    @Before
    fun setUp() {
        dispatcherProvider = TestDispatcherProvider()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun init_shouldFetchCountryData_WHEN_networkIsAvailable() = runTest {
        // Arrange
        val mockCountrySelections = listOf(
            CountrySelection(
                countryName = "United States",
                countryCode = "us"
            ),
            CountrySelection(
                countryName = "United Kingdom",
                countryCode = "gb"
            ),
            CountrySelection(
                countryName = "India",
                countryCode = "in"
            )
        )

        `when`(networkHelper.isNetworkAvailable()).thenReturn(true)
        `when`(countrySelectionRepository.getDataForCountry()).thenReturn(
            flowOf(
                mockCountrySelections
            )
        )

        // Act
        viewModel = CountrySelectionViewModel(
            countrySelectionRepository,
            networkHelper,
            logger,
            dispatcherProvider
        )

        // Assert
        viewModel.uiState.test {
            val emittedItem = awaitItem()
            assertTrue(emittedItem is UiState.Success)
            assertEquals(mockCountrySelections, (emittedItem as UiState.Success).data)
            cancelAndIgnoreRemainingEvents()
        }

        verify(networkHelper).isNetworkAvailable()
        verify(countrySelectionRepository).getDataForCountry()
        verify(logger).d(CountrySelectionViewModel.TAG, mockCountrySelections.toString())
    }

    @Test
    fun init_shouldShowError_WHEN_networkIsUnavailable() = runTest {
        // Arrange
        `when`(networkHelper.isNetworkAvailable()).thenReturn(false)

        // Act
        viewModel = CountrySelectionViewModel(
            countrySelectionRepository,
            networkHelper,
            logger,
            dispatcherProvider,
        )

        // Assert
        viewModel.uiState.test {
            val emittedItem = awaitItem()
            assertTrue(emittedItem is UiState.Error)
            assertEquals(AppConstant.NETWORK_ERROR, (emittedItem as UiState.Error).message)
            cancelAndIgnoreRemainingEvents()
        }

        verify(networkHelper).isNetworkAvailable()
    }

    @Test
    fun init_shouldShowError_WHEN_repositoryThrowsException() = runTest {
        // Arrange
        val exception = IOException("Repository error")

        `when`(networkHelper.isNetworkAvailable()).thenReturn(true)
        `when`(countrySelectionRepository.getDataForCountry()).thenReturn(
            flow { throw exception }
        )

        // Act
        viewModel = CountrySelectionViewModel(
            countrySelectionRepository,
            networkHelper,
            logger,
            dispatcherProvider,
        )

        // Assert
        viewModel.uiState.test {
            val emittedItem = awaitItem()
            assertTrue(emittedItem is UiState.Error)
            assertEquals(exception.toString(), (emittedItem as UiState.Error).message)
            cancelAndIgnoreRemainingEvents()
        }

        verify(networkHelper).isNetworkAvailable()
        verify(countrySelectionRepository).getDataForCountry()
        verify(logger).d(CountrySelectionViewModel.TAG, exception.toString())
    }

    @Test
    fun getCountrySelection_shouldHandleEmptyListFromRepository() = runTest {
        // Arrange
        val emptyList = emptyList<CountrySelection>()

        `when`(networkHelper.isNetworkAvailable()).thenReturn(true)
        `when`(countrySelectionRepository.getDataForCountry()).thenReturn(flowOf(emptyList))

        // Act
        viewModel = CountrySelectionViewModel(
            countrySelectionRepository,
            networkHelper,
            logger,
            dispatcherProvider,
        )

        // Assert
        viewModel.uiState.test {
            val emittedItem = awaitItem()
            assertTrue(emittedItem is UiState.Success)
            assertEquals(emptyList, (emittedItem as UiState.Success).data)
            assertEquals(0, emittedItem.data.size)
            cancelAndIgnoreRemainingEvents()
        }

        verify(networkHelper).isNetworkAvailable()
        verify(countrySelectionRepository).getDataForCountry()
        verify(logger).d(CountrySelectionViewModel.TAG, emptyList.toString())
    }
}
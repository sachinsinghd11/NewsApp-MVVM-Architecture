package com.sachin_singh_dighan.newsapp.data.repository.countryselection

import app.cash.turbine.test
import com.sachin_singh_dighan.newsapp.AppConstant
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CountrySelectionRepositoryTest {

    private lateinit var countrySelectionRepository: CountrySelectionRepository

    @Before
    fun setUp() {
        countrySelectionRepository = CountrySelectionRepository()
    }

    @Test
    fun `getDataForCountry should return country list from AppConstant`() = runTest {
        // Arrange
        val originalCountryList = AppConstant.COUNTRY_LIST

        // Act & Assert
        countrySelectionRepository.getDataForCountry().test {
            val result = awaitItem()

            // Should match the constant country list
            assertEquals(originalCountryList, result)
            assertNotNull(result)
            assertTrue(result.isNotEmpty())

            // Test if expected countries are present (assuming AppConstant has these countries)
            // This helps validate the repository is returning the right data
            assertTrue(result.any { it.countryCode == "us" }) // US should be in the list

            awaitComplete()
        }
    }

    @Test
    fun `getDataForCountry should return Flow that completes successfully`() = runTest {
        // Act & Assert
        countrySelectionRepository.getDataForCountry().test {
            // Get any item
            awaitItem()

            // Flow should complete successfully
            awaitComplete()

            // Should be no more items
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `getDataForCountry should emit only once`() = runTest {
        // Act & Assert
        countrySelectionRepository.getDataForCountry().test {
            // Should emit only one item
            awaitItem()

            // Should not emit anything else
            expectNoEvents()

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getDataForCountry should emit list with expected structure`() = runTest {
        // Act & Assert
        countrySelectionRepository.getDataForCountry().test {
            val result = awaitItem()

            // Each country item should have proper structure
            result.forEach { country ->
                assertNotNull(country.countryName)
                assertNotNull(country.countryCode)

                // Country code should be 2 characters (ISO standard)
                assertEquals(2, country.countryCode.length)
            }

            awaitComplete()
        }
    }

    @Test
    fun `getDataForCountry should return countries with valid country codes`() = runTest {
        // Act & Assert
        countrySelectionRepository.getDataForCountry().test {
            val result = awaitItem()

            // Check if common country codes are present
            val countryCodes = result.map { it.countryCode }

            // List of some common country codes that should be present
            val expectedCommonCodes = listOf("us", "gb", "in", "ca", "au")

            // At least some of these common codes should be present
            assertTrue(countryCodes.any { it in expectedCommonCodes })

            awaitComplete()
        }
    }
} 
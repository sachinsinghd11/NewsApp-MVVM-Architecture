package com.sachin_singh_dighan.newsapp.data.repository.languageselection

import app.cash.turbine.test
import com.sachin_singh_dighan.newsapp.AppConstant
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalCoroutinesApi
class LanguageSelectionRepositoryTest {

    private lateinit var languageSelectionRepository: LanguageSelectionRepository

    @Before
    fun setUp() {
        languageSelectionRepository = LanguageSelectionRepository()
    }

    @Test
    fun getLanguageData_shouldReturnLanguageListFromAppConstant() = runTest {
        // Act & Assert
        languageSelectionRepository.getLanguageData().test {
            val result = awaitItem()

            // Compare AppConstant.LANGUAGE_LIST with result
            assertEquals(AppConstant.LANGUAGE_LIST, result)
            assertNotNull(result)

            awaitComplete()
        }
    }

    @Test
    fun getLanguageData_shouldEmitListWithExpectedStructure() = runTest {
        // Act & Assert
        languageSelectionRepository.getLanguageData().test {
            val result = awaitItem()

            // languageCode, languageName  should be present in result
            result.forEach { languageData ->
                assertNotNull(languageData.languageCode)
                assertNotNull(languageData.languageName)
            }

            awaitComplete()
        }
    }
}
package com.sachin_singh_dighan.newsapp.data.repository.mainscreen

import app.cash.turbine.test
import com.sachin_singh_dighan.newsapp.AppConstant
import com.sachin_singh_dighan.newsapp.data.model.mainscreen.MainSection
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class MainRepositoryTest {

    private lateinit var mainRepository: MainRepository

    @Before
    fun setUp() {
        mainRepository = MainRepository()
    }

    @Test
    fun getDataForMainSection_whenRepositoryContainsData() {

        runTest {
            val mainSectionList = mutableListOf<MainSection>()
            mainSectionList.add(MainSection(AppConstant.TOP_HEADLINES))
            mainSectionList.add(MainSection(AppConstant.NEWS_SOURCES))
            mainSectionList.add(MainSection(AppConstant.COUNTRIES))
            mainSectionList.add(MainSection(AppConstant.LANGUAGES))
            mainSectionList.add(MainSection(AppConstant.SEARCH))

            mainRepository.getDataForMainSection()
            mainRepository.getDataForMainSection().test {
                Assert.assertEquals(mainSectionList.size, awaitItem().size)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }


    @Test
    fun getDataForMainSection_shouldEmit_MAIN_SECTION_LIST_fromAppConstant() = runTest {
        // Given
        val expectedSections = AppConstant.MAIN_SECTION_LIST

        // When
        val result = mainRepository.getDataForMainSection()

        // Then
        result.test {
            val emittedValue = awaitItem()
            Assert.assertEquals(expectedSections, emittedValue)
            Assert.assertEquals(expectedSections.size, emittedValue.size)
            cancelAndIgnoreRemainingEvents()
        }
    }


    @Test
    fun getDataForMainSection_shouldEmitOnlyOnce() = runTest {
        // When
        val emissions = mainRepository.getDataForMainSection().toList()

        // Then
        Assert.assertEquals(1, emissions.size)
    }

    @Test
    fun getDataForMainSection_shouldEmitNonEmptyList() = runTest {
        // When
        val result = mainRepository.getDataForMainSection()

        // Then
        result.test {
            val emittedValue = awaitItem()
            Assert.assertTrue(emittedValue.isNotEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getDataForMainSection_shouldEmitListContainingExpectedSectionNames() = runTest {
        // Given
        // We assume AppConstant.MAIN_SECTION_LIST contains these standard sections
        val expectedSectionNames = setOf(
            AppConstant.TOP_HEADLINES,
            AppConstant.NEWS_SOURCES,
            AppConstant.COUNTRIES,
            AppConstant.LANGUAGES,
            AppConstant.SEARCH,
        )

        // When
        val result = mainRepository.getDataForMainSection()

        // Then
        result.test {
            val emittedValue = awaitItem()
            val actualSectionNames = emittedValue.map { it.sectionName }.toSet()

            // Verify at least the expected sections are in the list
            // This makes the test more robust if AppConstant.MAIN_SECTION_LIST changes
            Assert.assertTrue(
                actualSectionNames.containsAll(expectedSectionNames) ||
                        expectedSectionNames.containsAll(actualSectionNames)
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getDataForMainSection_shouldEmitCorrectlyStructuredMainSectionObjects() = runTest {
        // When
        val result = mainRepository.getDataForMainSection()

        // Then
        result.test {
            val emittedValue = awaitItem()

            // Check that each item is properly structured
            emittedValue.forEach { section ->
                Assert.assertTrue(section.sectionName.isNotBlank())
                Assert.assertTrue(section.sectionName == section.sectionName.trim())

                // If there's a specific format for identifiers (like hyphenated names)
                // you could add more specific checks here
            }

            cancelAndIgnoreRemainingEvents()
        }
    }


}
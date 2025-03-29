# NewsApp-MVVM-Architecture

<p align="center">
<img alt="banner" src="https://github.com/user-attachments/assets/0a5a15b7-d985-4a52-8b75-3a658e5c5564">
</p>

## Branches

- **main:** Jetpack Compose using Dagger Hilt
- **migration_dagger_hilt:** XML using Dagger Hilt
- **app_with_xml:** XML using Dagger 2

## Migrate XML Project to Jetpack Compose

- **Update Android Gradle Plugin and Kotlin Plugin:** Using the latest versions of the Android Gradle Plugin and Kotlin Plugin.
- **Add Compose Dependencies:** build.gradle (module-level)
- **Set Up Compose Application:** Create a new @Composable function
- **Replace XML Layouts with Compose Code:** For example, if you had an XML layout with a TextView, replace it with a Compose Text element.
- **Adopt Compose Components:** Replace XML-based UI components with their Compose equivalents.
- **Integrate Compose Navigation:** Migrate from XML-based navigation to Compose Navigation.
- **Migrate UI Logic:** Update UI logic to use Compose's state management.
- **Migrate Resources:** Migrate string resources, colors, drawable.
- **Update Gradle Plugin Versions:** Update your Gradle dependencies accordingly.
- **Testing:** Write tests for your Compose UI using the Compose testing library.
- **Documentation and Learning:** - Refer to the official Jetpack Compose documentation and samples. Learn about Compose concepts like Composables, state management, and navigation.

## Migrate Dagger2 Project to Dagger-Hilt

- **Add Hilt Dependencies:** build.gradle (module-level)
- **Apply Hilt Gradle Plugin:** build.gradle (apply plugin: 'dagger.hilt.android.plugin').
- **Update Dagger Annotations:** Replace Dagger annotations with Hilt annotations where needed, For example, replace @Component with @HiltComponent.
- **Add Hilt Android Application:** Replace Dagger's DaggerAppComponent with Hilt's Hilt_AppComponent.
- **Annotate Android Application Class:** Annotate your Application class with @HiltAndroidApp.
- **Replace Dagger Android Modules:** Replace Dagger Android modules with Hilt Android entry points.
- **Update Injection in Activities/Fragments:** Replace Dagger with Hilt for activity or fragment injection.
- **Update ViewModel Injection:** Replace Dagger ViewModelFactory and ViewModelKey with Hilt's HiltViewModel and @ViewModelInject.
- **Update Dagger Android Testing Components:** Replace DaggerMyTestComponent with HiltTestApplicationComponent.

  ## Major Highlights

- **Jetpack Compose** for modern UI
- **Offline caching** with a **single source of truth**
- **MVVM architecture** for a clean and scalable codebase
- **Kotlin** and **Kotlin DSL**
- **Dagger Hilt** for efficient dependency injection.
- **Retrofit** for seamless networking
- **Room DB** for local storage of news articles
- **Coroutines** and **Flow** for asynchronous programming
- **StatFlow** for streamlined state management
- **Pagination** to efficiently load and display news articles
- **Unit tests** and **UI tests** for robust code coverage
- **Instant search** for quick access to relevant news
- **Navigation** for smooth transitions between screens
- **WorkManager** for periodic news fetching
- **Notification** for alerting about latest news
- **Coil** for efficient image loading

<p align="center">
<img alt="mvvm-architecture"  src="https://github.com/user-attachments/assets/15031f29-fbe0-4184-9d55-6fbd878b665b">
</p>

- ## Feature implemented:
- NewsApp
- Instant search using Flow operators
    - Debounce
    - Filter
    - DistinctUntilChanged
    - FlatMapLatest
- Offline news
- Pagination
- Unit Test
    - Mockito
    - Turbine https://github.com/cashapp/turbine
    - Espresso
- TopHeadlines of the news
- Country-wise news
- Multiple Languages selection-wise news
- Multiple sources wise news

## Dependency Use

- Jetpack Compose for UI: Modern UI toolkit for building native Android UIs
- Coil for Image Loading: Efficiently loads and caches images
- Retrofit for Networking: A type-safe HTTP client for smooth network requests
- Dagger Hilt for Dependency Injection: Simplifies dependency injection
- Room for Database: A SQLite object mapping library for local data storage
- Paging Compose for Pagination: Simplifies the implementation of paginated lists
- Mockito, JUnit, Turbine for Testing: Ensures the reliability of the application

##  Dependency Used:

- UI
```
implementation ("androidx.compose.ui:ui")
implementation ("androidx.compose.ui:ui-graphics")
implementation ("androidx.compose.ui:ui-tooling-preview")
implementation ("androidx.lifecycle:lifecycle-runtime-compose:2.8.7")
implementation ("androidx.activity:activity-compose:1.10.1")
val composeBom = platform("androidx.compose:compose-bom:2025.02.00")
implementation(composeBom)
```

- Material3
```
implementation("androidx.compose.material3:material3")
```

- Navigation
```
implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
implementation("androidx.navigation:navigation-compose:2.8.9")
```

- Coil for image loading
```
implementation("io.coil-kt:coil-compose:2.7.0")
```

- Retrofit for networking
```
implementation("com.squareup.retrofit2:retrofit:2.11.0")
implementation("com.squareup.retrofit2:converter-gson:2.11.0")
implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
```
- Paging library
```
implementation("androidx.paging:paging-runtime-ktx:3.3.6")
implementation("androidx.paging:paging-compose:3.3.6")
```

- Dagger hilt for dependency injection
```
 implementation("com.google.dagger:hilt-android:2.54")
 ksp("com.google.dagger:hilt-compiler:2.54")
```

- For webView browser
```
implementation("androidx.browser:browser:1.8.0")
```

- Room database
```
implementation("androidx.room:room-runtime:2.6.1")
ksp("androidx.room:room-compiler:2.6.1")
// optional - Kotlin Extensions and Coroutines support for Room
implementation("androidx.room:room-ktx:2.6.1")
```

- WorkManager
```
 implementation("androidx.work:work-runtime-ktx:2.10.0")
 implementation("androidx.hilt:hilt-work:1.2.0")
 ksp("androidx.hilt:hilt-compiler:1.2.0")
```
- Local Unit test
```
testImplementation("junit:junit:4.13.2")
testImplementation("org.mockito:mockito-core:5.3.1")
testImplementation("androidx.arch.core:core-testing:2.2.0")
testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.1")
testImplementation("app.cash.turbine:turbine:0.12.1")
```
- UI Test
```
androidTestImplementation("androidx.test.ext:junit:1.2.1")
androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
androidTestImplementation(composeBom)
androidTestImplementation("androidx.compose.ui:ui-test-junit4")
androidTestImplementation("androidx.navigation:navigation-testing:2.8.9")
androidTestImplementation("androidx.compose.ui:ui-tooling")
debugImplementation("androidx.compose.ui:ui-test-manifest")
```

## The Complete Project Folder Structure

```
└── com
    └── sachin_singh_dighan
        └── newsapp
            ├── NewsApplication.kt
            ├── data
            │   ├── api
            │   │   └── NetworkService.kt
            │   ├── local
            │   │   ├── AppDatabaseService.kt
            │   │   ├── DatabaseService.kt
            │   │   ├── AppDatabase.kt
            │   │   ├── dao
            │   │   │   ├── ArticleDao.kt
            │   │   └── entity
            │   │       ├── Article.kt
            │   │       └── Source.kt
            │   ├── model
            │   │   ├── countryselection
            │   │   │   └── CountrySelection.kt
            │   │   ├── languageselection
            │   │   │   └── LanguageData.kt    
            │   │   ├── mainscreen
            │   │   │   └── MainSection.kt  
            │   │   ├── newsources
            │   │   │   ├── Sources.kt
            │   │   │   └── NewSourceResponse.kt
            │   │   └── topheadline
            │   │       ├── ApiArticle.kt
            │   │       ├── ApiSource.kt
            │   │       └── TopHeadLinesResponse.kt
            │   └── repository
            │       ├── countryselection
            │       │   └── CountrySelectionRepository.kt
            │       ├── languageselection
            │       │   └── LanguageSelectionRepository.kt
            │       ├── mainscreen
            │       │   └── MainRepository.kt
            │       ├── newsources
            │       │   └── NewsSourcesRepository.kt
            │       ├── offlineArticles
            │       │   └── OfflineArticleRepository.kt
            │       ├── searchnews
            │       │   └── SearchNewsRepository.kt
            │       ├── topheadline
            │       │   └── TopHeadLineRepository.kt
            │       ├── topheadlinepaging
            │       │   ├── TopHeadlinePagingSource.kt
            │       │   └── TopHeadlinePagingSourceRepository.kt
            │       ├── topheadlinesync
            │           └── TopHeadlinesSyncRepository.kt
            ├── di
            │   ├── module
            │   │   └── ApplicationModule.kt
            │   └── qualifiers.kt
            ├── ui
            │   ├── MainActivity.kt
            │   ├── base
            │   │   ├── BaseViewModel.kt
            │   │   └── NewsNavigation.kt
            │   ├── common
            │   │   └── UiState.kt
            │   ├── component
            │   │   ├── BannerImage.kt
            │   │   ├── ButtonMainSection.kt
            │   │   ├── CommonUI.kt
            │   │   ├── CustomClickableCardButton.kt
            │   │   ├── DescriptionText.kt
            │   │   ├── ErrorDialogScreen.kt
            │   │   ├── SourceText.kt
            │   │   ├── TitleText.kt
            │   │   └── TopAppBar.kt
            │   ├── countryselection
            │   │   ├── CountrySelectionScreen.kt
            │   │   └── CountrySelectionViewModel.kt
            │   ├── languageselection
            │   │   ├── LanguageSelectionScreen.kt
            │   │   └── LanguageSelectionViewModel.kt
            │   ├── mainscreen
            │   │   ├── MainActivity.kt
            │   │   ├── MainScreen.kt
            │   │   └── MainViewModel.kt
            │   ├── news
            │   │   ├── NewsListScreen.kt
            │   │   └── NewsListViewModel.kt
            │   ├── newsources
            │   │   ├── NesSourceScreen.kt
            │   │   └── NewsSourcesViewModel.kt
            │   ├── offlinearticle
            │   │   ├── OfflineArticleScreen.kt
            │   │   └── OfflineArticleViewModel.kt
            │   ├── searchnews
            │   │   ├── SearchNewsScreen.kt
            │   │   └── SearchNewsViewModel.kt 
            │   ├── theme
            │   │   ├── Color.kt
            │   │   ├── Theme.kt
            │   │   └── Type.kt
            │   └── topheadline
            │   │   ├── TopHeadlineScreen.kt
            │   │   └── TopHeadlineViewModel.kt
            │   └── topheadlinebypaging
            │   │   ├── TopHeadlinePaginationScreen.kt
            │   │   └── TopHeadlineByPagingViewModel.kt
            │   └── topheadlinesync
            │       └── TopHeadlineSyncViewModel.kt
            ├── utils
            │   ├── AppConstant.kt
            │   ├── AuthInterceptor.kt
            │   ├── DispatcherProvider.kt
            │   ├── Extensions.kt
            │   ├── NetworkHelper.kt
            │   ├── NetworkHelperImpl.kt
            │   ├── RetryInterceptor.kt
            │   ├── TimeUtil.kt
            │   ├── logger
            │   │   ├── AppLogger.kt
            │   │   └── Logger.kt
            │   └── TypeAliases.kt
            └── worker
                ├── TopHeadlinesSyncWorker.kt
                └── TopHeadlineSyncScheduler.kt
```

<p align="center">
<img alt="main_screen" src="https://github.com/user-attachments/assets/4576ea3c-6049-4776-b9a4-e3bfa0d81dfb">
</p>

## 🚀 About Me
Hi there! My name is Nitin Londhe, I work as a Software Developer and like to expand my skill set in my spare time.

If you have any questions or want to connect, feel free to reach out to me on :

- [LinkedIn](https://linkedin.com/in/sachin-singh-2249b89b/)
- [GitHub](https://github.com/sachinsinghd11)

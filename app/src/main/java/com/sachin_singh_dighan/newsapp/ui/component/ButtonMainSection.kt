package com.sachin_singh_dighan.newsapp.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sachin_singh_dighan.newsapp.data.model.countryselection.CountrySelection
import com.sachin_singh_dighan.newsapp.data.model.languageselection.LanguageData
import com.sachin_singh_dighan.newsapp.data.model.mainscreen.MainSection
import com.sachin_singh_dighan.newsapp.data.model.newsources.Sources

@Composable
fun <T> ButtonMainSection(
    onClick: (String) -> Unit,
    data: T,
) {
    val textCode =
        when (data) {
            is Sources -> data.category
            is CountrySelection -> data.countryCode
            is LanguageData -> data.languageCode
            is MainSection -> data.sectionName
            else -> ""
        }

    val text =
        when (data) {
            is Sources -> data.category
            is CountrySelection -> data.countryName
            is LanguageData -> data.languageName
            is MainSection -> data.sectionName
            else -> ""
        }

    Button(
        onClick = { onClick(textCode) },
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(8.dp),
        shape = RoundedCornerShape(6.dp),
    ) {
        Text(text,fontSize = 28.sp)
    }

}

@Preview
@Composable
fun ButtonWithTextPreview() {
    ButtonMainSection(onClick = {}, "Text Button")
}
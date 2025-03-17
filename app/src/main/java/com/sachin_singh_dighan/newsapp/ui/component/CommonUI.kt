package com.sachin_singh_dighan.newsapp.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.sachin_singh_dighan.newsapp.R

@Composable
fun ShowLoading(){
    Box(
        modifier = Modifier.fillMaxWidth().fillMaxHeight()
    ){
        val contentDesc = stringResource(R.string.app_name)
        CircularProgressIndicator(modifier = Modifier
            .align(Alignment.Center)
            .semantics { contentDescription = contentDesc })
    }
}

@Composable
fun ShowError(text: String){
    Box(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = Color.Red,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(4.dp)
        )
    }
}

@Composable
fun EmptyState(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
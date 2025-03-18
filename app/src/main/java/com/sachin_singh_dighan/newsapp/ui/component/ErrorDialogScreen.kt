package com.sachin_singh_dighan.newsapp.ui.component

import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.sachin_singh_dighan.newsapp.R

@Composable
fun ErrorDialog(
    errorMessage: String,
    onDismiss: () -> Unit,
    onTryAgain: () -> Unit,
    showDismissButton: Boolean = true,
    dismissButtonText: String = "Dismiss",
    tryAgainButtonText: String = "Try Again",
    properties: DialogProperties = DialogProperties(
        dismissOnBackPress = true,
        dismissOnClickOutside = true
    )
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        properties = properties,
        icon = {
            Icon(
                imageVector = Icons.Filled.Clear,
                contentDescription = "Error Icon",
                tint = MaterialTheme.colorScheme.error
            )
        },
        title = {
            Text(
                text = "Oops",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column (
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onTryAgain,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = tryAgainButtonText)
            }
        },
        dismissButton = {
            if (showDismissButton) {
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = dismissButtonText)
                }
            }
        }
    )
}

/**
 * A customizable error dialog with a different visual style that displays
 * an error message in a card-like surface.
 *
 * @param errorMessage The error message to display
 * @param onDismiss Function to execute when the dialog is dismissed
 * @param onTryAgain Function to execute when the "Try Again" button is clicked
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ErrorDialogCard(
    errorMessage: String,
    onDismiss: () -> Unit,
    onTryAgain: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        content = {
            Surface (
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 4.dp
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "Error Icon",
                        tint = MaterialTheme.colorScheme.error
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Error Occurred",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = errorMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton (onClick = onDismiss) {
                            Text(text = "Dismiss")
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button (onClick = onTryAgain) {
                            Text(text = "Try Again")
                        }
                    }
                }
            }
        },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    )
}

@Preview
@Composable
fun ErrorDialogPreview() {
    MaterialTheme {
        ErrorDialog(
            errorMessage = "Failed to load data. Please check your internet connection and try again.",
            onDismiss = { },
            onTryAgain = { }
        )
    }
}

@Preview
@Composable
fun ErrorDialogCardPreview() {
    MaterialTheme {
        ErrorDialogCard(
            errorMessage = "Failed to load data. Please check your internet connection and try again.",
            onDismiss = { },
            onTryAgain = { }
        )
    }
}
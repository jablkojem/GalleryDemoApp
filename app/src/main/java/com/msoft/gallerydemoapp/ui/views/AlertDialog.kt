package com.msoft.gallerydemoapp.ui.views

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.msoft.gallerydemoapp.utils.DefaultPreviews

@Composable
fun AlertDialog(
    title: String,
    confirmButtonText: String,
    dismissButtonText: String,
    onConfirmButtonClick: () -> Unit,
    onDismiss: () -> Unit
) {
    MaterialTheme {
        Column {
            AlertDialog(
                onDismissRequest = {
                    onDismiss()
                },
                title = {
                    Text(text = title)
                },
                confirmButton = {
                    Button(
                        onClick = {
                            onConfirmButtonClick()
                        }) {
                        Text(confirmButtonText)
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            onDismiss()
                        }) {
                        Text(dismissButtonText)
                    }
                }
            )
        }

    }
}

@Composable
@DefaultPreviews
fun AlertDialogPreview() {
    MaterialTheme {
        Surface {
            AlertDialog("Title", "Confirm", "Reject", {}, {})
        }
    }
}

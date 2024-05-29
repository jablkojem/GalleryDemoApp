package com.msoft.gallerydemoapp.ui.views

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.msoft.gallerydemoapp.utils.DefaultPreviews

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrimaryButton(text: String, modifier: Modifier, onClick: () -> Unit) {
    Card(
        onClick = { onClick() },
        shape = RoundedCornerShape(2.dp),
        modifier = modifier
    ) {
        Text(
            text = text.uppercase(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}

@Composable
@DefaultPreviews
fun PrimaryButtonPreview() {
    MaterialTheme {
        Surface {
            PrimaryButton("Text", Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp), onClick = {})
        }
    }
}

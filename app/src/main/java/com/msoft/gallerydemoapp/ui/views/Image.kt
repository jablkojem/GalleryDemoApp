package com.msoft.gallerydemoapp.ui.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.msoft.gallerydemoapp.R
import com.msoft.gallerydemoapp.data.models.ImageData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Image(data: ImageData, onClick: (ImageData) -> Unit, modifier: Modifier = Modifier) {
    Card(
        onClick = { onClick(data) },
        shape = RoundedCornerShape(4.dp),
        modifier = modifier
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            AsyncImage(
                model = data.uri,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            if (data.isFavourite) {
                Icon(
                    painterResource(id = R.drawable.ic_star_filled),
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(4.dp)
                        .align(Alignment.BottomStart)
                )
            }
        }
    }
}

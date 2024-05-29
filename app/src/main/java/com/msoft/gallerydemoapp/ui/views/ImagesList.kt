package com.msoft.gallerydemoapp.ui.views

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.msoft.gallerydemoapp.data.models.ImageData

@Composable
fun ImagesList(imagesData: List<ImageData>, onClick: (ImageData) -> Unit, columns: Int = 4, modifier: Modifier = Modifier) {
    val imageSize = (LocalConfiguration.current.screenWidthDp.dp - (4.dp * columns)) / columns  // (4.dp * columns) -> space for paddings

    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        modifier = modifier
    ) {
        items(imagesData.size) { index ->
            Image(
                imagesData[index],
                onClick,
                Modifier
                    .size(imageSize)
                    .padding(2.dp)
            )
        }
    }
}

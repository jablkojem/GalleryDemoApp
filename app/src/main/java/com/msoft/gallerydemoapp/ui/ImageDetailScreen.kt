package com.msoft.gallerydemoapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.msoft.gallerydemoapp.R
import com.msoft.gallerydemoapp.data.models.ImageInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageDetailScreen(
    uri: String,
    isFavourite: Boolean,
    info: ImageInfo,
    popBackStack: () -> Unit,
    switchFavouriteButtonClick: (String) -> Unit,
    deleteImage: (String) -> Unit
) {
    var infoBottomSheetVisible by remember { mutableStateOf(false) }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.secondaryContainer) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = uri,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .padding(bottom = 60.dp)
                    .fillMaxSize()
            )

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(0.4f)),
                onClick = {
                    popBackStack()
                },
                modifier = Modifier
                    .padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
                    .width(40.dp)
                    .height(40.dp),

                ) {
                Icon(
                    painterResource(id = R.drawable.ic_arrow_back),
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxSize()
                )
            }

            Row(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .height(60.dp)
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            ) {
                ImageBottomActionButton(if (isFavourite) R.drawable.ic_star_filled else R.drawable.ic_star, Modifier.weight(1f)) { switchFavouriteButtonClick(uri) }
                ImageBottomActionButton(R.drawable.ic_info, Modifier.weight(1f)) { infoBottomSheetVisible = true }
                ImageBottomActionButton(R.drawable.ic_delete, Modifier.weight(1f)) { deleteImage(uri) }
            }
        }

        if (infoBottomSheetVisible) {
            ImageInfoBottomSheet(info) { infoBottomSheetVisible = false }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ImageBottomActionButton(icon: Int, modifier: Modifier, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(20.dp),
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        modifier = modifier
            .padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
            .width(40.dp)
            .height(40.dp),
    ) {
        Icon(
            painterResource(id = icon),
            tint = MaterialTheme.colorScheme.onSecondaryContainer,
            contentDescription = null,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ImageInfoBottomSheet(info: ImageInfo, onDismiss: () -> Unit) {
    val modalBottomSheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column {
            Text(
                text = stringResource(id = R.string.image_detail_image_name_label),
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 12.dp)
            )
            Text(
                text = info.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp)
            )

            Text(
                text = stringResource(id = R.string.image_detail_date_created_label),
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 12.dp)
            )
            Text(
                text = info.dateTaken,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp)
            )

            Text(
                text = stringResource(id = R.string.image_detail_size_label),
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 12.dp)
            )
            Text(
                text = info.size,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp)
            )

            Spacer(modifier = Modifier.height(64.dp))
        }
    }
}

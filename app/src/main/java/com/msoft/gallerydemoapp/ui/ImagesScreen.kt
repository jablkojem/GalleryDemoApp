package com.msoft.gallerydemoapp.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.msoft.gallerydemoapp.R
import com.msoft.gallerydemoapp.data.models.ImageData
import com.msoft.gallerydemoapp.data.models.UiState
import com.msoft.gallerydemoapp.ui.views.ImagesList
import com.msoft.gallerydemoapp.ui.views.PrimaryButton

@Composable
fun ImagesScreen(uiState: UiState, imagesData: List<ImageData>, onPermissionsButtonClick: () -> Unit, onImageClick: (ImageData) -> Unit) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        when (uiState) {
            UiState.LOADING -> {
                Loading()
            }
            UiState.PERMISSIONS_NOT_GRANTED -> {
                PermissionsNotGranted { onPermissionsButtonClick() }
            }
            UiState.NO_IMAGES -> {
                ImagesEmptyState()
            }
            UiState.CONTENT -> {
                var orientation by remember { mutableIntStateOf(Configuration.ORIENTATION_PORTRAIT) }
                val configuration = LocalConfiguration.current

                LaunchedEffect(configuration) { orientation = configuration.orientation }

                ImagesList(imagesData, onImageClick, if (orientation == Configuration.ORIENTATION_PORTRAIT) 4 else 6)
            }
        }
    }
}

@Composable
fun PermissionsNotGranted(onPermissionsButtonClick: () -> Unit) {
    Column(modifier = Modifier.wrapContentHeight(align = Alignment.CenterVertically)) {
        Text(stringResource(R.string.show_images_permissions_not_granted_title), modifier = Modifier.padding(16.dp), textAlign = TextAlign.Center)
        PrimaryButton(
            stringResource(R.string.show_images_permissions_not_granted_action_button_text),
            modifier = Modifier.padding(16.dp),
            onPermissionsButtonClick
        )
    }
}

@Composable
fun ImagesEmptyState() {
    Text(
        stringResource(R.string.images_empty_state_description),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .wrapContentHeight(align = Alignment.CenterVertically),
        textAlign = TextAlign.Center
    )
}

@Composable
fun Loading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}

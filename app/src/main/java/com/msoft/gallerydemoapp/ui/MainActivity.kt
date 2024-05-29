package com.msoft.gallerydemoapp.ui

import android.app.RecoverableSecurityException
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.msoft.gallerydemoapp.R
import com.msoft.gallerydemoapp.ui.theme.GalleryDemoAppTheme
import com.msoft.gallerydemoapp.ui.views.AlertDialog
import com.msoft.gallerydemoapp.ui.vm.ImageDetailViewModel
import com.msoft.gallerydemoapp.ui.vm.ImagesListViewModel
import com.msoft.gallerydemoapp.utils.DELETE_IMAGES_PERMISSION
import com.msoft.gallerydemoapp.utils.READ_IMAGES_PERMISSIONS
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val imagesListViewModel: ImagesListViewModel by viewModels()
    private val imageDetailViewModel: ImageDetailViewModel by viewModels()

    private val requestShowImagesPermissionsLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        if (permissions.all { true }) imagesListViewModel.onShowImagesPermissionsGranted()
    }

    private val requestDeletePermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) imagesListViewModel.onDeleteImagesPermissionsGranted()
    }

    private val intentLauncher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
        if (result.resultCode == RESULT_OK) imagesListViewModel.reloadImages()
    }

    private val deleteImageLauncher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
        if (result.resultCode == RESULT_OK) deleteImage(imageDetailViewModel.currentUri.value)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GalleryDemoAppTheme {
                val navController = rememberNavController()
                val uiState = imagesListViewModel.uiState.collectAsState()
                val imagesData = imagesListViewModel.imagesData.collectAsState()
                val deleteImagesPermissionGranted = imagesListViewModel.deleteImagesPermissionGranted.collectAsState()

                NavHost(navController = navController, startDestination = "images") {
                    composable("images") {
                        ImagesScreen(
                            uiState.value,
                            imagesData.value,
                            { requestShowImagesPermissionsLauncher.launch(READ_IMAGES_PERMISSIONS.toTypedArray()) },
                            { navController.navigate("imageDetail/${Uri.encode(it.uri.toString())}") }
                        )
                    }
                    composable("imageDetail/{uri}", arguments = listOf(navArgument("uri") { type = NavType.StringType })) { backStackEntry ->
                        val uri = backStackEntry.arguments?.getString("uri") ?: return@composable
                        imageDetailViewModel.setCurrentUri(uri)

                        val isFavourite = imageDetailViewModel.isFavourite.collectAsState()
                        var showAlertDialog by remember { mutableStateOf(false) }

                        ImageDetailScreen(
                            uri,
                            isFavourite.value,
                            imageDetailViewModel.getImageInfo(uri),
                            { navController.popBackStack() },
                            { imageDetailViewModel.switchFavouriteButtonClick(uri) },
                            {
                                if (deleteImagesPermissionGranted.value) {
                                    showAlertDialog = true
                                } else {
                                    DELETE_IMAGES_PERMISSION?.let { requestDeletePermissionLauncher.launch(it) }
                                }
                            })

                        if (showAlertDialog) {
                            AlertDialog(
                                stringResource(id = R.string.delete_image_dialog_title_text),
                                stringResource(id = R.string.delete_image_dialog_confirm_button_text),
                                stringResource(id = R.string.delete_image_dialog_dismiss_button_text),
                                {
                                    deleteImage(uri)

                                    showAlertDialog = false
                                    navController.popBackStack()
                                },
                                { showAlertDialog = false })
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        imagesListViewModel.reloadImages()  // I was unable to find some reactive mechanism to update images when they get changed outside of this app :/
    }

    private fun deleteImage(uri: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            intentLauncher.launch(IntentSenderRequest.Builder(imageDetailViewModel.getDeleteImageIntent(uri).intentSender).build())
        } else {
            try {
                imageDetailViewModel.deleteImage(uri)
            } catch (e: SecurityException) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    (e as? RecoverableSecurityException)?.let { ex ->
                        deleteImageLauncher.launch(IntentSenderRequest.Builder(ex.userAction.actionIntent.intentSender).build())
                    }
                }
            }
            imagesListViewModel.reloadImages()
        }
    }
}

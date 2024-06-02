package com.oguzdogdu.walliescompose.features.detail

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.media.MediaScannerConnection
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.oguzdogdu.walliescompose.WalliesApplication
import com.oguzdogdu.walliescompose.domain.model.favorites.FavoriteImages
import com.oguzdogdu.walliescompose.domain.repository.WallpaperRepository
import com.oguzdogdu.walliescompose.domain.wrapper.onFailure
import com.oguzdogdu.walliescompose.domain.wrapper.onLoading
import com.oguzdogdu.walliescompose.domain.wrapper.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import javax.inject.Inject

@HiltViewModel
class DetailViewModel@Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: WallpaperRepository,
) : ViewModel() {
    private val _getPhoto = MutableStateFlow(DetailState())
    val photo = _getPhoto.asStateFlow()

    private val _downloadBottomSheetOpenStat = MutableStateFlow(false)
    val downloadBottomSheetOpenStat = _downloadBottomSheetOpenStat.asStateFlow()

    private val _setWallpaperBottomSheetOpenStat = MutableStateFlow(false)
    val setWallpaperBottomSheetOpenStat = _setWallpaperBottomSheetOpenStat.asStateFlow()

    private val _photoQualityType = MutableStateFlow("")
    val photoQualityType = _photoQualityType.asStateFlow()

    private val _setWallpaperPlace = MutableStateFlow("")
    val setWallpaperPlace = _setWallpaperPlace.asStateFlow()

     val id: String = checkNotNull(savedStateHandle.toRoute<DetailScreenRoute>().photoId)

    fun handleScreenEvents(event: DetailScreenEvent) {
        when (event) {
            is DetailScreenEvent.GetPhotoDetails -> {
                getSinglePhoto()
            }

            is DetailScreenEvent.AddFavorites -> addImagesToFavorites(
              event.favorites
            )

            is DetailScreenEvent.DeleteFavorites -> deleteImageFromFavorites(
              event.favorites
            )

            is DetailScreenEvent.GetFavoriteCheckStat -> getFavoritesFromRoom(id)
            is DetailScreenEvent.OpenDownloadBottomSheet -> {
                _downloadBottomSheetOpenStat.value = event.isOpen
            }

            is DetailScreenEvent.PhotoQualityType ->  {
                _photoQualityType.value = event.type.name
            }

            is DetailScreenEvent.OpenSetWallpaperBottomSheet -> {
                _setWallpaperBottomSheetOpenStat.value = event.isOpen
            }

            is DetailScreenEvent.SetWallpaperPlace -> {
                _setWallpaperPlace.value = event.type.name
            }
        }
    }

    private fun getSinglePhoto() {
        viewModelScope.launch {
            repository.getPhoto(id = id).collectLatest { result ->
                result.onLoading {
                    _getPhoto.updateAndGet { it.copy(loading = true) }
                }

                result.onSuccess { photo ->
                    _getPhoto.updateAndGet { it.copy(loading = false,detail = photo)}
                }

                result.onFailure { error ->
                    _getPhoto.updateAndGet {
                        it.copy(errorMessage = error)
                    }
                }
            }
        }
    }

    fun setNullValueOfImageUrl() {
        viewModelScope.launch {
            _photoQualityType.value = ""
        }
    }

    private fun addImagesToFavorites(
        favoriteImage: FavoriteImages,
        ) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertImageToFavorites(favoriteImage)
    }

    private fun deleteImageFromFavorites(
        favoriteImage: FavoriteImages
    ) = viewModelScope.launch {
        repository.deleteFavorites(favoriteImage)
    }

    private fun getFavoritesFromRoom(id: String?) {
        viewModelScope.launch {
            repository.getFavorites().collectLatest { result ->
                result.onSuccess { list ->
                    val matchingFavorite = list?.find { it.id == id }
                    _getPhoto.updateAndGet {
                        it.copy(favorites = matchingFavorite)
                    }
                }
            }
        }
    }
}
enum class TypeOfPhotoQuality {
    LOW,
    MEDIUM,
    HIGH,
    RAW
}
enum class TypeOfSetWallpaper {
    LOCK,
    HOME,
    HOME_AND_LOCK
}
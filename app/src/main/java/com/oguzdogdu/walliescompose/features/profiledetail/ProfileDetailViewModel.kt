package com.oguzdogdu.walliescompose.features.profiledetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.walliescompose.domain.repository.UnsplashUserRepository
import com.oguzdogdu.walliescompose.domain.wrapper.onFailure
import com.oguzdogdu.walliescompose.domain.wrapper.onLoading
import com.oguzdogdu.walliescompose.domain.wrapper.onSuccess
import com.oguzdogdu.walliescompose.features.profiledetail.event.ProfileDetailEvent
import com.oguzdogdu.walliescompose.features.profiledetail.state.ProfileDetailState
import com.oguzdogdu.walliescompose.features.profiledetail.state.UserCollectionState
import com.oguzdogdu.walliescompose.features.profiledetail.state.UserPhotosState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileDetailViewModel @Inject constructor(
    private val unsplashUserRepository: UnsplashUserRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val username: String = checkNotNull(savedStateHandle["username"])

    private val _getUserDetails = MutableStateFlow(ProfileDetailState())
    val getUserDetails = _getUserDetails.asStateFlow()

    private val _getUserPhotoList = MutableStateFlow(UserPhotosState())
    val getUserPhotoList = _getUserPhotoList.asStateFlow()

    private val _getUserCollectionList = MutableStateFlow(UserCollectionState())
    val getUserCollectionList = _getUserCollectionList.asStateFlow()

    fun handleUIEvent(event: ProfileDetailEvent) {
        when (event) {
            is ProfileDetailEvent.FetchUserDetailInfos -> getUserDetails()
            ProfileDetailEvent.FetchUserCollectionsList -> getUsersCollections()
            ProfileDetailEvent.FetchUserPhotosList -> getUsersPhotos()
        }
    }

    private fun getUserDetails() {
        viewModelScope.launch {
            unsplashUserRepository.getUserDetails(username).collectLatest { result ->
                result.onLoading {
                    _getUserDetails.update {
                        it.copy(loading = true)
                    }
                }
                result.onSuccess { userDetails ->
                    _getUserDetails.update {
                        it.copy(loading = false, userDetails = userDetails)
                    }
                }
                result.onFailure { error ->
                    _getUserDetails.update {
                        it.copy(loading = false, errorMessage = error)
                    }
                }
            }
        }
    }

    private fun getUsersPhotos() {
        viewModelScope.launch {
            _getUserPhotoList.update { it.copy(loading = true) }

            unsplashUserRepository.getUsersPhotos(username).collectLatest { result ->
                result.onSuccess { list ->
                    _getUserPhotoList.update { it.copy(loading = false, usersPhotos = list) }
                }

                result.onFailure { error ->
                    _getUserPhotoList.update { it.copy(loading = false, errorMessage = error) }
                }
            }
        }
    }

    private fun getUsersCollections() {
        viewModelScope.launch {
            _getUserCollectionList.update { it.copy(loading = true) }

            unsplashUserRepository.getUsersCollections(username).collectLatest { result ->
                result.onSuccess { list ->
                    _getUserCollectionList.update {
                        it.copy(loading = false, usersCollection = list)
                    }
                }

                result.onFailure { error ->
                    _getUserCollectionList.update { it.copy(loading = false, errorMessage = error) }
                }
            }
        }
    }
}

package com.khalidtouch.classifiadmin.feeds

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khalidtouch.chatme.domain.models.UserNewsFeed
import com.khalidtouch.chatme.domain.repository.FeedQuery
import com.khalidtouch.chatme.domain.repository.UserDataRepository
import com.khalidtouch.chatme.domain.usecases.GetLikedUserNewsResourcesUseCase
import com.khalidtouch.chatme.domain.usecases.GetUserNewsResourcesUseCase
import com.khalidtouch.classifiadmin.model.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FeedsViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    getUserNewsResource: GetUserNewsResourcesUseCase,
    getLikedUserNewsResource: GetLikedUserNewsResourcesUseCase,
) : ViewModel() {
    private val shouldShowOnboarding: Flow<Boolean> =
        userDataRepository.userData.map { !it.shouldHideOnboarding }

    val feedState: StateFlow<NewsFeedUiState> =
        userDataRepository.getAssignedUserNewsResources(getUserNewsResource)
            .map(NewsFeedUiState::Success)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = NewsFeedUiState.Loading,
            )


    val onboardingUiState: StateFlow<OnboardingUiState> =
        shouldShowOnboarding.map {
            if (it) {
                OnboardingUiState.Shown
            } else {
                OnboardingUiState.NotShown
            }
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = OnboardingUiState.Loading
            )


    fun toggleLikeStateOfFeed(feedId: Long, isLiked: Boolean) {
        viewModelScope.launch { userDataRepository.toggleLikedFeedId(feedId, isLiked) }
    }

    fun dismissOnboarding() {
        viewModelScope.launch { userDataRepository.setShouldHideOnboarding(true) }
    }
}

private fun UserDataRepository.getLikedUserNewsResources(
    getLikedUserNewsResource: GetLikedUserNewsResourcesUseCase,
): Flow<List<UserNewsFeed>> = userData
    .map { userData ->
        userData.likedFeeds
    }
    .distinctUntilChanged()
    .flatMapLatest { likedFeeds ->
        if (likedFeeds.isEmpty()) {
            flowOf(emptyList())
        } else {
            getLikedUserNewsResource(
                FeedQuery(
                    filterByFeedIds = likedFeeds,
                )
            )
        }
    }


private fun UserDataRepository.getAssignedUserNewsResources(
    getUserNewsResource: GetUserNewsResourcesUseCase,
): Flow<List<UserNewsFeed>> = userData
    .map { userData ->
        if (userData.shouldShowEmptyFeed()) null else {
            userData.assignedFeeds
        }
    }
    .distinctUntilChanged()
    .flatMapLatest { assignedFeeds ->
        if (assignedFeeds == null) {
            flowOf(emptyList())
        } else {
            getUserNewsResource(
                FeedQuery(
                    filterByFeedIds = assignedFeeds
                )
            )
        }
    }

private fun UserData.shouldShowEmptyFeed() =
    !shouldHideOnboarding && assignedFeeds.isEmpty()
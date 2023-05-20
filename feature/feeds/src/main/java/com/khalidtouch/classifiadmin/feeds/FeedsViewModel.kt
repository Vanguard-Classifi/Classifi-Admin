package com.khalidtouch.classifiadmin.feeds

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khalidtouch.chatme.domain.models.UserNewsFeed
import com.khalidtouch.chatme.domain.repository.FeedQuery
import com.khalidtouch.chatme.domain.repository.UserDataRepository
import com.khalidtouch.chatme.domain.usecases.GetUserNewsResourcesUseCase
import com.khalidtouch.classifiadmin.model.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class FeedsViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    getUserNewsResource: GetUserNewsResourcesUseCase,
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
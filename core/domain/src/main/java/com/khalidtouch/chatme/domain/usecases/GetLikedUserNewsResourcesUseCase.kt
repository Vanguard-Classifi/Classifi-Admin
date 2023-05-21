package com.khalidtouch.chatme.domain.usecases

import com.khalidtouch.chatme.domain.models.UserNewsFeed
import com.khalidtouch.chatme.domain.repository.FeedQuery
import com.khalidtouch.chatme.domain.repository.FeedRepository
import com.khalidtouch.chatme.domain.repository.UserDataRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLikedUserNewsResourcesUseCase @Inject constructor(
    private val newsRepository: FeedRepository,
    private val userDataRepository: UserDataRepository,
) {
    operator fun invoke(
        query: FeedQuery = FeedQuery(),
    ): Flow<List<UserNewsFeed>> =
        newsRepository.fetchFeedResources(query).mapToUserNewsResources(
            userDataRepository.userData
        )
}
package com.khalidtouch.chatme.domain.usecases

import com.khalidtouch.chatme.domain.models.UserNewsFeed
import com.khalidtouch.chatme.domain.models.mapToUserNewsFeed
import com.khalidtouch.chatme.domain.repository.FeedQuery
import com.khalidtouch.chatme.domain.repository.FeedRepository
import com.khalidtouch.chatme.domain.repository.UserDataRepository
import com.khalidtouch.classifiadmin.model.UserData
import com.khalidtouch.classifiadmin.model.classifi.ClassifiFeed
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNot
import javax.inject.Inject

class GetUserNewsResourcesUseCase @Inject constructor(
    private val newsRepository: FeedRepository,
    private val userDataRepository: UserDataRepository,
) {
    operator fun invoke(
        query: FeedQuery = FeedQuery(),
    ): Flow<List<UserNewsFeed>> = newsRepository
        .fetchFeedResources(query).mapToUserNewsResources(userDataRepository.userData)
}

internal fun Flow<List<ClassifiFeed>>.mapToUserNewsResources(
    userDataStream: Flow<UserData>
): Flow<List<UserNewsFeed>> =
    filterNot { it.isEmpty() }
        .combine(userDataStream) { newsFeed, userData ->
            newsFeed.mapToUserNewsFeed(userData)
        }
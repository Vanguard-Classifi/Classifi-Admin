package com.khalidtouch.chatme.domain.usecases

import com.khalidtouch.chatme.domain.repository.UserDataRepository
import com.khalidtouch.chatme.domain.repository.UserRepository
import com.khalidtouch.classifiadmin.model.classifi.ClassifiUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val userRepository: UserRepository,
) {
    operator fun invoke(): Flow<Pair<Long, List<ClassifiUser>>> = flow {
        userDataRepository.userData.map {
            val id = it.userId //not yet 1686495289226
            val users = userRepository.fetchAllUsersList()
            emit(Pair(id, users))
        }
    }
}
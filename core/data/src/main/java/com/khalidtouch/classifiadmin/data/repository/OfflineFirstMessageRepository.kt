package com.khalidtouch.classifiadmin.data.repository

import com.khalidtouch.chatme.database.dao.MessageDao
import com.khalidtouch.chatme.domain.repository.MessageRepository
import com.khalidtouch.classifiadmin.data.mapper.ModelEntityMapper
import com.khalidtouch.classifiadmin.model.classifi.ClassifiMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class OfflineFirstMessageRepository @Inject constructor(
    private val modelMapper: ModelEntityMapper,
    private val messageDao: MessageDao,
) : MessageRepository {
    override suspend fun saveMessage(message: ClassifiMessage) {
        messageDao.saveMessageOrIgnore(modelMapper.messageModelToEntity(message)!!)
    }

    override suspend fun saveMessages(messages: List<ClassifiMessage>) {
        messageDao.saveMessagesOrIgnore(messages.map { message ->
            modelMapper.messageModelToEntity(
                message
            )!!
        })
    }

    override suspend fun updateMessage(message: ClassifiMessage) {
        messageDao.updateMessage(modelMapper.messageModelToEntity(message)!!)
    }

    override suspend fun deleteMessage(message: ClassifiMessage) {
        messageDao.deleteMessage(modelMapper.messageModelToEntity(message)!!)
    }

    override fun fetchMessageById(messageId: Long): Flow<ClassifiMessage?> = flow {
        messageDao.fetchMessageById(messageId).collect {
            val message = modelMapper.messageEntityToModel(it)
            emit(message)
        }
    }

    override fun fetchMessagesByFeed(feedId: Long): Flow<List<ClassifiMessage>> = flow {
        messageDao.fetchMessagesByFeed(feedId).collect {
            val messages = it.map { message -> modelMapper.messageEntityToModel(message)!! }
            emit(messages)
        }
    }
}
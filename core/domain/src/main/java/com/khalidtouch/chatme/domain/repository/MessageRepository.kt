package com.khalidtouch.chatme.domain.repository

import com.khalidtouch.classifiadmin.model.classifi.ClassifiMessage
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    suspend fun saveMessage(message: ClassifiMessage)

    suspend fun saveMessages(messages: List<ClassifiMessage>)

    suspend fun updateMessage(message: ClassifiMessage)

    suspend fun deleteMessage(message: ClassifiMessage)

    fun fetchMessageById(messageId: Long): Flow<ClassifiMessage?>

    fun fetchMessagesByFeed(feedId: Long): Flow<List<ClassifiMessage>>
}
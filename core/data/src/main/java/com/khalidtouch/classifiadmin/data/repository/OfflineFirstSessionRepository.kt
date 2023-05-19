package com.khalidtouch.classifiadmin.data.repository

import com.khalidtouch.chatme.database.dao.SessionDao
import com.khalidtouch.chatme.domain.repository.SessionRepository
import com.khalidtouch.classifiadmin.data.mapper.ModelEntityMapper
import com.khalidtouch.classifiadmin.model.classifi.ClassifiAcademicSession
import javax.inject.Inject

class OfflineFirstSessionRepository @Inject constructor(
    private val modelMapper: ModelEntityMapper,
    private val sessionDao: SessionDao,
) : SessionRepository {
    override suspend fun saveSession(session: ClassifiAcademicSession) {
        sessionDao.saveSessionOrIgnore(modelMapper.sessionModelToEntity(session)!!)
    }

    override suspend fun saveSessions(sessions: List<ClassifiAcademicSession>) {
        sessionDao.saveSessionsOrIgnore(sessions.map { session ->
            modelMapper.sessionModelToEntity(
                session
            )!!
        })
    }

    override suspend fun updateSession(session: ClassifiAcademicSession) {
        sessionDao.updateSession(modelMapper.sessionModelToEntity(session)!!)
    }

    override suspend fun updateSessions(sessions: List<ClassifiAcademicSession>) {
        sessionDao.updateSessions(sessions.map { session -> modelMapper.sessionModelToEntity(session)!! })
    }

    override suspend fun deleteSession(session: ClassifiAcademicSession) {
        sessionDao.deleteSession(modelMapper.sessionModelToEntity(session)!!)
    }

    override suspend fun deleteSessionsById(ids: List<Long>) {
        sessionDao.deleteSessionsById(ids)
    }

    override suspend fun fetchSessionById(id: Long): ClassifiAcademicSession? {
       val session = sessionDao.fetchSessionById(id)
        return modelMapper.sessionEntityToModel(session)
    }
}
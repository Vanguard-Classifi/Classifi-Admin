package com.khalidtouch.chatme.domain.repository

import com.khalidtouch.classifiadmin.model.classifi.ClassifiAcademicSession

interface SessionRepository {
    suspend fun saveSession(session: ClassifiAcademicSession)

    suspend fun saveSessions(sessions: List<ClassifiAcademicSession>)
    suspend fun updateSession(session: ClassifiAcademicSession)

    suspend fun updateSessions(sessions: List<ClassifiAcademicSession>)

    suspend fun deleteSession(session: ClassifiAcademicSession)

    suspend fun deleteSessionsById(ids: List<Long>)

    suspend fun fetchSessionById(id: Long): ClassifiAcademicSession?


}
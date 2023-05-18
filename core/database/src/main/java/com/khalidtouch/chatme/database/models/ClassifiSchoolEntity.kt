package com.khalidtouch.chatme.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime
import java.util.Date



@Entity
data class ClassifiSchoolEntity(
    @PrimaryKey(autoGenerate = true) val schoolId: Long,
    var schoolName: String? = null,
    var address: String? = null,
    var description: String? = null,
    var bannerImage: String? = null,
    var dateCreated: LocalDateTime? = null,
)
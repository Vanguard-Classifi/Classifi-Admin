package com.khalidtouch.chatme.network

import com.khalidtouch.classifiadmin.model.classifi.ClassifiSchool
import com.khalidtouch.classifiadmin.model.utils.OnCreateBatchAccountResult
import com.khalidtouch.classifiadmin.model.utils.StagedUser

interface CreateAccountForTeachers {
    fun createAccountForTeachers(
        mySchool: ClassifiSchool?,
        teachers: List<StagedUser>,
        result: OnCreateBatchAccountResult
    )
}
package com.khalidtouch.chatme.network

import com.khalidtouch.classifiadmin.model.classifi.ClassifiSchool
import com.khalidtouch.classifiadmin.model.utils.OnCreateBatchAccountResult
import com.khalidtouch.classifiadmin.model.utils.StageTeacher

interface CreateAccountForTeachers {
    fun createAccountForTeachers(
        mySchool: ClassifiSchool?,
        teachers: List<StageTeacher>,
        result: OnCreateBatchAccountResult
    )
}
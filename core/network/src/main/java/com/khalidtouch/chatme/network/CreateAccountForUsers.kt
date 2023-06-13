package com.khalidtouch.chatme.network

import com.khalidtouch.classifiadmin.model.classifi.ClassifiSchool
import com.khalidtouch.classifiadmin.model.utils.OnCreateBatchAccountResult
import com.khalidtouch.classifiadmin.model.utils.StagedUser

interface CreateAccountForUsers {
    fun createAccountForUsers(
        mySchool: ClassifiSchool?,
        users: List<StagedUser>,
        result: OnCreateBatchAccountResult
    )
}
package com.khalidtouch.chatme.network

import com.khalidtouch.classifiadmin.model.classifi.ClassifiSchool
import com.khalidtouch.classifiadmin.model.utils.OnCreateBatchAccountResult
import com.khalidtouch.classifiadmin.model.utils.StagedUser

interface CreateAccountForParents {
    fun createAccountForParents(
        mySchool: ClassifiSchool?,
        parents: List<StagedUser>,
        result: OnCreateBatchAccountResult
    )
}
package com.khalidtouch.classifiadmin.data.repository

import android.service.autofill.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {
    val userData: Flow<UserData>
}
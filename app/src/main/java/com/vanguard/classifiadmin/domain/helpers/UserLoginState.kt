package com.vanguard.classifiadmin.domain.helpers

sealed class UserLoginState {
    object Registered: UserLoginState()
    object SchoolCreator: UserLoginState()
}
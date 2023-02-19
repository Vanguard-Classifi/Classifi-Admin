package com.vanguard.classifiadmin.domain.helpers

enum class AuthExceptionState {
    InvalidUserCredentials,
    UserAlreadyExists,
    NetworkProblem,
    InvalidEmail,
    InvalidUser,
}
package com.khalidtouch.classifiadmin.model.utils


sealed interface OnCreateAccountState {
    object Starting: OnCreateAccountState
    object Success : OnCreateAccountState
    object Failed : OnCreateAccountState
    object EmptyEmailOrPassword : OnCreateAccountState
    object PasswordNotMatched : OnCreateAccountState
    object InvalidCredentials : OnCreateAccountState
    object InvalidUser : OnCreateAccountState
    object UserAlreadyExists : OnCreateAccountState
    object WeakPassword : OnCreateAccountState
    object EmailNotFound : OnCreateAccountState
    object NetworkProblem : OnCreateAccountState
}
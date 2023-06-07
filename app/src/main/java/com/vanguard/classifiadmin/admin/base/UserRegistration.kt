package com.vanguard.classifiadmin.admin.base

import android.os.Handler
import android.os.Looper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

abstract class UserRegistration {
    protected val authentication: FirebaseAuth = Firebase.auth
    protected val fireStore: FirebaseFirestore = Firebase.firestore

    protected fun postAction(action: () -> Unit) =
        Handler(Looper.getMainLooper()).post { action() }
}


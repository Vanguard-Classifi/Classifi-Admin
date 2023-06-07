package com.vanguard.classifiadmin.network.constants

import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

object CloudStoreConstants {
    object School {
        fun schoolBannerRef(schoolId: Long, filename: String): StorageReference {
            val ref =  Firebase.storage.getReference("school/$schoolId/banner")
            return ref.child(filename)
        }
    }
}
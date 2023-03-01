package com.vanguard.classifiadmin.di

import com.vanguard.classifiadmin.data.network.firestore.FirestoreManager
import com.vanguard.classifiadmin.data.network.firestore.FirestoreManagerImpl
import com.vanguard.classifiadmin.data.network.storage.FirebaseStorage
import com.vanguard.classifiadmin.data.network.storage.FirebaseStorageImpl
import com.vanguard.classifiadmin.domain.auth.FirebaseAuthManager
import com.vanguard.classifiadmin.domain.auth.FirebaseAuthManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class FirebaseModule {

    @Binds
    @Singleton
    abstract fun provideFirebaseAuthManager(impl: FirebaseAuthManagerImpl): FirebaseAuthManager

    @Binds
    @Singleton
    abstract fun provideFirestoreManager(impl: FirestoreManagerImpl): FirestoreManager

    @Binds
    @Singleton
    abstract fun provideFirebaseStorage(impl: FirebaseStorageImpl): FirebaseStorage

}
package com.khalidtouch.chatme.network

import javax.inject.Qualifier

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class Dispatcher(val dispatcher: ClassifiDispatcher)

enum class ClassifiDispatcher { IO, }
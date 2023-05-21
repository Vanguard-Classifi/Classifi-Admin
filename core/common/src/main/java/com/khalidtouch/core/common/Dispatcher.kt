package com.khalidtouch.core.common

import javax.inject.Qualifier

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class Dispatcher(val dispatcher: ClassifiDispatcher)

enum class ClassifiDispatcher { IO, }
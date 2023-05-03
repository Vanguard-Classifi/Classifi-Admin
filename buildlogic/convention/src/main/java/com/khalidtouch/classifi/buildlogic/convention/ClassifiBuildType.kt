package com.khalidtouch.classifi.buildlogic.convention

@Suppress("unused")
enum class ClassifiBuildType(val applicationIdSuffix: String? = null) {
    DEBUG(".debug"),
    RELEASE,
    BENCHMARK(".benchmark")
}
package com.vanguard.classifiadmin.domain.helpers

object MimeType {
    fun pdf(): Array<String> {
        return arrayOf("application/pdf")
    }

    fun images(): Array<String> {
        return arrayOf("image/png", "image/jpeg")
    }
}
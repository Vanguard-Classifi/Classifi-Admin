package com.khalidtouch.classifiadmin.model

data class Country(
    var name: String? = null,
    var code: String? = null,
)


data class PagedCountry(
    val countries: List<Country>,
    val page: Int,
    val totalSize: Int,
)
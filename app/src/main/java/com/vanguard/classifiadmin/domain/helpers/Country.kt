package com.vanguard.classifiadmin.domain.helpers

import android.telephony.PhoneNumberUtils
import com.vanguard.classifiadmin.domain.extensions.splitWithSpace
import java.util.Locale

data class Country(
    val name: String,
    val code: String,
) {
    companion object {
        fun all(): List<Country> {
            val isoCountries  = Locale.getISOCountries()
            val countries = ArrayList<Country>()
            isoCountries.map { iso ->
                val locale = Locale("", iso)
                countries.add(Country(
                    name  = locale.displayCountry.splitWithSpace().first(),
                    code = ""
                ))
            }
            return countries
        }
    }
}
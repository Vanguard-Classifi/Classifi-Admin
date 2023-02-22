package com.vanguard.classifiadmin.domain.helpers

import java.util.Locale

class Country {
    companion object {
        fun getAllCountries(): List<String> {
            val names = ArrayList<String>()
            for (locale in Locale.getAvailableLocales()) {
                val name = locale.displayCountry
                if (name.trim().isNotEmpty() && !names.contains((name))) {
                    names.add(name)
                }
            }
            names.sort()
            return names
        }
    }
}
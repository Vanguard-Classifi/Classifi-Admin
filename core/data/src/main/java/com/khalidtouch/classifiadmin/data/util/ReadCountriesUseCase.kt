package com.khalidtouch.classifiadmin.data.util

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.khalidtouch.classifiadmin.model.Country
import com.khalidtouch.classifiadmin.model.PagedCountry
import java.io.IOException
import javax.inject.Inject

class ReadCountriesUseCase @Inject constructor() {
    val TAG = "ReadCo"
    operator fun invoke(context: Context, page: Int, limit: Int): PagedCountry {
        return try {
            val json = context.assets.open("countries.json")
                .bufferedReader()
                .use { it.readText() }
            val countryType = object : TypeToken<List<Country>>() {}.type
            val countries = Gson().fromJson<List<Country>>(json, countryType)
            Log.e(TAG, "invoke: size of countries ${countries.size}")
            val index = if(page > 0) page - 1 else 0
            Log.e(TAG, "invoke: current page $page")
            val resources = countries.chunked(limit)
            Log.e(TAG, "invoke: size of resources ${resources[index]}.size")
            PagedCountry(
                countries = resources[index],
                page = page,
                totalSize = countries.size
            )
        } catch (e: IOException) {
            e.printStackTrace()
            return PagedCountry(emptyList(), -1, 0)
        }
    }
}